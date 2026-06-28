package com.enviro.assessment.junior.reitumetse.dto;

import java.util.List;

import com.enviro.assessment.junior.reitumetse.entity.Investor;
import com.enviro.assessment.junior.reitumetse.entity.Product;

public class PortfolioResponse {

    private Investor investor;
    private List<Product> products;

    public PortfolioResponse(Investor investor, List<Product> products){
        this.investor = investor;
        this.products = products;
    }

    public Investor getInvestor(){
        return investor;
    }

    public void setInvestor(Investor investor){
        this.investor = investor;
    }

    public List<Product> getProducts(){
        return products;
    }

    public void setProducts(List<Product> products){
        this.products = products;
    }
}