package org.sentinel.tests.utils.insights;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EmailSender {
 private static final String HOST = "smtp.gmail.com"; // Replace with actual SMTP host
    private static final String USERNAME = "swapnildamate@gmail.com";
    private static final String PASSWORD = "bzdr fspc wdmq qnil";
    private static final String RECIPIENT = "damateswapnil@gmail.com";

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
                    + "<p>Best Regards.</br><strong style='margin-top: 1px;'><em> Sentinel Tests</em></strong></p></body></html>";
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
            System.out.println("✅ Email sent successfully with Allure, PDF, and Excel reports!");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("❌ Failed to send email: " + e.getMessage());
        }
    }

    private static void attachFile(Multipart multipart, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("⚠️ Attachment file not found: " + filePath);
            return;
        }

        try {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(file);
            multipart.addBodyPart(attachmentPart);
            System.out.println("📎 Attached: " + filePath);
        } catch (IOException | MessagingException e) {
            System.err.println("❌ Error attaching file: " + filePath + " - " + e.getMessage());
        }
    }

    private static String readSummaryFromExcel(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("⚠️ Excel file not found: " + filePath);
            return "<p><b>Summary not available.</b></p>";
        }

        StringBuilder summaryContent = new StringBuilder();
        summaryContent.append("<table border='1' style='border-collapse:collapse;width:100%;text-align:center;'>");
        summaryContent.append("<tr style='background-color:#343a40;color:white;font-weight:bold;'>");
        summaryContent.append("<th>Package Name</th><th>Total Tests</th><th>Passed</th><th>Failed</th><th>Pass %</th></tr>");

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Summary");
            if (sheet == null) {
                System.err.println("⚠️ Summary sheet not found in Excel.");
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
                    } else if (colIndex == 3 && cell.getCellType() == CellType.NUMERIC && cell.getNumericCellValue() > 0) {
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
            System.err.println("❌ Error reading Summary sheet: " + e.getMessage());
            return "<p><b>Error reading Summary sheet.</b></p>";
        }

        summaryContent.append("</table>");
        return summaryContent.toString();
    }

    public static void main(String[] args) {
        System.out.println("🚀 EmailSender started...");
        sendEmailWithReport();
    }
}
