package com.nexign.brt.service;

import com.nexign.brt.dto.CdrDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CdrListenerService {

    private final CdrProcessingService callService;

    @RabbitListener(queues = "${rabbitmq.queue}")
    private void receiveCalls(@Payload List<CdrDto> calls) {
        log.info("Received {} CDR messages", calls.size());
        callService.process(calls);
    }

}
