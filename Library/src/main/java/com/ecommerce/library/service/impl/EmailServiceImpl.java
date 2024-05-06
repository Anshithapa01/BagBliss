package com.ecommerce.library.service.impl;

import com.ecommerce.library.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private JavaMailSender javaMailSender;

    //@Value("${spring.mail.username}")
    private String sender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }



    private String generateEmailOtpVarificationMessage(String otp) {
        String message ="Hello Customer "
                +"For email verification you need to enter OTP.One Time Password for verification is: "+otp
                +" Note: this OTP is set to expire in 1 minutes.";
        return message;
    }


    @Override
    public String sendSimpleMail(String email, String otp) {
        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            String message=generateEmailOtpVarificationMessage(otp);
            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(email);
            mailMessage.setText(message);
            mailMessage.setSubject("Email Verification for BagBliss");

            // Sending the mail
            javaMailSender.send(mailMessage);

            return "success";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @Override
    public String generateNotify(String product) {
        String message ="Hello Customer "
                +"Thank you for choosing BagBliss and placing an order with us!" +
                " We wanted to let know that the item you purchased " +product+
                " is currently out of stock." +
                " We have received a high volume of orders and while we strive to maintain a good supply of products, sometimes we make an error." +
                "  We take full responsibility for this and truly apologize for any inconvenience it may have caused." +
                " As soon as this order is back in stock, we will." +
                "Thank you!";
        return message;
    }


    @Override
    public String sendNotifyMail(String email, String product) {

        try {
            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            String message=generateEmailOtpVarificationMessage(product);
            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(email);
            mailMessage.setText(message);
            mailMessage.setSubject("Notify Soon!");

            // Sending the mail
            javaMailSender.send(mailMessage);

            return "success";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

}
