package com.cyberaakash.controlleddatafetch.repository;

import com.cyberaakash.controlleddatafetch.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
