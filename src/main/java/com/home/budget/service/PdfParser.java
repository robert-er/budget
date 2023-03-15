package com.home.budget.service;

import java.io.File;

public interface PdfParser {

    void printFile(File file);
    int parsePdf(File file);
}
