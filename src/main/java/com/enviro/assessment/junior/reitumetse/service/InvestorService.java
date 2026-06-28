package com.enviro.assessment.junior.reitumetse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.enviro.assessment.junior.reitumetse.dto.PortfolioResponse;
import com.enviro.assessment.junior.reitumetse.entity.Investor;
import com.enviro.assessment.junior.reitumetse.entity.Product;
import com.enviro.assessment.junior.reitumetse.repository.InvestorRepository;
import com.enviro.assessment.junior.reitumetse.repository.ProductRepository;

@Service
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final ProductRepository productRepository;

    public InvestorService(InvestorRepository investorRepository, ProductRepository productRepository){
    this.investorRepository = investorRepository;
    this.productRepository = productRepository;
}

    public Investor createInvestor(Investor investor){
        return investorRepository.save(investor);
    }

    public List<Investor> getAllInvestors(){
        return investorRepository.findAll();
    }

    public Investor getInvestorById(Long id){
    return investorRepository.findById(id).orElseThrow();
    } 

    public PortfolioResponse getPortfolio(Long investorId){
    Investor investor = investorRepository.findById(investorId).orElseThrow();
    List<Product> products = productRepository.findByInvestorId(investorId);
    return new PortfolioResponse(investor, products);
}
}