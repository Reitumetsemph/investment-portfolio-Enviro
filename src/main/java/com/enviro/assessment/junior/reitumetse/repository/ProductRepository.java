package com.enviro.assessment.junior.reitumetse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enviro.assessment.junior.reitumetse.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByInvestorId(Long investorId);
}