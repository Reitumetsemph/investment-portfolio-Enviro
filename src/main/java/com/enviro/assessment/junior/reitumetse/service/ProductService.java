package com.enviro.assessment.junior.reitumetse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.enviro.assessment.junior.reitumetse.dto.ProductRequest;
import com.enviro.assessment.junior.reitumetse.entity.Investor;
import com.enviro.assessment.junior.reitumetse.entity.Product;
import com.enviro.assessment.junior.reitumetse.repository.InvestorRepository;
import com.enviro.assessment.junior.reitumetse.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InvestorRepository investorRepository;

    public ProductService(ProductRepository productRepository, InvestorRepository investorRepository){
        this.productRepository = productRepository;
        this.investorRepository = investorRepository;
    }

    public Product createProduct(ProductRequest request){
        Investor investor = investorRepository.findById(request.getInvestorId()).orElseThrow();

        Product product = new Product();
        product.setType(request.getType());
        product.setBalance(request.getBalance());
        product.setInvestor(investor);

        return productRepository.save(product);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}