package org.sentinel.tests.utils.insights;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.sentinel.tests.utils.log.LoggerUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFReport {

    private static String logFilePath = "app.log"; // Change this to your log file path
    private static String pdfFilePath = "reports/pdf-report";
    private static String pdfFileName = "reports/pdf-report/PDFReport.pdf";

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

    private static void createPDFReport(String logFilePath, String pdfFilePath) throws Exception {
        Document document = new Document(PageSize.A3);
        PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
        document.open();

        // Title with color
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
        Paragraph title = new Paragraph("PDF Report\n\n", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Generate timestamp
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Font timeFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Paragraph timeParagraph = new Paragraph("Report generated at: " + timeStamp + "\n\n", timeFont);
        timeParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(timeParagraph);

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

    private static void processLogLine(PdfPTable table, String logLine) {
        // Example log format: "2025-04-02 10:15:30 INFO Some log message"
        String[] parts = logLine.split(" ", 4);
        if (parts.length < 4) return; // Skip invalid lines

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

    public static void main(String[] args) {
        generatePDF();
    }
}