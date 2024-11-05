package com.pdf_viewer.demo.repository;

import com.pdf_viewer.demo.entity.PDF;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PdfRepository extends JpaRepository<PDF,Long> {
}
