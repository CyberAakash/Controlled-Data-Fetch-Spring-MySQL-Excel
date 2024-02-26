package com.cyberaakash.controlleddatafetch.services;

import com.cyberaakash.controlleddatafetch.dto.CustomerDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    void saveCustomersToDatabase(MultipartFile file);
    List<CustomerDto> getCustomers();

    List<CustomerDto> get20Customers();

    List<CustomerDto> getCustomCustomers(int page, int size);

    int getTotalRecords();
}
