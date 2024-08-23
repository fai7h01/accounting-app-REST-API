package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.exception.ProductLowLimitAlertException;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;
    private final ProductService productService;
    private final CompanyService companyService;

    public InvoiceProductServiceImpl(InvoiceProductRepository repository, MapperUtil mapperUtil, ProductService productService, CompanyService companyService) {
        this.invoiceProductRepository = repository;
        this.mapperUtil = mapperUtil;
        this.productService = productService;
        this.companyService = companyService;
    }


    @Override
    public InvoiceProductDto findById(Long id) {
        InvoiceProduct foundInvoiceProduct = invoiceProductRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Invoice Product not found."));
        return mapperUtil.convert(foundInvoiceProduct, new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> listAllByInvoiceId(Long id) {
        return invoiceProductRepository.findAllByInvoiceId(id).stream()
                .map(invoiceProduct -> {
                    InvoiceProductDto invoiceProductDto = mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
                    invoiceProductDto.setTotal(getInvoiceProductTotalWithTax(invoiceProductDto));
                    return invoiceProductDto;
                })
                .toList();
    }

    @Override
    public InvoiceProductDto save(InvoiceProductDto invoiceProductDto) {
        ProductDto product = productService.findByNameInCompany(invoiceProductDto.getProduct().getName());
        invoiceProductDto.setProduct(product);
        InvoiceProduct saved = invoiceProductRepository.save(mapperUtil.convert(invoiceProductDto, new InvoiceProduct()));
        return mapperUtil.convert(saved, new InvoiceProductDto());
    }

    @Override
    public void delete(Long id) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Invoice Product not found."));
        invoiceProduct.setIsDeleted(true);
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public BigDecimal getInvoiceProductTotalWithTax(InvoiceProductDto invoiceProductDto) {
        BigDecimal totalWithoutTax = getInvoiceProductTotalWithoutTax(invoiceProductDto);
        BigDecimal tax = (totalWithoutTax.multiply(BigDecimal.valueOf(invoiceProductDto.getTax()))).divide(BigDecimal.valueOf(100), RoundingMode.DOWN);
        return totalWithoutTax.add(tax);
    }

    @Override
    public BigDecimal getInvoiceProductTotalWithoutTax(InvoiceProductDto invoiceProductDto) {
        return invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity()));
    }

    @Override
    public void updateQuantityInStockForSale(Long id) {
        List<Product> products = invoiceProductRepository.findProductsByInvoiceId(id);
        for (Product product : products) {
            int stock = product.getQuantityInStock() - invoiceProductRepository.sumQuantityOfProducts(id, product.getId());
            if (stock < 0) {
                throw new NoSuchElementException("Stock for " + product.getName() + " is not enough to approve invoice.");
            }
            product.setQuantityInStock(stock);
            productService.save(mapperUtil.convert(product, new ProductDto()));
        }
    }

    @Override
    public void updateQuantityInStockForPurchase(Long id) {
        List<Product> products = invoiceProductRepository.findProductsByInvoiceId(id);
        products.forEach(product -> {
            product.setQuantityInStock(product.getQuantityInStock() + invoiceProductRepository.sumQuantityOfProducts(id, product.getId()));
            productService.save(mapperUtil.convert(product, new ProductDto()));
        });
    }

    @Override
    public void updateRemainingQuantityUponApproval(Long id) {
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findAllByInvoiceId(id);
        invoiceProducts.forEach(invoiceProduct -> {
            invoiceProduct.setRemainingQuantity(invoiceProduct.getQuantity());
            invoiceProductRepository.save(invoiceProduct);
        });
    }

    @Override
    public void calculateProfitLoss(Long id) {
        List<InvoiceProductDto> invoiceProducts = listAllByInvoiceId(id);
        for (InvoiceProductDto each : invoiceProducts) {
            Long productId = each.getProduct().getId();
            BigDecimal profitLoss = getInvoiceProductTotalWithTax(each)
                    .subtract(calculateCost(productId, each.getQuantity()));
            each.setProfitLoss(profitLoss);
            save(each);
        }
    }

    @Override
    public void lowQuantityAlert(Long id) {
        List<Product> products = invoiceProductRepository.findProductsByInvoiceId(id);
        for (Product each : products) {
            int stock = each.getQuantityInStock();
            if (stock < each.getLowLimitAlert()){
                throw new ProductLowLimitAlertException("Stock of " + each.getName() + " decreased below low limit!");
            }
        }
    }

    private BigDecimal calculateCost(Long productId, Integer salesQuantity) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        List<InvoiceProduct> approvedInvoiceProducts = invoiceProductRepository.findApprovedPurchaseInvoices(productId, companyId);
        BigDecimal totalCost = BigDecimal.ZERO;
        for (InvoiceProduct each : approvedInvoiceProducts) {
            int remainingQuantity = each.getRemainingQuantity() - salesQuantity;
            BigDecimal costWithoutTax = each.getPrice().multiply(BigDecimal.valueOf(each.getRemainingQuantity()));
            BigDecimal tax = costWithoutTax.multiply(BigDecimal.valueOf(each.getTax())).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            BigDecimal costWithTax = costWithoutTax.add(tax);
            if (remainingQuantity <= 0){
                salesQuantity -= each.getRemainingQuantity();
                each.setRemainingQuantity(0);
                totalCost = totalCost.add(costWithTax);
                invoiceProductRepository.save(each);
                if (remainingQuantity == 0) break;
            }else{
                each.setRemainingQuantity(remainingQuantity);
                totalCost = totalCost.add(costWithTax);
                invoiceProductRepository.save(each);
                break;
            }
        }
        return totalCost;
    }

    @Override
    public List<InvoiceProductDto> listAllByInvoiceIdAndCalculateTotalPrice(Long invoiceId) {
        return invoiceProductRepository.findAllByInvoiceId(invoiceId).stream()
                .map(invoiceProduct -> {
                    InvoiceProductDto dto = mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
                    dto.setTotal(getInvoiceProductTotalWithTax(dto));
                    return dto;
                }).toList();
    }

    @Override
    public List<InvoiceProductDto> listAllApprovedInvoiceProducts() {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByInvoiceCompanyIdAndInvoiceInvoiceStatusOrderByInvoiceDateDesc(companyId, InvoiceStatus.APPROVED);
        return invoiceProducts.stream()
                .map(invoiceProduct -> {
                    InvoiceProductDto dto = mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
                    dto.setTotal(getInvoiceProductTotalWithTax(dto));
                    return dto;
                })
                .toList();
    }


}
