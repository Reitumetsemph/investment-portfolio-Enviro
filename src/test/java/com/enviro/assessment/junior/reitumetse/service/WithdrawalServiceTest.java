package com.enviro.assessment.junior.reitumetse.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enviro.assessment.junior.reitumetse.dto.WithdrawalRequest;
import com.enviro.assessment.junior.reitumetse.entity.Investor;
import com.enviro.assessment.junior.reitumetse.entity.Product;
import com.enviro.assessment.junior.reitumetse.entity.WithdrawalNotice;
import com.enviro.assessment.junior.reitumetse.repository.ProductRepository;
import com.enviro.assessment.junior.reitumetse.repository.WithdrawalNoticeRepository;

@ExtendWith(MockitoExtension.class)
public class WithdrawalServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WithdrawalNoticeRepository withdrawalNoticeRepository;

    @InjectMocks
    private WithdrawalService withdrawalService;

    private Investor youngInvestor;
    private Investor oldInvestor;

    @BeforeEach
    void setUp() {
        youngInvestor = new Investor();
        youngInvestor.setId(1L);
        youngInvestor.setFullName("Alice Young");
        youngInvestor.setEmail("alice@enviro365.com");
        youngInvestor.setAge(30);

        oldInvestor = new Investor();
        oldInvestor.setId(2L);
        oldInvestor.setFullName("Bob Senior");
        oldInvestor.setEmail("bob@enviro365.com");
        oldInvestor.setAge(70);
    }

    private Product makeProduct(Long id, String type, double balance, Investor owner) {
        Product p = new Product();
        p.setId(id);
        p.setType(type);
        p.setBalance(balance);
        p.setInvestor(owner);
        return p;
    }

    private WithdrawalRequest makeRequest(Long productId, double amount) {
        WithdrawalRequest r = new WithdrawalRequest();
        r.setProductId(productId);
        r.setAmount(amount);
        return r;
    }

    @Test
    void shouldRejectZeroOrNegativeAmount() {
        Product savings = makeProduct(1L, "SAVINGS", 10_000.0, youngInvestor);
        when(productRepository.findById(1L)).thenReturn(Optional.of(savings));

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> withdrawalService.createWithdrawal(makeRequest(1L, 0))
        );
        assertEquals("Amount must be greater than zero", ex.getMessage());
    }

    @Test
    void shouldRejectRetirementWithdrawalForInvestorUnder65() {
        Product retirement = makeProduct(2L, "RETIREMENT", 50_000.0, youngInvestor);
        when(productRepository.findById(2L)).thenReturn(Optional.of(retirement));

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> withdrawalService.createWithdrawal(makeRequest(2L, 1000))
        );
        assertEquals("Retirement withdrawals only allowed for investors over 65", ex.getMessage());
    }

    @Test
    void shouldRejectWithdrawalExceedingBalance() {
        Product savings = makeProduct(1L, "SAVINGS", 10_000.0, youngInvestor);
        when(productRepository.findById(1L)).thenReturn(Optional.of(savings));

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> withdrawalService.createWithdrawal(makeRequest(1L, 20_000))
        );
        assertEquals("Withdrawal amount exceeds available balance", ex.getMessage());
    }

    @Test
    void shouldRejectWithdrawalExceedingNinetyPercentOfBalance() {
        Product savings = makeProduct(1L, "SAVINGS", 10_000.0, youngInvestor);
        when(productRepository.findById(1L)).thenReturn(Optional.of(savings));

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> withdrawalService.createWithdrawal(makeRequest(1L, 9_500))
        );
        assertEquals("Withdrawal amount cannot exceed 90% of the balance", ex.getMessage());
    }

    @Test
    void shouldAllowValidWithdrawalAndDeductBalance() {
        Product savings = makeProduct(1L, "SAVINGS", 10_000.0, youngInvestor);
        when(productRepository.findById(1L)).thenReturn(Optional.of(savings));
        when(productRepository.save(any(Product.class))).thenReturn(savings);
        when(withdrawalNoticeRepository.save(any(WithdrawalNotice.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        WithdrawalNotice result = withdrawalService.createWithdrawal(makeRequest(1L, 1_000));

        assertEquals(9_000.0, savings.getBalance());
        assertEquals(1_000.0, result.getAmount());
    }

    @Test
    void shouldAllowRetirementWithdrawalForInvestorOver65() {
        Product retirement = makeProduct(3L, "RETIREMENT", 100_000.0, oldInvestor);
        when(productRepository.findById(3L)).thenReturn(Optional.of(retirement));
        when(productRepository.save(any(Product.class))).thenReturn(retirement);
        when(withdrawalNoticeRepository.save(any(WithdrawalNotice.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        WithdrawalNotice result = withdrawalService.createWithdrawal(makeRequest(3L, 5_000));

        assertEquals(95_000.0, retirement.getBalance());
        assertEquals(5_000.0, result.getAmount());
    }
}