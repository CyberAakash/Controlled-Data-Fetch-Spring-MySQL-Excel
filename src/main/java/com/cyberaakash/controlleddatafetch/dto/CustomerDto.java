package com.cyberaakash.controlleddatafetch.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDto {
    private Integer CustomerID;
    private String Gender;
    private Integer Age;
    private String Profession;
    private Integer Annual_Income;
    private Integer Work_Experience;
}
