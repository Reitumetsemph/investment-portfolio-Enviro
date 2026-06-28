package com.enviro.assessment.junior.reitumetse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductRequest {

    @NotBlank
    private String type;

    @Positive
    private double balance;

    @NotNull
    private Long investorId;

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public double getBalance(){
        return balance;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }

    public Long getInvestorId(){
        return investorId;
    }

    public void setInvestorId(Long investorId){
        this.investorId = investorId;
    }
}