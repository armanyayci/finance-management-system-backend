package com.sau.swe.notification.listener;

import com.sau.swe.dto.TwoFactorEmailMessage;
import com.sau.swe.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TwoFactorEmailListener {

    private final EmailService emailService;

    @RabbitListener(queues = "two-factor-email-queue")
    public void handleTwoFactorEmailMessage(TwoFactorEmailMessage message) {
        try {
            log.info("Received two-factor email message for: {}", message.getEmail());
            emailService.sendTwoFactorCode(message.getEmail(), message.getUsername(), message.getCode());
            log.info("Two-factor email processed successfully for: {}", message.getEmail());
        } catch (Exception e) {
            log.error("Error processing two-factor email message for: {}", message.getEmail(), e);
            throw e;
        }
    }
} 