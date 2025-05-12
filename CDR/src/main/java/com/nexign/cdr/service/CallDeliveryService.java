package com.nexign.cdr.service;

import com.nexign.cdr.config.RabbitConfig;
import com.nexign.cdr.entity.Call;
import com.nexign.cdr.repository.CallRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CallDeliveryService {

    private final CallRepository callRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitConfig rabbitConfig;

    @Scheduled(fixedDelay = 5000)
    public void deliverCallsToQueue() {
        while (true) {
            List<Call> calls = callRepository.findTop10ByOrderByStartTimeAsc();
            if (calls.isEmpty()) {
                break;
            }

            try {
                rabbitTemplate.convertAndSend(
                        rabbitConfig.getExchangeName(),
                        rabbitConfig.getRoutingKey(),
                        calls
                );
                callRepository.deleteAll(calls);
                log.info("Sent and deleted {} calls", calls.size());
            } catch (Exception e) {
                log.error("Error sending batch of calls to RabbitMQ", e);
                break;
            }
        }
    }

}
