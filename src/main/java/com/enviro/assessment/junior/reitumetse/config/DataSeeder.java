package com.enviro.assessment.junior.reitumetse.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.enviro.assessment.junior.reitumetse.entity.Investor;
import com.enviro.assessment.junior.reitumetse.entity.Product;
import com.enviro.assessment.junior.reitumetse.repository.InvestorRepository;
import com.enviro.assessment.junior.reitumetse.repository.ProductRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final InvestorRepository investorRepository;
    private final ProductRepository productRepository;

    public DataSeeder(InvestorRepository investorRepository, ProductRepository productRepository){
        this.investorRepository = investorRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args){

        // Only seed if there are no investors yet
        if (investorRepository.count() > 0){
            return;
        }

        Investor alice = new Investor();
        alice.setFullName("Alice Young");
        alice.setEmail("alice@enviro365.com");
        alice.setAge(30);
        investorRepository.save(alice);

        Investor bob = new Investor();
        bob.setFullName("Bob Senior");
        bob.setEmail("bob@enviro365.com");
        bob.setAge(70);
        investorRepository.save(bob);

        Product aliceSavings = new Product();
        aliceSavings.setType("SAVINGS");
        aliceSavings.setBalance(10000);
        aliceSavings.setInvestor(alice);
        productRepository.save(aliceSavings);

        Product aliceRetirement = new Product();
        aliceRetirement.setType("RETIREMENT");
        aliceRetirement.setBalance(50000);
        aliceRetirement.setInvestor(alice);
        productRepository.save(aliceRetirement);

        Product bobRetirement = new Product();
        bobRetirement.setType("RETIREMENT");
        bobRetirement.setBalance(100000);
        bobRetirement.setInvestor(bob);
        productRepository.save(bobRetirement);

        System.out.println(">>> Seeded 2 investors and 3 products");
    }
}