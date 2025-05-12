package com.nexign.brt.service;

import com.nexign.brt.dto.PaymentRequestDto;
import com.nexign.brt.dto.PaymentResponseDto;
import com.nexign.brt.entity.ClientEntity;
import com.nexign.brt.entity.TransactionEntity;
import com.nexign.brt.exception.InternalServerErrorException;
import com.nexign.brt.exception.InvalidPaymentAmountException;
import com.nexign.brt.exception.SubscriberNotFoundException;
import com.nexign.brt.repository.ClientRepository;
import com.nexign.brt.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public PaymentResponseDto payBalance(String msisdn, PaymentRequestDto paymentRequest) throws InternalServerErrorException {
        ClientEntity client = clientRepository.findClientByMsisdn(msisdn)
                .orElseThrow(() -> new SubscriberNotFoundException("Subscriber not found"));

        if (paymentRequest.getMoney().compareTo(BigDecimal.valueOf(0.1)) < 0) {
            throw new InvalidPaymentAmountException("Minimum allowed payment is 0.1");
        }

        BigDecimal updatedBalance = client.getMoney().add(paymentRequest.getMoney());
        client.setMoney(updatedBalance);

        TransactionEntity transaction = TransactionEntity.builder()
                .msisdn(client)
                .money(paymentRequest.getMoney())
                .description("Payment for msisdn: " + msisdn)
                .date(LocalDateTime.now())
                .build();

        try {
            clientRepository.save(client);
            transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to update balance due to internal error", e);
        }

        return new PaymentResponseDto(updatedBalance);
    }
}
