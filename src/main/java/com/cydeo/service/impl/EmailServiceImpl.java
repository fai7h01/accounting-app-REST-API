package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.service.EmailService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void sendEmailWithAttachment(String to, String subject, String text, byte[] pdfAttachment) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment("invoice.pdf", new ByteArrayDataSource(pdfAttachment, "application/pdf"));
    }

    @Override
    public byte[] generatePdfFromDto(InvoiceDto invoice, List<InvoiceProductDto> invoiceProducts) throws DocumentException, FileNotFoundException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, os);

        document.open();
        PdfPTable table = new PdfPTable(6);
        addTableHeader(table);
        addRows(table, invoiceProducts);
        document.add(table);

        document.close();
        return new ByteArrayOutputStream().toByteArray();
    }

    private void addRows(PdfPTable table, List<InvoiceProductDto> invoiceProducts) {
        for (InvoiceProductDto each : invoiceProducts) {
            table.addCell(each.getInvoice().getId().toString());
            table.addCell(each.getProduct().getName());
            table.addCell(each.getPrice().toString());
            table.addCell(each.getQuantity().toString());
            table.addCell(each.getTax().toString());
            table.addCell(each.getTotal().toString());
        }
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("#", "Product", "Price", "Quantity", "Tax", "Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

}
