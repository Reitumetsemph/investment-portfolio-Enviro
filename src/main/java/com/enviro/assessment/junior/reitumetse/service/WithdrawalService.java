package com.enviro.assessment.junior.reitumetse.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.enviro.assessment.junior.reitumetse.dto.WithdrawalRequest;
import com.enviro.assessment.junior.reitumetse.entity.Investor;
import com.enviro.assessment.junior.reitumetse.entity.Product;
import com.enviro.assessment.junior.reitumetse.entity.WithdrawalNotice;
import com.enviro.assessment.junior.reitumetse.repository.ProductRepository;
import com.enviro.assessment.junior.reitumetse.repository.WithdrawalNoticeRepository;

@Service
public class WithdrawalService {

    private final ProductRepository productRepository;
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public WithdrawalService(ProductRepository productRepository, WithdrawalNoticeRepository withdrawalNoticeRepository){
        this.productRepository = productRepository;
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    public WithdrawalNotice createWithdrawal(WithdrawalRequest request){

        //Finds the product
        Product product = productRepository.findById(request.getProductId()).orElseThrow();
        Investor investor = product.getInvestor();
        double amount = request.getAmount();

        //Check the busi rules
        if (amount <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (product.getType().equalsIgnoreCase("RETIREMENT") && investor.getAge() <= 65){
            throw new IllegalArgumentException("Retirement withdrawals only allowed for investors over 65");
        }

        if (amount > product.getBalance()){
            throw new IllegalArgumentException("Withdrawal amount exceeds available balance");
        }

        if (amount > product.getBalance() * 0.9){
            throw new IllegalArgumentException("Withdrawal amount cannot exceed 90% of the balance");
        }

        //Deduct from the product balance
        product.setBalance(product.getBalance() - amount);
        productRepository.save(product);

        //create the withdrawal notice
        WithdrawalNotice notice = new WithdrawalNotice();
        notice.setProduct(product);
        notice.setAmount(amount);
        notice.setDate(LocalDateTime.now());

        return withdrawalNoticeRepository.save(notice);
    }

    public List<WithdrawalNotice> getHistory(Long investorId){
        return withdrawalNoticeRepository.findByProductInvestorId(investorId);
    }

    public String exportCsv(Long investorId, LocalDate from, LocalDate to){
    LocalDateTime fromDt = from.atStartOfDay();
    LocalDateTime toDt = to.atTime(LocalTime.MAX);

    List<WithdrawalNotice> notices = withdrawalNoticeRepository
        .findByProductInvestorIdAndDateBetween(investorId, fromDt, toDt);

    StringBuilder csv = new StringBuilder();
    csv.append("ID,Date,Product Type,Amount\n");

    for (WithdrawalNotice n : notices){
        csv.append(n.getId()).append(",")
           .append(n.getDate()).append(",")
           .append(n.getProduct().getType()).append(",")
           .append(n.getAmount()).append("\n");
    }

    return csv.toString();
}
}