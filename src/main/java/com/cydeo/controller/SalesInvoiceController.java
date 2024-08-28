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
@RequestMapping("/api/v1/salesInvoice")
@RequiredArgsConstructor
@Tag(description = "Sales Invoice Controller", name = "Sales Invoice API")
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    @GetMapping("/list")
    @Operation(summary = "List all sales invoices")
    public ResponseEntity<ResponseWrapper> listSalesInvoices(){
        List<InvoiceDto> invoices = invoiceService.listAllByType(InvoiceType.SALES);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Sales invoice list is successfully retrieved.")
                .data(invoices).build());
    }

    @GetMapping("/print/{id}")
    @Operation(summary = "Print sales invoice")
    public ResponseEntity<ResponseWrapper> printSalesInvoice(@PathVariable Long id){
        InvoiceDto invoiceDto = invoiceService.printInvoice(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Purchase invoice is successfully retrieved.")
                .data(invoiceDto).build());
    }

    @PostMapping
    @Operation(summary = "Create sales invoice")
    public ResponseEntity<ResponseWrapper> createSalesInvoice(@RequestBody InvoiceDto invoice){
        InvoiceDto invoiceDto = invoiceService.save(invoice, InvoiceType.SALES);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("Sales invoice is successfully created.")
                .data(invoiceDto).build());
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update sales invoice")
    public ResponseEntity<ResponseWrapper> updateSalesInvoice(@PathVariable Long id, @RequestBody InvoiceDto invoice){
        InvoiceDto updatedInvoice = invoiceService.update(id, invoice);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Sales invoice is successfully updated.")
                .data(updatedInvoice).build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete sales invoice")
    public ResponseEntity<ResponseWrapper> deleteSalesInvoice(@PathVariable Long id){
        invoiceService.delete(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Sales invoice is successfully deleted.").build());
    }

    @PostMapping("/add/invoiceProduct/{id}")
    @Operation(summary = "Add product to sales invoice")
    public ResponseEntity<ResponseWrapper> addInvoiceProductToSalesInvoice(@PathVariable Long id, @RequestBody InvoiceProductDto invoiceProduct){
        InvoiceDto invoice = invoiceService.findById(id);
        invoiceProduct.setInvoice(invoice);
        InvoiceProductDto saved = invoiceProductService.save(invoiceProduct);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Product is successfully added to invoice.")
                .data(saved).build());
    }

    @DeleteMapping("/remove/invoiceProduct/{id}")
    @Operation(summary = "Remove product from sales invoice")
    public ResponseEntity<ResponseWrapper> removeInvoiceProductFromSalesInvoice(@PathVariable Long id){
        invoiceProductService.delete(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Product is successfully deleted from invoice.").build());
    }

    @ExecutionTime
    @GetMapping("/approve/{id}")
    @Operation(summary = "Approve sales invoice")
    public ResponseEntity<ResponseWrapper> approveSalesInvoice(@PathVariable Long id){
        invoiceService.approve(id);
        invoiceProductService.lowQuantityAlert(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Purchase invoice is successfully approved.").build());
    }

}
