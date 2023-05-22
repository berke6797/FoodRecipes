package com.bilgeadam.service;

import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender javaMailSender;

    public void sendMail(RegisterMailModel registerMailModel) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        try {
            mailMessage.setFrom("${spring.mail.username}");
            mailMessage.setTo(registerMailModel.getEmail());
            mailMessage.setSubject("ACCOUNT ACTIVATION CODE");
            mailMessage.setText(registerMailModel.getUsername() + "kayıt başarıyla tamamlandı, giriş yapabilirsiniz...");
            mailMessage.setText("Activation Code : " + registerMailModel.getActivationCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        javaMailSender.send(mailMessage);
    }
}
