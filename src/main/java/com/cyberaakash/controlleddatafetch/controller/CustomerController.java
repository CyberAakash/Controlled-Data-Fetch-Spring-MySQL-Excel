package com.cyberaakash.controlleddatafetch.controller;

import com.cyberaakash.controlleddatafetch.dto.CustomerDto;
import com.cyberaakash.controlleddatafetch.services.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static com.cyberaakash.controlleddatafetch.services.PushDbToExcel.writeCustomersToExcel;

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
    public ResponseEntity<List<CustomerDto>> getCustomCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletResponse response) {
//        return new ResponseEntity<>(customerService.getCustomCustomers(page, size), HttpStatus.OK);
        List<CustomerDto> customers = customerService.getCustomCustomers(page, size);

        // Write data to Excel file
        try {
            writeCustomersToExcel(customers);

            // Check if all records have been fetched
            int totalRecords = customerService.getTotalRecords(); // Implement this method to get total records
            if ((page + 1) * size >= totalRecords) {
                // Set response headers for file download
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=customers.xlsx");

                // Write Excel file to response output stream
                OutputStream outputStream = response.getOutputStream();
                FileInputStream inputStream = new FileInputStream("customers.xlsx");
                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();
                inputStream.close();
                outputStream.close();
            }

        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        }

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

}
