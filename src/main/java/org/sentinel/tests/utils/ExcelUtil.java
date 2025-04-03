package org.sentinel.tests.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sentinel.tests.utils.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {
    private static final Object lock = new Object();
    private static final String FILE_PATH;
    private static String FILE_PATH_LATEST = null;

    static {
        String userDir = System.getProperty("user.dir"); // Gets the current working directory
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        FILE_PATH = userDir + File.separator + "reports" + File.separator + "excel-report" + File.separator + "TestSummary.xlsx";
    }

    /**
     * Reads key-value pairs from an Excel sheet and stores them in a Map.
     */
    public static Map<String, String> readKeyValuePairs(String fileName, String sheetName) {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "src" + File.separator + "data-files" + File.separator + "excel" + File.separator + fileName;

        synchronized (lock) {
            Map<String, String> dataMap = new HashMap<>();

            try (FileInputStream fis = new FileInputStream(filePath);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    Logger.info("Sheet not found: " + sheetName);
                    return dataMap;
                }

                for (Row row : sheet) {
                    if (row.getPhysicalNumberOfCells() < 2) continue;

                    Cell keyCell = row.getCell(0);
                    Cell valueCell = row.getCell(1);

                    if (keyCell != null && valueCell != null) {
                        String key = getStringCellValue(keyCell).trim();
                        String value = getStringCellValue(valueCell).trim();
                        dataMap.put(key, value);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return dataMap;
        }
    }

    /**
     * Creates an Excel file with a predefined structure if it doesn't exist.
     */
    public static void createExcelFile() {
        synchronized (lock) {
            try {
                String directoryPath = FILE_PATH.substring(0, FILE_PATH.lastIndexOf(File.separator));
                File directory = new File(directoryPath);

                if (!directory.exists()) {
                    directory.mkdirs(); // Ensure directory exists
                }

                FILE_PATH_LATEST = FILE_PATH;
                Logger.info(String.format("File Location: %s", FILE_PATH_LATEST));

                File file = new File(FILE_PATH_LATEST);
                if (file.exists()) {
                    Logger.info("Excel file already exists: " + FILE_PATH_LATEST);
                    return;
                }

                try (Workbook workbook = new XSSFWorkbook();
                     FileOutputStream fos = new FileOutputStream(FILE_PATH_LATEST)) {

//                    Sheet sheet = workbook.createSheet("TestCases");
//                    Row headerRow = sheet.createRow(0);
//                    String[] headers = {"Sr No", "Package Name", "Method Name", "Test Case Status", "Remark"};
//
//                    for (int i = 0; i < headers.length; i++) {
//                        Cell cell = headerRow.createCell(i);
//                        cell.setCellValue(headers[i]);
//                    }

                    workbook.write(fos);
                    Logger.info("Excel file created successfully: " + FILE_PATH_LATEST);
                }
            } catch (IOException e) {
                System.err.println("Error creating Excel file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a new row to the Excel file for each test case.
     */
    public static void addTestCase(String packageName, String methodName, String status, String remark) {
        synchronized (lock) {
            try (FileInputStream fis = new FileInputStream(FILE_PATH_LATEST);
                 Workbook workbook = new XSSFWorkbook(fis);
                 FileOutputStream fos = new FileOutputStream(FILE_PATH_LATEST)) {

                Sheet sheet = workbook.getSheet("TestCases");
                if (sheet == null) {
                    Logger.info("Sheet 'TestCases' not found!");
                    return;
                }

                int lastRowNum = sheet.getLastRowNum();
                Row newRow = sheet.createRow(lastRowNum + 1);

                newRow.createCell(0).setCellValue(lastRowNum + 1); // Sr No
                newRow.createCell(1).setCellValue(packageName);
                newRow.createCell(2).setCellValue(methodName);
                newRow.createCell(3).setCellValue(status);
                newRow.createCell(4).setCellValue(remark);

                workbook.write(fos);
                Logger.info("Added Test Case: " + methodName + " -> Status: " + status + ", Remark: " + remark);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Converts any cell type to a string representation.
     */
    private static String getStringCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "Unknown Type!";
        }
    }

    public static void addTestCases(List<Map<String, String>> testCases) {
        synchronized (lock) {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            Workbook workbook = null;

            try {
                File file = new File(FILE_PATH_LATEST);
                if (!file.exists()) {
                    workbook = new XSSFWorkbook(); // Create new workbook if file does not exist
                } else {
                    fis = new FileInputStream(file);
                    workbook = new XSSFWorkbook(fis);
                }

                Map<String, Map<String, Integer>> summaryMap = new LinkedHashMap<>();
                boolean dataAdded = false;

                for (Map<String, String> testCase : testCases) {
                    String packageName = testCase.getOrDefault("Package", "Uncategorized");
                    String methodName = testCase.get("Method");
                    String status = testCase.get("Status");
                    String remark = testCase.get("Remark");

                    if (methodName == null || status == null) {
                        continue;
                    }

                    status = status.trim();

                    // Get or create the sheet
                    Sheet sheet = workbook.getSheet(packageName.split(".tests.")[1]);
                    if (sheet == null) {
                        sheet = workbook.createSheet(packageName.split(".tests.")[1]);
                        createHeader(sheet);
                    }

                    int lastRowNum = sheet.getLastRowNum();
                    Row newRow = sheet.createRow(lastRowNum + 1);
                    newRow.createCell(0).setCellValue(lastRowNum + 1);
                    newRow.createCell(1).setCellValue(packageName);
                    newRow.createCell(2).setCellValue(methodName);

                    newRow.createCell(3).setCellValue(status);

                    Cell statusCell = newRow.createCell(3);
                    statusCell.setCellValue(status);
                    statusCell.setCellStyle(getStatusCellStyle(workbook, status));

                    newRow.createCell(4).setCellValue(remark);

                    dataAdded = true;
                    summaryMap.putIfAbsent(packageName, new HashMap<>());
                    Map<String, Integer> packageSummary = summaryMap.get(packageName);

                    packageSummary.putIfAbsent("Total", 0);
                    packageSummary.putIfAbsent("Pass", 0);
                    packageSummary.putIfAbsent("Fail", 0);

                    packageSummary.put("Total", packageSummary.get("Total") + 1);

                    if ("Pass".equalsIgnoreCase(status)) {
                        packageSummary.put("Pass", packageSummary.get("Pass") + 1);
                    } else {
                        packageSummary.put("Fail", packageSummary.get("Fail") + 1);
                    }
                }

                if (!dataAdded) {
                    Logger.info("No valid test cases found. Skipping Excel update.");
                    return;
                }

                removeEmptySheets(workbook);
                updateSummarySheet(workbook, summaryMap);

                fos = new FileOutputStream(FILE_PATH_LATEST);
                workbook.write(fos);
                Logger.info("Excel update complete!");

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) fis.close();
                    if (fos != null) fos.close();
                    if (workbook != null) workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static void createHeader(Sheet sheet) {
        if (sheet.getPhysicalNumberOfRows() > 0) {
            return;
        }
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Sr No", "Package", "Method", "Status", "Remark"};
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }
    }

    private static void updateSummarySheet(Workbook workbook, Map<String, Map<String, Integer>> summaryMap) {
        Sheet summarySheet = workbook.getSheet("Summary");

        // Delete and recreate Summary sheet to avoid stale data issues
        if (summarySheet != null) {
            int sheetIndex = workbook.getSheetIndex(summarySheet);
            workbook.removeSheetAt(sheetIndex);
        }
        summarySheet = workbook.createSheet("Summary");

        workbook.setSheetOrder("Summary", 0); // Make Summary the first sheet

        // Create header row
        Row headerRow = summarySheet.createRow(0);
        String[] columns = {"Package Name", "Total Tests", "Passed", "Failed", "Pass %"};
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        int rowIndex = 1;
        for (Map.Entry<String, Map<String, Integer>> entry : summaryMap.entrySet()) {
            String packageName = entry.getKey();
            Map<String, Integer> counts = entry.getValue();
            int total = counts.getOrDefault("Total", 0);
            int passed = counts.getOrDefault("Pass", 0);
            int failed = counts.getOrDefault("Fail", 0);
            double passPercentage = total > 0 ? (passed * 100.0 / total) : 0;

            if (total == 0) continue; // Avoid adding empty rows

            Row summaryRow = summarySheet.createRow(rowIndex++);
            summaryRow.createCell(0).setCellValue(packageName);
            summaryRow.createCell(1).setCellValue(total);
            summaryRow.createCell(2).setCellValue(passed);
            summaryRow.createCell(3).setCellValue(failed);
            summaryRow.createCell(4).setCellValue(String.format("%.2f%%", passPercentage));
        }

        // Auto-size columns for better readability
        for (int i = 0; i < columns.length; i++) {
            summarySheet.autoSizeColumn(i);
        }
    }

    private static void removeEmptySheets(Workbook workbook) {
        for (int i = workbook.getNumberOfSheets() - 1; i >= 0; i--) {
            Sheet sheet = workbook.getSheetAt(i);
            if (!sheet.getSheetName().equals("Summary") && sheet.getPhysicalNumberOfRows() == 0) {
                workbook.removeSheetAt(i);
            }
        }
    }

    private static CellStyle getStatusCellStyle(Workbook workbook, String status) {
        CellStyle style = workbook.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        if ("Pass".equalsIgnoreCase(status)) {
            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex()); // 🟢 Green
        } else if ("Fail".equalsIgnoreCase(status)) {
            style.setFillForegroundColor(IndexedColors.RED.getIndex()); // 🔴 Red
        } else if ("Skip".equalsIgnoreCase(status)) {
            style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex()); // 🔵 Blue
        } else {
            style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex()); // 🟡 Yellow for Warning/Other
        }

        return style;
    }

}