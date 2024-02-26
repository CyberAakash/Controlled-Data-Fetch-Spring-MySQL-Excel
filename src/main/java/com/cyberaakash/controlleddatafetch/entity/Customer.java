package com.cyberaakash.controlleddatafetch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    private Integer CustomerID;
    private String Gender;
    private Integer Age;
    private String Profession;
    private Integer Annual_Income;
    private Integer Work_Experience;
}
