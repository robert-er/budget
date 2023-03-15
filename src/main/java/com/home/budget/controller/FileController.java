package com.home.budget.controller;

import com.home.budget.service.PdfParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("file")
public class FileController {

    private final PdfParser pdfParser;

    private static final Logger log = LogManager.getLogger(FileController.class);

    @Autowired
    public FileController(PdfParser pdfParser) {
        this.pdfParser = pdfParser;
    }

    @GetMapping
    public ResponseEntity<String> download(@RequestParam("filepath") String filepath) {

        File file = new File(filepath);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");


        pdfParser.parsePdf(file);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.getAbsolutePath().length())
                .contentType(MediaType.TEXT_PLAIN)
                .body(file.getAbsolutePath());
    }


}
