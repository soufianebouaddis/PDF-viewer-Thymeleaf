package com.pdf_viewer.demo.controller;


import com.pdf_viewer.demo.entity.PDF;
import com.pdf_viewer.demo.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


@Controller
public class PDFController {

    private final PDFService pdfService;

    @Autowired
    public PDFController(PDFService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/")
    public String uploadPage() {
        return "upload.html";
    }

    @PostMapping("/upload")
    public String handlePdfUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            PDF pdfFile = pdfService.uploadFile(file);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully");
            return "redirect:/view/" + pdfFile.getId();
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Failed to upload.css file");
            return "redirect:/";
        }
    }

    @GetMapping("/view/{id}")
    public String viewPdf(@PathVariable Long id, Model model) {
        PDF pdfFile = pdfService.findPdfById(id);
        if (pdfFile != null) {
            model.addAttribute("fileName", pdfFile.getFileName());
            return "viewer.html";
        }
        model.addAttribute("message", "File not found");
        return "upload.html";
    }

    @GetMapping("/pdf/{fileName}")
    public ResponseEntity<Resource> servePdf(@PathVariable String fileName) {
        FileSystemResource file = new FileSystemResource(pdfService.getPdfFile(fileName));

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(file);
    }

}
