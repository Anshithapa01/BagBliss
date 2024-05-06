package com.ecommerce.library.service;

public interface EmailService {
    public String sendSimpleMail(String email, String otp);

    String generateNotify(String product);

    String sendNotifyMail(String email, String product);
}
