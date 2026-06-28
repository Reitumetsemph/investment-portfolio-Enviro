package com.enviro.assessment.junior.reitumetse.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enviro.assessment.junior.reitumetse.dto.WithdrawalRequest;
import com.enviro.assessment.junior.reitumetse.entity.WithdrawalNotice;
import com.enviro.assessment.junior.reitumetse.service.WithdrawalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/withdrawals")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    public WithdrawalController(WithdrawalService withdrawalService){
        this.withdrawalService = withdrawalService;
    }

    @PostMapping
    public WithdrawalNotice createWithdrawal(@Valid @RequestBody WithdrawalRequest request){
        return withdrawalService.createWithdrawal(request);
    }

    @GetMapping("/investor/{investorId}")
    public List<WithdrawalNotice> getHistory(@PathVariable Long investorId){
        return withdrawalService.getHistory(investorId);
    }

    @GetMapping("/investor/{investorId}/export")
public ResponseEntity<String> exportCsv(
    @PathVariable Long investorId,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
){
    String csv = withdrawalService.exportCsv(investorId, from, to);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"withdrawals.csv\"")
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(csv);
}
}