package com.cydeo.controller;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.common.response.ResponseWrapper;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchaseInvoice")
@RequiredArgsConstructor
@Tag(description = "Purchase Invoice Controller", name = "Purchase Invoice API")
public class PurchaseInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    @GetMapping("/list")
    @Operation(summary = "List all purchase invoices")
    public ResponseEntity<ResponseWrapper> listPurchaseInvoices(){
        List<InvoiceDto> invoices = invoiceService.listAllByType(InvoiceType.PURCHASE);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Purchase invoice list is successfully retrieved.")
                .data(invoices).build());
    }

    @GetMapping("/print/{id}")
    @Operation(summary = "Print purchase invoice")
    public ResponseEntity<ResponseWrapper> printPurchaseInvoice(@PathVariable Long id){
        InvoiceDto invoiceDto = invoiceService.printInvoice(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Purchase invoice is successfully retrieved.")
                .data(invoiceDto).build());
    }

    @PostMapping
    @Operation(summary = "Create purchase invoice")
    public ResponseEntity<ResponseWrapper> createPurchaseInvoice(@RequestBody InvoiceDto invoice){
        InvoiceDto invoiceDto = invoiceService.save(invoice, InvoiceType.PURCHASE);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("Purchase invoice is successfully created.")
                .data(invoiceDto).build());
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update purchase invoice")
    public ResponseEntity<ResponseWrapper> updatePurchaseInvoice(@PathVariable Long id, @RequestBody InvoiceDto invoice){
        InvoiceDto updatedInvoice = invoiceService.update(id, invoice);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Purchase invoice is successfully updated.")
                .data(updatedInvoice).build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete purchase invoice")
    public ResponseEntity<ResponseWrapper> deletePurchaseInvoice(@PathVariable Long id){
        invoiceService.delete(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Purchase invoice is successfully deleted.").build());
    }

    @PostMapping("/add/invoiceProduct/{id}")
    @Operation(summary = "Add product to purchase invoice")
    public ResponseEntity<ResponseWrapper> addInvoiceProductToPurchaseInvoice(@PathVariable Long id, @RequestBody InvoiceProductDto invoiceProduct){
        InvoiceDto invoice = invoiceService.findById(id);
        invoiceProduct.setInvoice(invoice);
        InvoiceProductDto saved = invoiceProductService.save(invoiceProduct);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Product is successfully added to invoice.")
                .data(saved).build());
    }

    @DeleteMapping("/remove/invoiceProduct/{id}")
    @Operation(summary = "Remove product from purchase invoice")
    public ResponseEntity<ResponseWrapper> removeInvoiceProductFromPurchaseInvoice(@PathVariable Long id){
        invoiceProductService.delete(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Product is successfully deleted from invoice.").build());
    }

    @ExecutionTime
    @GetMapping("/approve/{id}")
    @Operation(summary = "Approve purchase invoice")
    public ResponseEntity<ResponseWrapper> approvePurchaseInvoice(@PathVariable Long id){
        invoiceService.approve(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Purchase invoice is successfully approved.").build());
    }

}
