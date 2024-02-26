package com.cyberaakash.controlleddatafetch.mapper;

import com.cyberaakash.controlleddatafetch.dto.CustomerDto;
import com.cyberaakash.controlleddatafetch.entity.Customer;

public class CustomerMapper {
    public static Customer mapToCustomer(CustomerDto customerDto) {
        Customer customer = new Customer(
                customerDto.getCustomerID(),
                customerDto.getGender(),
                customerDto.getAge(),
                customerDto.getProfession(),
                customerDto.getAnnual_Income(),
                customerDto.getWork_Experience()
        );

        return customer;
    }

    public  static  CustomerDto mapToCustomerDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto(
                customer.getCustomerID(),
                customer.getGender(),
                customer.getAge(),
                customer.getProfession(),
                customer.getAnnual_Income(),
                customer.getWork_Experience()
        );

        return customerDto;
    }
}
