package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository repository;
    private final MapperUtil mapperUtil;
    private final InvoiceProductRepository invoiceProductRepository;
    private final ProductService productService;
    private final CompanyService companyService;

    public InvoiceProductServiceImpl(InvoiceProductRepository repository, MapperUtil mapperUtil, InvoiceProductRepository invoiceProductRepository, ProductService productService, CompanyService companyService) {
        this.repository = repository;
        this.mapperUtil = mapperUtil;
        this.invoiceProductRepository = invoiceProductRepository;
        this.productService = productService;
        this.companyService = companyService;
    }


    @Override
    public InvoiceProductDto findById(Long id) {

        InvoiceProduct foundInvoiceProduct = repository.findById(id).orElseThrow(IllegalArgumentException::new);

        return mapperUtil.convert(foundInvoiceProduct, new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> listAllByInvoiceId(Long id) {
        return repository.findAllByInvoiceId(id).stream()
                .map(invoiceProduct -> {
                    InvoiceProductDto invoiceProductDto = mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
                    invoiceProductDto.setTotal(getInvoiceProductTotalWithTax(invoiceProductDto));
                    return invoiceProductDto;
                })
                .toList();
    }

    @Override
    public InvoiceProductDto save(InvoiceProductDto invoiceProductDto) {
        InvoiceProduct entity = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
        return mapperUtil.convert(repository.save(entity), new InvoiceProductDto());
    }

    @Override
    public void deleteById(Long id) {
        InvoiceProduct invoiceProduct = repository.findById(id).orElseThrow(IllegalArgumentException::new);
        invoiceProduct.setIsDeleted(true);
        repository.save(invoiceProduct);
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

    private BigDecimal calculateCost(Long productId, Integer salesQuantity) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        List<InvoiceProduct> approvedInvoiceProducts = invoiceProductRepository.findApprovedPurchaseInvoices(productId, companyId);
        BigDecimal totalCost = BigDecimal.ZERO;

        for (InvoiceProduct each : approvedInvoiceProducts) {
            int remainingQuantity = each.getRemainingQuantity() - salesQuantity;
            if (remainingQuantity <= 0) {
                BigDecimal costWithoutTax = each.getPrice().multiply(BigDecimal.valueOf(each.getRemainingQuantity()));
                BigDecimal tax = costWithoutTax.multiply(BigDecimal.valueOf(each.getTax())).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal costWithTax = costWithoutTax.add(tax);
                salesQuantity -= each.getRemainingQuantity();
                each.setRemainingQuantity(0);
                totalCost = totalCost.add(costWithTax);
                invoiceProductRepository.save(each);
                if (remainingQuantity == 0) break;
            } else {
                BigDecimal costWithoutTax = each.getPrice().multiply(BigDecimal.valueOf(each.getRemainingQuantity()));
                BigDecimal tax = costWithoutTax.multiply(BigDecimal.valueOf(each.getTax())).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal costWithTax = costWithoutTax.add(tax);
                each.setRemainingQuantity(remainingQuantity);
                totalCost = totalCost.add(costWithTax);
                invoiceProductRepository.save(each);
                break;
            }
        }
        return totalCost;
    }

    @Override
    public List<InvoiceProductDto> findAllByInvoiceIdAndCalculateTotalPrice(Long invoiceId) {
        return repository.findAllByInvoiceId(invoiceId).stream()
                .map(invoiceProduct -> {
                    InvoiceProductDto dto = mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
                    dto.setTotal(getInvoiceProductTotalWithTax(dto));
                    return dto;
                }).toList();
    }


}
