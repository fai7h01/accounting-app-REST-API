package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.common.ResponseWrapper;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchaseInvoice")
@RequiredArgsConstructor
public class PurchaseController {

    private final InvoiceService invoiceService;

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper> listPurchaseInvoices(){
        List<InvoiceDto> invoices = invoiceService.listAllByType(InvoiceType.PURCHASE);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Purchase invoice list is successfully retrieved.")
                .data(invoices).build());
    }

    @GetMapping("/print/{id}")
    public ResponseEntity<ResponseWrapper> printPurchaseInvoice(@PathVariable Long id){
        InvoiceDto invoiceDto = invoiceService.printInvoice(id);
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Purchase invoice is retrieved successfully.")
                .data(invoiceDto).build());
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createPurchaseInvoice(@RequestBody InvoiceDto invoice){
        InvoiceDto invoiceDto = invoiceService.save(invoice, InvoiceType.PURCHASE);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("Purchase invoice is successfully created.")
                .data(invoiceDto).build());

    }


}
