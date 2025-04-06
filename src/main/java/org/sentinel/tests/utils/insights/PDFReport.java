/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils.insights;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.sentinel.tests.utils.log.LoggerUtil;
import org.sentinel.tests.utils.testng.ReadTestNG;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Generates a PDF report from a log file.
 * The report includes a title, timestamp, and a table with log entries.
 * Each log entry is color-coded based on its severity level.
 *
 * <p>
 * Log levels are color-coded as follows:
 * <ul>
 * <li>INFO - Green
 * <li>WARNING - Orange
 * <li>ERROR - Red
 * <li>SEVERE - Red
 * </ul>
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public class PDFReport {

    private static String logFilePath = "app.log"; // Change this to your log file path
    private static String pdfFilePath = "reports/pdf-report";
    private static String pdfFileName = "reports/pdf-report/PDFReport.pdf";

    public static void main(String[] args) {
        generatePDF();
    }

    /**
     * Generates a PDF report from the specified log file.
     * The report includes a title, timestamp, and a table with log entries.
     * Each log entry is color-coded based on its severity level.
     *
     * <p>
     * Log levels are color-coded as follows:
     * <ul>
     * <li>INFO - Green
     * <li>WARNING - Orange
     * <li>ERROR - Red
     * <li>SEVERE - Red
     * </ul>
     */
    public static void generatePDF() {
        try {
            LoggerUtil.info("PDF Report Generate Started.....");

            File directory = new File(pdfFilePath);
            if (!directory.exists()) {
                directory.mkdirs(); // Ensure directory exists
            }

            createPDFReport(logFilePath, pdfFileName);
            LoggerUtil.info("PDF Report Generated.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a PDF report from the specified log file.
     * The report includes a title, timestamp, and a table with log entries.
     * Each log entry is color-coded based on its severity level.
     *
     * @param logFilePath Path to the log file
     * @param pdfFilePath Path to save the generated PDF report
     * @throws Exception if an error occurs during PDF generation
     */
    private static void createPDFReport(String logFilePath, String pdfFilePath) throws Exception {
        Document document = new Document(PageSize.A3);
        PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
        document.open();

        // Generate timestamp
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Font timeFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Paragraph timeParagraph = new Paragraph("Report Generated On: " + timeStamp + "\n\n", timeFont);
        timeParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(timeParagraph);

        // Title with color
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
        Paragraph title = new Paragraph("Execution Details\n\n", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        PdfPTable envTable = addEnvTable();
        document.add(envTable);

        // Table setup
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 1, 6});

        // Add headers
        addTableHeader(table, "Timestamp", "Level", "Log Message");

        // Read and process log file
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLogLine(table, line);
            }
        }

        document.add(table);
        document.close();
    }

    /**
     * Adds a header row to the PDF table.
     *
     * @param table The PDF table to which the header will be added
     * @param col1  Header text for the first column
     * @param col2  Header text for the second column
     * @param col3  Header text for the third column
     */
    private static void addTableHeader(PdfPTable table, String col1, String col2, String col3) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        BaseColor headerColor = BaseColor.DARK_GRAY;

        PdfPCell header1 = new PdfPCell(new Phrase(col1, headerFont));
        PdfPCell header2 = new PdfPCell(new Phrase(col2, headerFont));
        PdfPCell header3 = new PdfPCell(new Phrase(col3, headerFont));

        header1.setBackgroundColor(headerColor);
        header2.setBackgroundColor(headerColor);
        header3.setBackgroundColor(headerColor);

        table.addCell(header1);
        table.addCell(header2);
        table.addCell(header3);
    }

    /**
     * Processes a single log line and adds it to the PDF table.
     *
     * @param table   The PDF table to which the log entry will be added
     * @param logLine The log line to process
     */
    private static void processLogLine(PdfPTable table, String logLine) {
        // Example log format: "2025-04-02 10:15:30 INFO Some log message"
        String[] parts = logLine.split(" ", 4);
        if (parts.length < 4)
            return; // Skip invalid lines

        String timestamp = parts[0] + " " + parts[1];
        String level = parts[2];
        String message = parts[3];

        BaseColor levelColor = getLogLevelColor(level);

        PdfPCell cellTimestamp = new PdfPCell(new Phrase(timestamp));
        PdfPCell cellLevel = new PdfPCell(new Phrase(level));
        cellLevel.setBackgroundColor(levelColor);
        PdfPCell cellMessage = new PdfPCell(new Phrase(message));

        table.addCell(cellTimestamp);
        table.addCell(cellLevel);
        table.addCell(cellMessage);
    }

    /**
     * Returns the color associated with a specific log level.
     *
     * @param level The log level (e.g., INFO, WARNING, ERROR, SEVERE)
     * @return BaseColor The color associated with the log level
     */
    private static BaseColor getLogLevelColor(String level) {
        switch (level.toUpperCase()) {
            case "[INFO]":
                return BaseColor.GREEN;
            case "[WARNING]":
                return BaseColor.ORANGE;
            case "[ERROR]":
                return BaseColor.RED;
            case "[SEVERE]":
                return BaseColor.RED;
            default:
                return BaseColor.LIGHT_GRAY;
        }
    }

    private static PdfPTable addEnvTable() throws DocumentException {

        // Create a 2-column table
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(40);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Table fonts
        Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Font tableCellFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        // Table header styling
        PdfPCell keyHeader = new PdfPCell(new Phrase("Property", tableHeaderFont));
        PdfPCell valueHeader = new PdfPCell(new Phrase("Value", tableHeaderFont));
        keyHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        valueHeader.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Header background color
        keyHeader.setBackgroundColor(BaseColor.DARK_GRAY);
        valueHeader.setBackgroundColor(BaseColor.DARK_GRAY);
        keyHeader.setPadding(5f);
        valueHeader.setPadding(5f);

        table.addCell(keyHeader);
        table.addCell(valueHeader);

        Properties properties = AllureEnvironmentSetup.getProperties();

        // Add property key-value pairs with alternating row colors
        boolean alternate = false;
        for (String key : properties.stringPropertyNames()) {
            PdfPCell keyCell = new PdfPCell(new Phrase(key, tableCellFont));
            PdfPCell valueCell = new PdfPCell(new Phrase(properties.getProperty(key), tableCellFont));

            keyCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            if (alternate) {
                keyCell.setBackgroundColor(new BaseColor(230, 230, 230)); // Light Gray
                valueCell.setBackgroundColor(new BaseColor(230, 230, 230));
            }

            keyCell.setPadding(5f);
            valueCell.setPadding(5f);

            table.addCell(keyCell);
            table.addCell(valueCell);

            alternate = !alternate; // Toggle for alternating row colors
        }

        // Spacing before and after
        table.setSpacingBefore(1f);
        table.setSpacingAfter(10f);

        return table;
    }

}