package com.home.budget.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private static final org.apache.logging.log4j.Logger log =
            org.apache.logging.log4j.LogManager.getLogger(FileController.class);

    @GetMapping
    public ResponseEntity<String> download(@RequestParam("filepath") String filepath) {

        File file = new File(filepath);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        printFile(file);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.getAbsolutePath().length())
                .contentType(MediaType.TEXT_PLAIN)
                .body(file.getAbsolutePath());
    }

    private void printFile(File file) {
        String[] fileLines = readPDFLines(file);

        for (String line : fileLines) {
            log.info(line);
        }
    }

    private static String[] readPDFLines(File file) {

        String[] lines = new String[0];
        try (PDDocument document = PDDocument.load(file)) {
            document.getClass();

            if (!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);

                // split by whitespace
                lines = pdfFileInText.split("\\r?\\n");
                document.close();
            }
        } catch (IOException e) {
            log.error("Unable to open file [{}]", file, e);
        }
        return lines;
    }
}
