package com.pdf_viewer.demo.service;

import com.pdf_viewer.demo.entity.PDF;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface PDFService {
    public PDF uploadFile(MultipartFile file) throws IOException;
    public File getPdfFile(String fileName);
    public PDF findPdfById(Long id);
}
