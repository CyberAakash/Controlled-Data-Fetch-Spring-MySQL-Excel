package com.cyberaakash.controlleddatafetch.services;

import com.cyberaakash.controlleddatafetch.dto.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class PushDbToExcel {

    public static void writeCustomersToExcel(List<CustomerDto> customers) throws IOException {
        // Load existing workbook if file exists, otherwise create a new workbook
        XSSFWorkbook workbook;
        File file = new File("customers.xlsx");
        if (file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
        } else {
            workbook = new XSSFWorkbook();
        }

        // Get or create sheet
        XSSFSheet sheet = workbook.getSheet("Customers");
        if (sheet == null) {
            sheet = workbook.createSheet("Customers");
            // Create header row if sheet is newly created
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Customer ID");
            headerRow.createCell(1).setCellValue("Gender");
            headerRow.createCell(2).setCellValue("Age");
            headerRow.createCell(3).setCellValue("Annual Income");
            headerRow.createCell(4).setCellValue("Profession");
            headerRow.createCell(5).setCellValue("Work Experience");
        }

        // Get the last row number
        int lastRowNum = sheet.getLastRowNum();

        // Write customer data to rows, starting from the last row number + 1
        for (CustomerDto customer : customers) {
            // Check if the customer already exists in the Excel file
            boolean customerExists = false;
            for (int i = 1; i <= lastRowNum; i++) { // Start from 1 to skip header row
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null && row.getCell(0).getCellType() == CellType.NUMERIC) {
                    int customerId = (int) row.getCell(0).getNumericCellValue();
                    if (customerId == customer.getCustomerID()) {
                        // Customer exists, update the existing record
                        customerExists = true;
                        updateCustomerRow(row, customer);
                        break;
                    }
                }
            }
            if (!customerExists) {
                // Customer doesn't exist, append a new record
                Row newRow = sheet.createRow(++lastRowNum);
                writeCustomerToRow(newRow, customer);
            }
        }

        // Write workbook to OutputStream
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        }
    }

    private static void updateCustomerRow(Row row, CustomerDto customer) {
        // Update existing customer record in the Excel file
        row.getCell(1).setCellValue(customer.getGender());
        if (customer.getAge() != null) {
            row.getCell(2).setCellValue(customer.getAge());
        }
        if (customer.getAnnual_Income() != null) {
            row.getCell(3).setCellValue(customer.getAnnual_Income());
        }
        row.getCell(4).setCellValue(customer.getProfession());
        if (customer.getWork_Experience() != null) {
            row.getCell(5).setCellValue(customer.getWork_Experience());
        }
    }

    private static void writeCustomerToRow(Row row, CustomerDto customer) {
        // Write customer data to the Excel row
        row.createCell(0).setCellValue(customer.getCustomerID());
        row.createCell(1).setCellValue(customer.getGender());
        if (customer.getAge() != null) {
            row.createCell(2).setCellValue(customer.getAge());
        }
        if (customer.getAnnual_Income() != null) {
            row.createCell(3).setCellValue(customer.getAnnual_Income());
        }
        row.createCell(4).setCellValue(customer.getProfession());
        if (customer.getWork_Experience() != null) {
            row.createCell(5).setCellValue(customer.getWork_Experience());
        }
    }

}
