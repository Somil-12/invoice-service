package com.example.invoice.util;

import com.example.invoice.model.Dealer;
import com.example.invoice.model.Vehicle;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PdfUtils {

    public static byte[] buildInvoicePdf(
            Dealer dealer,
            Vehicle vehicle,
            String customerName,
            String invoiceNo,
            String transactionId,
            String timestamp,
            double price,
            double tax,
            double total
    ) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Vehicle Sales Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph(" "));

            // Header table: Dealer + Invoice meta
            PdfPTable header = new PdfPTable(2);
            header.setWidthPercentage(100);
            header.setWidths(new float[]{60f, 40f});

            PdfPCell dealerCell = new PdfPCell();
            dealerCell.setBorder(Rectangle.NO_BORDER);
            dealerCell.addElement(new Paragraph(dealer.getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            dealerCell.addElement(new Paragraph(dealer.getAddress()));
            dealerCell.addElement(new Paragraph("Phone: " + dealer.getPhone()));
            header.addCell(dealerCell);

            PdfPCell metaCell = new PdfPCell();
            metaCell.setBorder(Rectangle.NO_BORDER);
            metaCell.addElement(new Paragraph("Invoice #: " + invoiceNo));
            metaCell.addElement(new Paragraph("Timestamp: " + timestamp));
            metaCell.addElement(new Paragraph("Transaction ID: " + transactionId));
            header.addCell(metaCell);

            doc.add(header);
            doc.add(new Paragraph(" "));

            // Customer
            doc.add(new Paragraph("Bill To: " + customerName, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            doc.add(new Paragraph(" "));

            // Vehicle table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{20f, 20f, 20f, 20f, 20f});
            addHeaderCell(table, "Vehicle ID");
            addHeaderCell(table, "Make");
            addHeaderCell(table, "Model");
            addHeaderCell(table, "Year");
            addHeaderCell(table, "Base Price (₹)");

            addBodyCell(table, vehicle.getVehicleId());
            addBodyCell(table, vehicle.getMake());
            addBodyCell(table, vehicle.getModel());
            addBodyCell(table, String.valueOf(vehicle.getYear()));
            addBodyCell(table, String.format("%.2f", price));
            doc.add(table);
            doc.add(new Paragraph(" "));

            // Totals
            PdfPTable totals = new PdfPTable(2);
            totals.setWidthPercentage(40);
            totals.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totals.setWidths(new float[]{60f, 40f});
            addTotalsRow(totals, "Tax (10%)", String.format("₹ %.2f", tax));
            addTotalsRow(totals, "Total", String.format("₹ %.2f", total));
            doc.add(totals);
            doc.add(new Paragraph(" "));

            // QR Code (Bonus)
            Image qr = qrAsITextImage("transactionId=" + transactionId);
            qr.scalePercent(40f);
            qr.setAlignment(Element.ALIGN_RIGHT);
            doc.add(qr);

            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to build PDF", e);
        }
    }

    private static void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private static void addBodyCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private static void addTotalsRow(PdfPTable table, String label, String value) {
        PdfPCell l = new PdfPCell(new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA, 11)));
        PdfPCell v = new PdfPCell(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
        l.setHorizontalAlignment(Element.ALIGN_RIGHT);
        v.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(l);
        table.addCell(v);
    }

    private static Image qrAsITextImage(String contents) throws Exception {
        int size = 256;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());

        var bitMatrix = new QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
        BufferedImage img = com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage(bitMatrix);

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", bos);
            return Image.getInstance(bos.toByteArray());
        }
    }
}
