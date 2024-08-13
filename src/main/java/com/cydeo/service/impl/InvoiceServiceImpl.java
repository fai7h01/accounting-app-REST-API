package com.cydeo.service.impl;

import com.cydeo.dto.*;
import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.*;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
        InvoiceDto generatedInvoice = generateInvoice(invoiceDto, invoiceType);
        Invoice savedInvoice = invoiceRepository.save(mapperUtil.convert(generatedInvoice, new Invoice()));
        return mapperUtil.convert(savedInvoice, new InvoiceDto());
    }

    @Override
    public InvoiceDto findById(Long id) {
        Invoice foundInvoice = invoiceRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Invoice " + id + " not found"));
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

        ClientVendorDto clientVendorDto = clientVendorService.findByName(invoiceDto.getClientVendor().getClientVendorName());

        int currentNo = listAllByType(invoiceType).size();
        String prefix = invoiceType.equals(InvoiceType.PURCHASE) ? "P" : "S";
        String invoiceNo = String.format("%s-%03d", prefix, currentNo + 1);
        invoiceDto.setInvoiceNo(invoiceNo);
        invoiceDto.setDate(LocalDateTime.now());
        invoiceDto.setInvoiceType(invoiceType);
        invoiceDto.setCompany(companyService.getCompanyDtoByLoggedInUser());
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.getClientVendor().setId(clientVendorDto.getId());
        return invoiceDto;
    }

//    @Override
//    public InvoiceDto generateInvoiceForCompanyByType(InvoiceType invoiceType) {
//
//        InvoiceDto invoiceDto = new InvoiceDto();
//
//        String prefix;
//        int currentInvNum;
//
//        List<InvoiceDto> invoiceDtoList = listAllByTypeAndCompany(invoiceType);
//
//        if (invoiceType == InvoiceType.SALES) prefix = "S";
//        else prefix = "P";
//
//        if (invoiceDtoList.isEmpty()) currentInvNum = 1;
//        else {
//            String numPart = invoiceDtoList.get(0).getInvoiceNo().substring(2);
//            currentInvNum = Integer.parseInt(numPart) + 1;
//        }
//
//        invoiceDto.setInvoiceNo(String.format("%s-%03d", prefix, currentInvNum));
//        invoiceDto.setDate(LocalDateTime.now());
//        return invoiceDto;
//    }


    @Override
    public void delete(Long id) {

        Optional<Invoice> invoice = invoiceRepository.findById(id);

        if (invoice.isPresent()) {
            invoice.get().setIsDeleted(true);
            invoiceRepository.save(invoice.get());
        }
        if (invoice.get().getInvoiceType().equals(InvoiceType.PURCHASE)) {
            List<InvoiceProductDto> invoiceProductDtos = invoiceProductService.listAllByInvoiceId(invoice.get().getId());
            invoiceProductDtos.forEach(ip -> invoiceProductService.deleteById(ip.getId()));
        }

    }


//    @Override
//    public void update(InvoiceDto invoiceDto) {
//        Invoice invoice = invoiceRepository.findById(invoiceDto.getId()).orElseThrow(IllegalArgumentException::new);
//        invoiceDto.setInvoiceStatus(invoice.getInvoiceStatus());
//        invoiceDto.setCompany(new CompanyDto());
//
//        save(invoiceDto, invoice.getInvoiceType());
//    }

//    @Override
//    public List<InvoiceDto> listAllByClientVendor(ClientVendor clientVendor) {
//        List<Invoice> invoiceList = invoiceRepository.findByClientVendor(clientVendor);
//
//        return invoiceList.stream().map(invoice -> mapperUtil.convert(invoice, new InvoiceDto())).collect(Collectors.toList());
//    }
//
//    @Override
//    public void approve(InvoiceDto invoiceDto, InvoiceType invoiceType) {
//        invoiceDto.setInvoiceStatus(InvoiceStatus.APPROVED);
//        invoiceDto.setDate(LocalDateTime.now());
//        List<InvoiceProductDto> invoiceProductDtos = invoiceProductService.listAllByInvoiceId(invoiceDto.getId());
//
//        if (invoiceDto.getInvoiceType().equals(InvoiceType.PURCHASE)) {
//            invoiceProductDtos.forEach(i -> {
//                InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductService.findById(i.getId()), new InvoiceProduct());
//                invoiceProduct.setRemainingQuantity(i.getQuantity());
//                invoiceProductService.save(mapperUtil.convert(invoiceProduct, new InvoiceProductDto()));
//                ProductDto productDto = i.getProduct();
//                productDto.setQuantityInStock(i.getProduct().getQuantityInStock() + i.getQuantity());
//                productService.save(productDto);
//            });
//        } else {
//            invoiceProductService.updateQuantityInStockForSale(invoiceDto.getId());
//            invoiceProductService.calculateProfitLoss(invoiceDto.getId());
//        }
//        invoiceProductDtos.forEach(i -> i.getProduct().setQuantityInStock(i.getProduct().getQuantityInStock() + i.getQuantity()));
//        save(invoiceDto, invoiceType);
//    }
//
//    @Override
//    public List<InvoiceDto> listTop3Approved(InvoiceStatus status) {
//        String title = companyService.getCompanyDtoByLoggedInUser().getTitle();
//        List<Invoice> top3Approved = invoiceRepository.findTop3ByInvoiceStatusAndCompany_TitleOrderByDateDesc(InvoiceStatus.APPROVED, title);
//
//        return top3Approved.stream().map(each -> setPriceTaxAndTotal(mapperUtil.convert(each, new InvoiceDto()))).collect(Collectors.toList());
//
//    }
//
//    @Override
//    public BigDecimal countTotal(InvoiceType invoiceType) {
//        return listAllByTypeAndCompany(invoiceType)
//                .stream()
//                .filter(invoiceDto -> invoiceDto.getInvoiceStatus().equals(InvoiceStatus.APPROVED))
//                .map(InvoiceDto::getTotal)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//    }
//
//    @Override
//    public BigDecimal sumProfitLoss() {
//        return invoiceRepository.findApprovedSalesInvoices(1L)
//                .stream()
//                .map(invoice -> invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(invoice.getId())
//                        .stream()
//                        .map(InvoiceProductDto::getProfitLoss).reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//    }
//
//
    @Override
    public InvoiceDto setPriceTaxAndTotal(InvoiceDto invoiceDto) {
        List<InvoiceProductDto> invoiceProductDtoList = invoiceProductService.listAllByInvoiceId(invoiceDto.getId());
        BigDecimal totalPrice = invoiceProductDtoList.stream().map(invoiceProductService::getInvoiceProductTotalWithoutTax).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalWithTax = invoiceProductDtoList.stream().map(invoiceProductService::getInvoiceProductTotalWithTax).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalTax = totalWithTax.subtract(totalPrice);
        invoiceDto.setPrice(totalPrice);
        invoiceDto.setTax(totalTax);
        invoiceDto.setTotal(totalWithTax);
        return invoiceDto;
    }

//
//    @Override
//    public List<InvoiceDto> findByInvoiceTypeAndStatus(InvoiceType type, InvoiceStatus status) {
//        String companyTitle = companyService.getCompanyDtoByLoggedInUser().getTitle();
//        return invoiceRepository.findByInvoiceTypeAndInvoiceStatusAndCompanyTitleOrderByDateAsc(type, status, companyTitle).stream()
//                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDto()))
//                .toList();
//    }
//
    @Override
    public InvoiceDto printInvoice(Long id) {
        InvoiceDto dto = findById(id);
        setPriceTaxAndTotal(dto);
        return dto;
    }

}
