/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils.insights;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sentinel.tests.utils.log.LoggerUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A utility class for sending automated test reports via email.
 * This class handles the creation and sending of emails containing test
 * execution summaries
 * and attaches various report files (Allure, PDF, Excel).
 * 
 * The email configuration is done through environment variables:
 * - SMTP_HOST: SMTP server host
 * - SMTP_USERNAME: Email username/sender address
 * - SMTP_PASSWORD: Email password
 * - SMTP_RECIPIENT: Recipient email address
 * 
 * The class reads test execution summaries from an Excel file and formats them
 * into
 * an HTML table in the email body. It also handles the attachment of multiple
 * report
 * files generated during test execution.
 *
 * Features:
 * - HTML formatted email content with test execution summary
 * - Attachment of Allure, PDF, and Excel reports
 * - Color-coded test results in the summary table
 * - Configurable SMTP settings through environment variables
 * - Logging of email sending status and errors
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public class EmailSender {
    private static final String HOST = System.getenv("SMTP_HOST");
    private static final String USERNAME = System.getenv("SMTP_USERNAME");
    private static final String PASSWORD = System.getenv("SMTP_PASSWORD");
    private static final String RECIPIENT = System.getenv("SMTP_RECIPIENT");

    /**
     * Sends an automated email containing test execution reports and summary.
     * The email includes the following components:
     * - Test execution summary read from Excel file
     * - HTML formatted email body
     * - Attachments:
     * - Allure Report (index.html)
     * - PDF Report
     * - Excel Report (TestSummary.xlsx)
     * 
     * The email is sent using SMTP with TLS encryption and authentication.
     * Email configuration is handled through predefined constants:
     * HOST, USERNAME, PASSWORD, and RECIPIENT.
     */
    public static void sendEmailWithReport() {
        // Read Summary from Excel
        String summaryContent = readSummaryFromExcel("reports/excel-report/TestSummary.xlsx");

        // SMTP Configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", "587"); // TLS

        // Create session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(RECIPIENT));
            message.setSubject("Automated Test Reports");

            // Email Body (HTML Format)
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            String emailContent = "<html><body>"
                    + "<h3>Test Execution Summary:</h3>"
                    + summaryContent
                    + "<p>Please find the attached test reports.</p>"
                    + "<p>Best Regards.</p>" // Fixed incorrect <br> tag
                    + "<span><strong style='margin-top: 1px;'><em>Sentinel Tests</em></strong></span>"
                    + "</body></html>";
            messageBodyPart.setContent(emailContent, "text/html");

            // Attachments
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Attach Allure Report (index.html)
            attachFile(multipart, "allure-report/index.html");

            // Attach PDF Report
            attachFile(multipart, "reports/pdf-report/PDFReport.pdf");

            // Attach Excel Report
            attachFile(multipart, "reports/excel-report/TestSummary.xlsx");

            // Set email content
            message.setContent(multipart);

            // Send email
            Transport.send(message);
            LoggerUtil.info("Email sent successfully with Allure, PDF, and Excel reports!");

        } catch (MessagingException e) {
            LoggerUtil.error("Failed to send email: " + e.getMessage());
        }
    }

    /**
     * Attaches a file to the given multipart message.
     * 
     * @param multipart The Multipart object to attach the file to
     * @param filePath  The full path of the file to be attached
     * 
     * @throws MessagingException if there is an error in the messaging protocol
     * @throws IOException        if there is an error accessing the file
     * 
     *                            The method will:
     *                            - Check if file exists at the given path
     *                            - Create a MimeBodyPart for the attachment
     *                            - Attach the file to the multipart message
     *                            - Log success or failure of the operation
     * 
     *                            If the file is not found at the specified path, a
     *                            warning will be logged
     *                            and the method will return without attaching
     *                            anything.
     */
    private static void attachFile(Multipart multipart, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            LoggerUtil.warning("Attachment file not found: " + filePath);
            return;
        }

        try {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(file);
            multipart.addBodyPart(attachmentPart);
            LoggerUtil.info("Attached: " + filePath);
        } catch (IOException | MessagingException e) {
            LoggerUtil.error("Error attaching file: " + filePath + " - " + e.getMessage());
        }
    }

    /**
     * Reads test execution summary from an Excel file and converts it into an HTML
     * table format.
     * 
     * The method reads data from a "Summary" sheet and creates an HTML table with
     * the following columns:
     * - Package Name
     * - Total Tests
     * - Passed
     * - Failed
     * - Pass %
     *
     * The table includes conditional formatting:
     * - Failed count: Green background for 0, Red background for > 0
     * - Pass percentage: Green background for >= 80%, Orange background for < 80%
     *
     * @param filePath The path to the Excel file containing the test summary
     * @return An HTML formatted string containing the summary table, or an error
     *         message if:
     *         - The file doesn't exist ("
     *         <p>
     *         <b>Summary not available.</b>
     *         </p>
     *         ")
     *         - The Summary sheet is missing ("
     *         <p>
     *         <b>Summary sheet not found.</b>
     *         </p>
     *         ")
     *         - There's an error reading the file ("
     *         <p>
     *         <b>Error reading Summary sheet.</b>
     *         </p>
     *         ")
     * @throws IOException If there's an error accessing or reading the Excel file
     */
    private static String readSummaryFromExcel(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            LoggerUtil.warning("Excel file not found: " + filePath);
            return "<p><b>Summary not available.</b></p>";
        }

        StringBuilder summaryContent = new StringBuilder();
        summaryContent.append("<table border='1' style='border-collapse:collapse;width:100%;text-align:center;'>");
        summaryContent.append("<tr style='background-color:#343a40;color:white;font-weight:bold;'>");
        summaryContent
                .append("<th>Package Name</th><th>Total Tests</th><th>Passed</th><th>Failed</th><th>Pass %</th></tr>");

        try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Summary");
            if (sheet == null) {
                LoggerUtil.warning("Summary sheet not found in Excel.");
                return "<p><b>Summary sheet not found.</b></p>";
            }

            boolean isHeader = true; // Flag to skip the header row

            for (Row row : sheet) {
                if (isHeader) {
                    isHeader = false; // Skip header row
                    continue;
                }

                summaryContent.append("<tr>");
                int colIndex = 0;
                for (Cell cell : row) {
                    String cellValue = cell.toString().trim();
                    String style = "";

                    // Apply styles based on column index
                    if (colIndex == 3 && cell.getCellType() == CellType.NUMERIC && cell.getNumericCellValue() == 0) {
                        style = "background-color:#d4edda;color:#155724;"; // Green for 0 Failed
                    } else if (colIndex == 3 && cell.getCellType() == CellType.NUMERIC
                            && cell.getNumericCellValue() > 0) {
                        style = "background-color:#f8d7da;color:#721c24;"; // Red for Failed > 0
                    } else if (colIndex == 4) {
                        try {
                            double passPercent = Double.parseDouble(cellValue.replace("%", ""));
                            if (passPercent >= 80) {
                                style = "background-color:#d4edda;color:#155724;"; // Green for >= 80%
                            } else {
                                style = "background-color:#fff3cd;color:#856404;"; // Orange for < 80%
                            }
                        } catch (NumberFormatException e) {
                            style = ""; // In case of invalid value, no styling
                        }
                    }

                    summaryContent.append("<td style='" + style + "'>" + cellValue + "</td>");
                    colIndex++;
                }
                summaryContent.append("</tr>");
            }

        } catch (IOException e) {
            LoggerUtil.error("Error reading Summary sheet: " + e.getMessage());
            return "<p><b>Error reading Summary sheet.</b></p>";
        }

        summaryContent.append("</table>");
        return summaryContent.toString();
    }

    public static void main(String[] args) {
        LoggerUtil.info("EmailSender started.....");
        sendEmailWithReport();
    }
}
