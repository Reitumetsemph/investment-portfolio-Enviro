package com.enviro.assessment.junior.reitumetse.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enviro.assessment.junior.reitumetse.entity.WithdrawalNotice;

public interface WithdrawalNoticeRepository extends JpaRepository<WithdrawalNotice, Long> {

    List<WithdrawalNotice> findByProductInvestorId(Long investorId);

    List<WithdrawalNotice> findByProductInvestorIdAndDateBetween(Long investorId, LocalDateTime from, LocalDateTime to);
}