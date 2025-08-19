package com.example.invoice.controller;

import com.example.invoice.dto.InvoiceRequest;
import com.example.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    /**
     * POST /api/invoices
     * Body: { "dealerId":"D001", "vehicleId":"V1002", "customerName":"John Doe" }
     * Response: PDF file (download)
     */
    @PostMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<ByteArrayResource> generate(@Valid @RequestBody InvoiceRequest request) {
        var generated = service.generate(request);

        var resource = new ByteArrayResource(generated.getPdfBytes());
        String filename = generated.getInvoiceNo() + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

        // Helpful metadata headers (optional)
        headers.add("X-Invoice-Number", generated.getInvoiceNo());
        headers.add("X-Transaction-Id", generated.getTransactionId());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
