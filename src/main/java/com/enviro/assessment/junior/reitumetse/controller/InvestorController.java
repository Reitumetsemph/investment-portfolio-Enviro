package com.enviro.assessment.junior.reitumetse.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enviro.assessment.junior.reitumetse.dto.PortfolioResponse;
import com.enviro.assessment.junior.reitumetse.entity.Investor;
import com.enviro.assessment.junior.reitumetse.service.InvestorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/investors")
public class InvestorController {

    private final InvestorService investorService;

    public InvestorController(InvestorService investorService){
        this.investorService = investorService;
    }

    @PostMapping
    public Investor createInvestor(@Valid @RequestBody Investor investor){
        return investorService.createInvestor(investor);
    }

    @GetMapping
    public List<Investor> getAllInvestors(){
        return investorService.getAllInvestors();
    }

    @GetMapping("/{id}")
    public Investor getInvestorById(@PathVariable Long id){
    return investorService.getInvestorById(id);
    }
    
    @GetMapping("/{id}/portfolio")
    public PortfolioResponse getPortfolio(@PathVariable Long id){
        return investorService.getPortfolio(id);
    }
}