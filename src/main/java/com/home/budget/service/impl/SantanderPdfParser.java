package com.home.budget.service.impl;

import com.home.budget.model.History;
import com.home.budget.service.HistoryService;
import com.home.budget.service.PdfParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SantanderPdfParser implements PdfParser {

    private final HistoryService historyService;

    private static final Logger log = LogManager.getLogger(SantanderPdfParser.class);

    private static final String SANTANDER_LABEL = "Santander Bank Polska S.A. z siedzibą w Warszawie, " +
            "przy al. Jana Pawła II 17, 00-854 Warszawa";
    private static final String START_LINE = "Data Opis Kwota Saldo po operacji";
    private static final String OPERATION_DATE_LINE = "Data operacji";
    private static final String POSTING_DATE_LINE = "Data księgowania";
    private static final String FROM_ACCOUNT_LABEL = "Z rachunku: ";
    private static final String TO_ACCOUNT_LABEL = "Na rachunek: ";
    private static final String CARD_NUMBER_LABEL = "Numer karty: ";
    private static final String TITLE_LABEL = "Tytuł: ";
    private static final String FINAL_CONCLUSION_LABEL = "Podsumowanie końcowe";
    private static final Pattern AMOUNT_PATTERN = Pattern
            .compile("([+-]?[0-9]{1,3}(?:\\s?\\ ?[0-9]{3})*\\,[0-9]{2})\\sPLN\\s([+-]?[0-9]{1,3}(?:\\s?\\ ?[0-9]{3})*\\,[0-9]{2})\\sPLN");

    public SantanderPdfParser(HistoryService historyService) {
        this.historyService = historyService;
    }

    public boolean isSantanderPdfFile(File file) {

        String[] lines = readPDFLines(file);

        for (String line : lines) {
            if (line.contains(SANTANDER_LABEL)) {
                return true;
            }
        }
        return false;
    }

    public void printFile(File file) {
        String[] fileLines = readPDFLines(file);

        log.info("Read file [{}]", file);

        for (String line : fileLines) {
            log.info(line);
        }

        log.info("Is Santander file? [{}]", isSantanderPdfFile(file));
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

    private int findStartLine(File file) {
        String[] lines = readPDFLines(file);

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].equals(START_LINE)) {
                return i;
            }
        }
        return -1;
    }

    public int parsePdf(File file) {
        String[] lines = readPDFLines(file);
        int startLine = findStartLine(file);

        Date operationDate = null;
        Date postingDate = null;
        String description = null;
        String fromAccount = null;
        String toAccount = null;
        String cardNumber = null;
        String title = null;
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal balanceAfter = BigDecimal.ZERO;

        int counter = 0;
        for (int i = startLine; i < lines.length; i++) {

            if (lines[i - 1].equals(OPERATION_DATE_LINE)) {
                operationDate = Date.valueOf(lines[i]);
            }

            if (lines[i].equals(POSTING_DATE_LINE)) {
                postingDate = Date.valueOf(lines[i + 1]);
                description = lines[i + 2];
            }

            if (lines[i].startsWith(FROM_ACCOUNT_LABEL)) {
                fromAccount = lines[i].replace(FROM_ACCOUNT_LABEL, "");
            }

            if (lines[i].startsWith(TO_ACCOUNT_LABEL)) {
                toAccount = lines[i].replace(TO_ACCOUNT_LABEL, "");
            }

            if (lines[i].startsWith(CARD_NUMBER_LABEL)) {
                cardNumber = lines[i].replace(CARD_NUMBER_LABEL, "");
            }

            if (lines[i].startsWith(TITLE_LABEL)) {
                title = parseTitle(lines, i);
            }

            Matcher matcher = AMOUNT_PATTERN.matcher(lines[i]);
            if (matcher.find()) {
                amount = new BigDecimal(matcher.group(1)
                        .replace(" ", "")
                        .replace(" ", "")
                        .replace(",", "."));
                balanceAfter = new BigDecimal(matcher.group(2)
                        .replace(" ", "")
                        .replace(" ", "")
                        .replace(",", "."));
            }

            if ((lines[i].equals(OPERATION_DATE_LINE) || lines[i].contains(FINAL_CONCLUSION_LABEL)) &&
                    operationDate != null && postingDate != null) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                log.info("           \n" +
                                "Date operationDate=[{}]\n" +
                                "            Date postingDate=[{}]\n" +
                                "            String description=[{}]\n" +
                                "            String fromAccount=[{}]\n" +
                                "            String toAccount=[{}]\n" +
                                "            String cardNumber=[{}]\n" +
                                "            String title=[{}]\n" +
                                "            BigDecimal amount=[{}]\n" +
                                "            BigDecimal balanceAfter=[{}]",
                        simpleDateFormat.format(operationDate),
                        simpleDateFormat.format(postingDate),
                        description,
                        fromAccount,
                        toAccount,
                        cardNumber,
                        title,
                        amount,
                        balanceAfter);

                History recentHistory = historyService.add(History.builder()
                        .operationDate(operationDate)
                        .postingDate(postingDate)
                        .description(description)
                        .fromAccount(fromAccount)
                        .toAccount(toAccount)
                        .cardNumber(cardNumber)
                        .title(title)
                        .amount(amount)
                        .balanceAfter(balanceAfter)
                        .build());
                counter++;

                log.info("Cnt [{}] Saved to H2 history [{}]", counter, recentHistory);

            operationDate = null;
            postingDate = null;
            description = null;
            fromAccount = null;
            toAccount = null;
            cardNumber = null;
            title = null;
            amount = BigDecimal.ZERO;
            balanceAfter = BigDecimal.ZERO;
            }
        }
        log.info("Number of parsed entries [{}]", counter);
        return counter;
    }

    private String parseTitle(String[] lines, int startLine) {
        String title = lines[startLine].replace(TITLE_LABEL, "");
        int endLine = startLine;

        for (int i = startLine; i < lines.length; i++) {
            Matcher matcher = AMOUNT_PATTERN.matcher(lines[i]);
            if (matcher.find()) {
                endLine = i;
                break;
            }
        }

        for (int j = startLine + 1; j < endLine; j++) {
            title = title.concat(lines[j]);
        }
        return title;
    }
}
