package com.sau.swe.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendTwoFactorCode(String email, String username, String code) {
        try {
            String subject = "Your Two-Factor Authentication Code";
            String body = buildTwoFactorEmailBody(username, code);
            
            sendHtmlEmail(email, subject, body);
            log.info("Two-factor authentication code sent successfully to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send two-factor authentication code to: {}", email, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private void sendHtmlEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
    }

    private String buildTwoFactorEmailBody(String username, String code) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;">
                    <h2 style="color: #2c3e50; text-align: center;">Two-Factor Authentication</h2>
                    <p>Hello <strong>%s</strong>,</p>
                    <p>You are attempting to log in to your Finance Management account. For security purposes, please use the verification code below:</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <div style="display: inline-block; background-color: #f8f9fa; padding: 20px; border-radius: 8px; border: 2px dashed #007bff;">
                            <h1 style="color: #007bff; margin: 0; font-size: 36px; letter-spacing: 8px;">%s</h1>
                        </div>
                    </div>
                    <p><strong>Important:</strong></p>
                    <ul>
                        <li>This code will expire in 5 minutes</li>
                        <li>Do not share this code with anyone</li>
                        <li>If you didn't request this code, please ignore this email</li>
                    </ul>
                    <p style="margin-top: 30px; font-size: 14px; color: #666; text-align: center;">
                        This is an automated message from Finance Management System. Please do not reply to this email.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(username, code);
    }
} 