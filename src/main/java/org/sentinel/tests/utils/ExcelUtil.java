/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sentinel.tests.utils.log.LoggerUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for Excel file operations in test automation reporting.
 * This class provides methods for reading from and writing to Excel files,
 * specifically designed for test case management and reporting.
 * 
 * The class includes functionality for:
 * - Reading key-value pairs from Excel sheets
 * - Creating new Excel files with predefined structures
 * - Adding test case results to Excel sheets
 * - Generating test execution summaries
 * - Managing test case status with color-coded cells
 * 
 * Thread-safety is ensured through synchronization on all file operations.
 * The class uses Apache POI library for Excel operations and supports XLSX format.
 * 
 * File structure:
 * - Summary sheet: Contains overall test execution statistics
 * - Package-specific sheets: Contains detailed test case results for each package
 * 
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see org.apache.poi.xssf.usermodel.XSSFWorkbook
 * @see org.apache.poi.ss.usermodel.Workbook
 */
public class ExcelUtil {
    private static final Object lock = new Object();
    private static final String FILE_PATH;
    private static String FILE_PATH_LATEST = null;


    static {
        String userDir = System.getProperty("user.dir"); // Gets the current working directory
        FILE_PATH = userDir + File.separator + "reports" + File.separator + "excel-report" + File.separator + "TestSummary.xlsx";
    }

    /**
     * Reads key-value pairs from a specified Excel file and sheet.
     * The first column is treated as the key and the second column as the value.
     * 
     * @param fileName The name of the Excel file to read from
     * @param sheetName The name of the sheet to read from
     * @return A map containing key-value pairs read from the specified sheet
     */
    public static Map<String, String> readKeyValuePairs(String fileName, String sheetName) {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "src" + File.separator + "data-files" + File.separator + "excel" + File.separator + fileName;

        synchronized (lock) {
            Map<String, String> dataMap = new HashMap<>();

            try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    LoggerUtil.info("Sheet not found: " + sheetName);
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
     * Creates a new Excel file with a predefined structure for test case management.
     * The file includes a summary sheet and individual sheets for each package.
     * 
     * The summary sheet contains the following columns:
     * - Package Name
     * - Total Tests
     * - Passed
     * - Failed
     * - Pass %
     * 
     * Each package sheet contains the following columns:
     * - Sr No
     * - Package Name
     * - Method Name
     * - Status (Pass/Fail/Skip)
     * - Remark/Error Message
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
                LoggerUtil.info(String.format("File Location: %s", FILE_PATH_LATEST));

                File file = new File(FILE_PATH_LATEST);
                if (file.exists()) {
                    LoggerUtil.info("Excel file already exists: " + FILE_PATH_LATEST);
                    return;
                }

                try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fos = new FileOutputStream(FILE_PATH_LATEST)) {
                    workbook.write(fos);
                    LoggerUtil.info("Excel file created successfully: " + FILE_PATH_LATEST);
                }
            } catch (IOException e) {
                LoggerUtil.error("Error creating Excel file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a test case result to the latest Excel file.
     * This method is synchronized to ensure thread safety when adding results.
     * 
     * @param packageName The name of the package containing the test case
     * @param methodName The name of the test method
     * @param status The status of the test (e.g., "Pass", "Fail", "Skip")
     * @param remark Additional remarks or error messages related to the test case
     */
    public static void addTestCase(String packageName, String methodName, String status, String remark) {
        synchronized (lock) {
            try (FileInputStream fis = new FileInputStream(FILE_PATH_LATEST); Workbook workbook = new XSSFWorkbook(fis); FileOutputStream fos = new FileOutputStream(FILE_PATH_LATEST)) {

                Sheet sheet = workbook.getSheet("TestCases");
                if (sheet == null) {
                    LoggerUtil.info("Sheet 'TestCases' not found!");
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
                LoggerUtil.info("Added Test Case: " + methodName + " -> Status: " + status + ", Remark: " + remark);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the string value from a cell, handling different cell types.
     * 
     * @param cell The cell to retrieve the value from
     * @return The string value of the cell
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
  

    /**
     * Adds test cases to an Excel workbook, organizing them by package and maintaining a summary sheet.
     * This method is thread-safe through synchronization.
     * 
     * The method performs the following operations:
     * - Creates a new workbook if it doesn't exist
     * - Organizes test cases by package name in separate sheets
     * - Creates/updates summary statistics for test cases
     * - Applies formatting based on test status
     * - Removes empty sheets
     * - Updates the summary sheet with package-wise statistics
     *
     * @param testCases A List of Maps where each Map represents a test case with the following keys:
     *                  - "Package": The package name of the test (defaults to "Uncategorized")
     *                  - "Method": The name of the test method
     *                  - "Status": The test status ("Pass" or "Fail")
     *                  - "Remark": Any additional comments or remarks
     *                  
     * @throws IOException If there are issues with file operations
     * 
     * Note: The method silently skips invalid test cases (those missing Method or Status)
     *       and logs a message if no valid test cases are found to process.
     */
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
                    LoggerUtil.info("No valid test cases found. Skipping Excel update.");
                    return;
                }

                removeEmptySheets(workbook);
                updateSummarySheet(workbook, summaryMap);

                fos = new FileOutputStream(FILE_PATH_LATEST);
                workbook.write(fos);
                LoggerUtil.info("Excel update complete!");

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

    /**
     * Creates a header row for the specified sheet.
     * The header includes the following columns:
     * - Sr No
     * - Package Name
     * - Method Name
     * - Status (Pass/Fail/Skip)
     * - Remark/Error Message
     *
     * @param sheet The sheet to create the header for
     */
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

    /**
     * Updates the summary sheet with package-wise statistics.
     * The summary includes the following columns:
     * - Package Name
     * - Total Tests
     * - Passed
     * - Failed
     * - Pass %
     *
     * @param workbook The workbook containing the summary sheet
     * @param summaryMap A map containing package-wise statistics
     */
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

    /**
     * Removes empty sheets from the workbook, except for the "Summary" sheet.
     * This helps in keeping the workbook clean and focused on relevant data.
     *
     * @param workbook The workbook from which to remove empty sheets
     */
    private static void removeEmptySheets(Workbook workbook) {
        for (int i = workbook.getNumberOfSheets() - 1; i >= 0; i--) {
            Sheet sheet = workbook.getSheetAt(i);
            if (!sheet.getSheetName().equals("Summary") && sheet.getPhysicalNumberOfRows() == 0) {
                workbook.removeSheetAt(i);
            }
        }
    }

    /**
     * Returns a CellStyle for the given status, applying color coding based on the status.
     * The method uses Apache POI's IndexedColors to set the cell background color.
     *
     * @param workbook The workbook to create the cell style in
     * @param status The status of the test case (e.g., "Pass", "Fail", "Skip")
     * @return A CellStyle object with the appropriate background color for the status
     */
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