package com.cyberaakash.controlleddatafetch.services;

import com.cyberaakash.controlleddatafetch.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ExcelUploadService {
    public static boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Customer> getCustomersDataFromExcel(InputStream inputStream){
        List<Customer> customers = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("customers");
            int rowIndex =0;
            for (Row row : sheet){
                if (rowIndex ==0){
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                Customer customer = new Customer();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell != null) {
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    customer.setCustomerID((int) cell.getNumericCellValue());
                                }
                                break;
                            case 1:
                                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                    customer.setGender(cell.getStringCellValue());
                                } else {
                                    customer.setGender(null); // Set null if cell is empty
                                }
                                break;
                            case 2:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    customer.setAge((int) cell.getNumericCellValue());
                                } else {
                                    customer.setAge(null); // Set null if cell is empty
                                }
                                break;
                            case 3:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    customer.setAnnual_Income((int) cell.getNumericCellValue());
                                } else {
                                    customer.setAnnual_Income(null); // Set null if cell is empty
                                }
                                break;
                            case 4:
                                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue() != null && !cell.getStringCellValue().isEmpty()) {
                                    customer.setProfession(cell.getStringCellValue());
                                } else {
                                    customer.setProfession(null); // Set null if cell is empty
                                }
                                break;
                            case 5:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    customer.setWork_Experience((int) cell.getNumericCellValue());
                                } else {
                                    customer.setWork_Experience(null); // Set null if cell is empty
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    cellIndex++;
                }

                customers.add(customer);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return customers;
    }

}
