package com.cyberaakash.controlleddatafetch.services.impl;

import com.cyberaakash.controlleddatafetch.dto.CustomerDto;
import com.cyberaakash.controlleddatafetch.entity.Customer;
import com.cyberaakash.controlleddatafetch.mapper.CustomerMapper;
import com.cyberaakash.controlleddatafetch.repository.CustomerRepository;
import com.cyberaakash.controlleddatafetch.services.CustomerService;
import com.cyberaakash.controlleddatafetch.services.ExcelUploadService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void saveCustomersToDatabase(MultipartFile file) {
//        if(ExcelUploadService.isValidExcelFile(file)){
        try {
            List<Customer> customers = ExcelUploadService.getCustomersDataFromExcel(file.getInputStream());
            this.customerRepository.saveAll(customers);
        } catch (IOException e) {
            throw new IllegalArgumentException("The file is not a valid excel file");
        }
//        } else {
//            throw new IllegalArgumentException("The file is not a valid excel file");
//        }
    }

    public List<CustomerDto> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
//                .map((customer -> CustomerMapper.mapToCustomerDto(customer)))
                .map(CustomerMapper::mapToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDto> get20Customers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .limit(20) // Limit to 20 customers
//                .map(customer -> CustomerMapper.mapToCustomerDto(customer))
                .map(CustomerMapper::mapToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDto> getCustomCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        List<Customer> customers = customerPage.getContent();
        return customers.stream()
                .map(CustomerMapper::mapToCustomerDto)
                .collect(Collectors.toList());
    }
}
