package com.cydeo.controller;

import com.cydeo.annotation.DefaultExceptionMessage;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.common.response.ResponseWrapper;
import com.cydeo.service.EmailService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
@Slf4j
@Tag(description = "Email Controller", name = "Email API")
public class EmailController {

    private final EmailService emailService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    @DefaultExceptionMessage(defaultMessage = "Something went wrong while sending email.")
    @GetMapping("/send-email/{id}")
    @Operation(summary = "Send email with generated PDF attachment")
    public ResponseEntity<ResponseWrapper> sendEmail(@PathVariable("id") Long invoiceId){
        InvoiceDto invoice = invoiceService.findById(invoiceId);
        List<InvoiceProductDto> invoiceProducts = invoiceProductService.listAllByInvoiceId(invoiceId);

        try {
            byte[] pdfBytes = emailService.generatePdfFromDto(invoice, invoiceProducts);
            emailService.sendEmailWithAttachment(invoice.getClientVendor().getEmail(), "Invoice for " + invoice.getClientVendor().getClientVendorName(),
                    "This invoice has been generated by " + invoice.getCompany().getTitle(), pdfBytes);
        }catch (MessagingException e){
            log.error("Error occurred while sending email: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ResponseWrapper.builder().message("Error occurred while sending email").build());
        }catch (DocumentException | FileNotFoundException e){
            log.error("Error occurred as sending email: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ResponseWrapper.builder().message("Error occurred while sending email").build());
        }
        return ResponseEntity.ok(ResponseWrapper.builder().code(HttpStatus.OK.value())
                .success(true)
                .message("Email is successfully sent.").build());

    }

}
