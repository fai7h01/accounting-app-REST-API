package com.cydeo.service.impl;

import com.cydeo.dto.*;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.exception.InvoiceNotFoundException;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.*;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MapperUtil mapperUtil, CompanyService companyService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService) {
        this.invoiceRepository = invoiceRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
    }

    @Override
    public InvoiceDto save(InvoiceDto invoiceDto, InvoiceType invoiceType) {
        ClientVendorDto clientVendorDto = clientVendorService.findByName(invoiceDto.getClientVendor().getClientVendorName());
        InvoiceDto generatedInvoice = generateInvoice(invoiceDto, invoiceType);
        invoiceDto.setClientVendor(clientVendorDto);
        Invoice savedInvoice = invoiceRepository.save(mapperUtil.convert(generatedInvoice, new Invoice()));
        return mapperUtil.convert(savedInvoice, new InvoiceDto());
    }

    @Override
    public InvoiceDto findById(Long id) {
        Invoice foundInvoice = invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found."));
        return mapperUtil.convert(foundInvoice, new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> listAllByType(InvoiceType invoiceType) {
        Long id = companyService.getCompanyDtoByLoggedInUser().getId();
        return invoiceRepository.findByInvoiceTypeAndCompanyIdOrderByInvoiceNoDesc(invoiceType, id).stream()
                .map(invoice -> {
                    InvoiceDto invoiceDto = mapperUtil.convert(invoice, new InvoiceDto());
                    setPriceTaxAndTotal(invoiceDto);
                    return invoiceDto;
                })
                .toList();
    }

    @Override
    public InvoiceDto generateInvoice(InvoiceDto invoiceDto, InvoiceType invoiceType) {

        int currentNo = listAllByType(invoiceType).size();
        String prefix = invoiceType.equals(InvoiceType.PURCHASE) ? "P" : "S";
        String invoiceNo = String.format("%s-%03d", prefix, currentNo + 1);
        invoiceDto.setInvoiceNo(invoiceNo);
        invoiceDto.setDate(LocalDateTime.now());
        invoiceDto.setInvoiceType(invoiceType);
        invoiceDto.setCompany(companyService.getCompanyDtoByLoggedInUser());
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);

        return invoiceDto;
    }

    @Override
    public InvoiceDto update(Long id, InvoiceDto invoiceDto) {
        ClientVendorDto clientVendor = clientVendorService.findByName(invoiceDto.getClientVendor().getClientVendorName());
        Invoice foundInvoice = invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found."));
        InvoiceDto invoiceInDB = mapperUtil.convert(foundInvoice, new InvoiceDto());
        invoiceInDB.setClientVendor(clientVendor);
        Invoice saved = invoiceRepository.save(mapperUtil.convert(invoiceInDB, new Invoice()));
        return mapperUtil.convert(saved, new InvoiceDto());
    }


    @Override
    public void delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found."));
        invoice.setIsDeleted(true);
        List<InvoiceProductDto> invoiceProducts = invoiceProductService.listAllByInvoiceId(invoice.getId());
        invoiceProducts.forEach(ip -> invoiceProductService.delete(ip.getId()));
        invoiceRepository.save(invoice);
    }

    @Override
    public void approve(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found."));
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDateTime.now());
        invoiceRepository.save(invoice);
        if (invoice.getInvoiceType().equals(InvoiceType.PURCHASE)) {
            invoiceProductService.updateQuantityInStockForPurchase(id);
            invoiceProductService.updateRemainingQuantityUponApproval(id);
        } else {
            invoiceProductService.updateQuantityInStockForSale(id);
            invoiceProductService.calculateProfitLoss(id);
        }
    }

    @Override
    public void setPriceTaxAndTotal(InvoiceDto invoiceDto) {
        List<InvoiceProductDto> invoiceProductDtoList = invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(invoiceDto.getId());
        BigDecimal totalPrice = invoiceProductDtoList.stream().map(invoiceProductService::getInvoiceProductTotalWithoutTax).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalWithTax = invoiceProductDtoList.stream().map(invoiceProductService::getInvoiceProductTotalWithTax).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalTax = totalWithTax.subtract(totalPrice);
        invoiceDto.setPrice(totalPrice);
        invoiceDto.setTax(totalTax);
        invoiceDto.setTotal(totalWithTax);
    }

    @Override
    public InvoiceDto printInvoice(Long id) {
        InvoiceDto dto = findById(id);
        setPriceTaxAndTotal(dto);
        return dto;
    }

    @Override
    public Map<String, BigDecimal> getMonthlyProfitLossMap() {
        Map<String, BigDecimal> monthlyProfitLoss = new HashMap<>();

        List<Invoice> salesInvoices = invoiceRepository.findApprovedSalesInvoices(companyService.getCompanyDtoByLoggedInUser().getId());
        List<InvoiceProductDto> salesInvoiceProducts = salesInvoices.stream()
                .sorted(Comparator.comparing(Invoice::getDate).reversed())
                .flatMap(invoice -> invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(invoice.getId()).stream()).toList();

        for (InvoiceProductDto salesInvoiceProduct : salesInvoiceProducts) {
            String key = salesInvoiceProduct.getInvoice().getDate().getYear() + " " + salesInvoiceProduct.getInvoice().getDate().getMonth();
            BigDecimal profitLoss = salesInvoiceProduct.getProfitLoss();
            monthlyProfitLoss.put(key, monthlyProfitLoss.getOrDefault(key, BigDecimal.ZERO).add(profitLoss));
        }
        return monthlyProfitLoss;
    }

    @Override
    public List<InvoiceDto> listLastThreeApproved() {
        return invoiceRepository.findTop3ByCompanyIdAndInvoiceStatusOrderByDateDesc(companyService.getCompanyDtoByLoggedInUser().getId(), InvoiceStatus.APPROVED)
                .stream().map(invoice -> {
                    InvoiceDto dto = mapperUtil.convert(invoice, new InvoiceDto());
                    setPriceTaxAndTotal(dto);
                    return dto;
                }).toList();
    }

    @Override
    public BigDecimal countTotal(InvoiceType invoiceType) {
        return listAllByType(invoiceType).stream()
                .filter(invoiceDto -> invoiceDto.getInvoiceStatus().equals(InvoiceStatus.APPROVED))
                .map(InvoiceDto::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumProfitLoss() {
        return invoiceRepository.findApprovedSalesInvoices(companyService.getCompanyDtoByLoggedInUser().getId())
                .stream().map(invoice -> invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(invoice.getId())
                        .stream().map(InvoiceProductDto::getProfitLoss).reduce(BigDecimal.ZERO, BigDecimal::add))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
