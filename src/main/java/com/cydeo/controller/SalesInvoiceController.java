package com.cydeo.controller;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salesInvoice")
@RequiredArgsConstructor
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper> listSalesInvoices(){
        List<InvoiceDto> invoices = invoiceService.listAllByType(InvoiceType.SALES);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Sales invoice list is successfully retrieved.")
                .data(invoices).build());
    }

    @GetMapping("/print/{id}")
    public ResponseEntity<ResponseWrapper> printSalesInvoice(@PathVariable Long id){
        InvoiceDto invoiceDto = invoiceService.printInvoice(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Purchase invoice is successfully retrieved.")
                .data(invoiceDto).build());
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createSalesInvoice(@RequestBody InvoiceDto invoice){
        InvoiceDto invoiceDto = invoiceService.save(invoice, InvoiceType.SALES);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("Sales invoice is successfully created.")
                .data(invoiceDto).build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseWrapper> updateSalesInvoice(@PathVariable Long id, @RequestBody InvoiceDto invoice){
        InvoiceDto updatedInvoice = invoiceService.update(id, invoice);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Sales invoice is successfully updated.")
                .data(updatedInvoice).build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper> deleteSalesInvoice(@PathVariable Long id){
        invoiceService.delete(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Sales invoice is successfully deleted.").build());
    }

    @PostMapping("/add/invoiceProduct/{id}")
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
    public ResponseEntity<ResponseWrapper> removeInvoiceProductFromSalesInvoice(@PathVariable Long id){
        invoiceProductService.delete(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Product is successfully deleted from invoice.").build());
    }

    @ExecutionTime
    @GetMapping("/approve/{id}")
    public ResponseEntity<ResponseWrapper> approveSalesInvoice(@PathVariable Long id){
        invoiceService.approve(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Purchase invoice is successfully approved.").build());
    }

}
