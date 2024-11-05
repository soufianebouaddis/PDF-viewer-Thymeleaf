package com.pdf_viewer.demo.service.serviceimpl;

import com.pdf_viewer.demo.entity.PDF;
import com.pdf_viewer.demo.repository.PdfRepository;
import com.pdf_viewer.demo.service.PDFService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class PDFImpl implements PDFService{

    private final Path pdfDirectory = Path.of(System.getProperty("java.io.tmpdir"), "uploaded_pdfs");
    private final PdfRepository pdfRepository;

    @Autowired
    public PDFImpl(PdfRepository pdfRepository) throws IOException {
        this.pdfRepository = pdfRepository;
        Files.createDirectories(pdfDirectory);
    }
    @Override
    public File getPdfFile(String fileName) {
        return pdfDirectory.resolve(fileName).toFile();
    }
    @Override
    public PDF findPdfById(Long id) {
        return pdfRepository.findById(id).orElse(null);
    }

    @Override
    public PDF uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path targetLocation = pdfDirectory.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        PDF pdfFile = new PDF(fileName, targetLocation.toString());
        return pdfRepository.save(pdfFile);
    }
}

