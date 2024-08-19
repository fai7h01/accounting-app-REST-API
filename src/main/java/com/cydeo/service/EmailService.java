package com.cydeo.service;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.itextpdf.text.DocumentException;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.util.List;

public interface EmailService {

    void sendEmailWithAttachment(String to, String subject, String text, byte[] pdfAttachment) throws MessagingException;

    byte[] generatePdfFromDto(InvoiceDto invoice, List<InvoiceProductDto> invoiceProducts) throws FileNotFoundException, DocumentException;
}
