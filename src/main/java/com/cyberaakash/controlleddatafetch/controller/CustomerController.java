package com.cyberaakash.controlleddatafetch.controller;

import com.cyberaakash.controlleddatafetch.dto.CustomerDto;
import com.cyberaakash.controlleddatafetch.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.cyberaakash.controlleddatafetch.services.PushDbToExcel.writeCustomersToExcel;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/upload-customers-data")
    public ResponseEntity<?> uploadCustomersData(@RequestParam("file") MultipartFile file) {
        this.customerService.saveCustomersToDatabase(file);
        return ResponseEntity
                .ok(Map.of("Message", " Customers data uploaded and saved to database successfully"));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        return new ResponseEntity<>(customerService.getCustomers(), HttpStatus.FOUND);
    }

    @GetMapping("/first20")
    public ResponseEntity<List<CustomerDto>> get20Customers() {
        return new ResponseEntity<>(customerService.get20Customers(), HttpStatus.FOUND);
    }

    @GetMapping("/custom")
    public ResponseEntity<?> getCustomCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request,
            HttpServletResponse response) {
//        return new ResponseEntity<>(customerService.getCustomCustomers(page, size), HttpStatus.OK);
        List<CustomerDto> customers = customerService.getCustomCustomers(page, size);

        // Write data to Excel file
        try {
            writeCustomersToExcel(customers);

            Path filePath = Paths.get("customers.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile());
            XSSFSheet sheet = workbook.getSheet("Customers");

            int totalRecords = sheet.getLastRowNum(); // Count includes header row
            workbook.close();
            int totalPages = (int) Math.ceil((double) (totalRecords-1) / size);
            log.info("Total Pages: {}", totalPages);

            int records = customerService.getTotalRecords();
            int pages = (int) Math.ceil((double) (records) / size);
            log.info("Pages: {}", pages);


            if (pages == totalPages) {
                // Prepare file path
                Resource resource = new UrlResource(filePath.toUri());

                // Determine content type
                String contentType = null;
                try {
                    contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                } catch (IOException ex) {
                    log.warn("Could not determine the requested file type.");
                }
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                // Set response headers for file download
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            }
            else {
                // If not all records have been fetched, return the list of customers
                return new ResponseEntity<>(customers, HttpStatus.OK);
            }
        } catch (IOException | InvalidFormatException e) {
            // Handle IOException
            e.printStackTrace();
        }

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

}
