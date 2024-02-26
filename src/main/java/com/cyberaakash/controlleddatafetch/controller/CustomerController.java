package com.cyberaakash.controlleddatafetch.controller;

import com.cyberaakash.controlleddatafetch.dto.CustomerDto;
import com.cyberaakash.controlleddatafetch.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    private CustomerService customerService;

    @PostMapping("/upload-customers-data")
    public ResponseEntity<?> uploadCustomersData(@RequestParam("file") MultipartFile file){
        this.customerService.saveCustomersToDatabase(file);
        return ResponseEntity
                .ok(Map.of("Message" , " Customers data uploaded and saved to database successfully"));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomers(){
        return new ResponseEntity<>(customerService.getCustomers(), HttpStatus.FOUND);
    }

    @GetMapping("/first20")
    public ResponseEntity<List<CustomerDto>> get20Customers() {
        return new ResponseEntity<>(customerService.get20Customers(), HttpStatus.FOUND);
    }

    @GetMapping("/custom")
    public ResponseEntity<List<CustomerDto>> getCustomCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return new ResponseEntity<>(customerService.getCustomCustomers(page, size), HttpStatus.OK);
    }

}
