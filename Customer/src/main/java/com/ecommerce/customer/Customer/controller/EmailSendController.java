package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.model.UserOtp;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.EmailService;
import com.ecommerce.library.service.OtpService;
import com.ecommerce.library.service.UserOtpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
public class EmailSendController {
   private OtpService otpService;
    private EmailService emailService;
    private UserOtpService userOTPService;

    private CustomerService usersSevice;
    private BCryptPasswordEncoder passwordEncoder;

    public EmailSendController(OtpService otpService,
                               EmailService emailService,
                               UserOtpService userOTPService,
                               CustomerService usersSevice,
                               BCryptPasswordEncoder passwordEncoder) {
        this.otpService = otpService;
        this.emailService = emailService;
        this.userOTPService = userOTPService;
        this.usersSevice = usersSevice;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/sendVerificationEmailOtp")
    public String sendVerificationEmailOtp(
            @RequestParam("email")String email
            , HttpSession session,
            RedirectAttributes redirectAttributes) throws Exception {

        if(usersSevice.findByEmail(email)==null){
            String otp = otpService.generateOTP();
            if(!userOTPService.existsByEmail(email)){
                // new email verification
                UserOtp userOTP =new UserOtp();
                userOTP.setEmail(email);
                userOTP.setOneTimePassword(passwordEncoder.encode(otp));
                userOTP.setCreatedAt(new Date());
                userOTP.setOtpRequestedTime(new Date());
                userOTP.setUpdateOn(new Date());
                try{
                    userOTPService.saveOrUpdate(userOTP);
                }catch(Exception e){
                    e.printStackTrace();
                    throw new Exception("Couldn't finish OTP verification process"+ HttpStatus.BAD_REQUEST);
                }

            }else{
                //code to delete all data related to this email id
                UserOtp userOTP=userOTPService.findByEmail(email);
                userOTP.setOneTimePassword(passwordEncoder.encode(otp));
                userOTP.setOtpRequestedTime(new Date());
                userOTP.setUpdateOn(new Date());
                try{
                    userOTPService.saveOrUpdate(userOTP);
                }catch(Exception e){
                    e.printStackTrace();
                    throw new Exception("Couldn't finish OTP verification process");
                }
            }
            String status = emailService.sendSimpleMail(email,otp);
            if(status.equals("success")){
                session.setAttribute("message","otpsent");
                redirectAttributes.addFlashAttribute("email",email);
                return "redirect:/otpvalidation";

            }else{
                return "redirect:/verifyEmail?error";
            }
        }else{
            return "redirect:/verifyEmail?existUser";
        }

    }
    @PostMapping("/sendEmailOTPLogin")
    public String sendEmailOTPLogin(
            @RequestParam("email")String email
            , HttpSession session,
            RedirectAttributes redirectAttributes) throws Exception {
        if(usersSevice.findByEmail(email)!=null){
            String otp = otpService.generateOTP();
            UserOtp userOTP = userOTPService.findByEmail(email);
            if(userOTP!=null){
                userOTP.setOneTimePassword(passwordEncoder.encode(otp));
                userOTP.setOtpRequestedTime(new Date());
                userOTP.setUpdateOn(new Date());
            }else{
                userOTP = new UserOtp();
                userOTP.setEmail(email);
                userOTP.setOneTimePassword(passwordEncoder.encode(otp));
                userOTP.setCreatedAt(new Date());
                userOTP.setOtpRequestedTime(new Date());
                userOTP.setUpdateOn(new Date());
            }
            try{
                userOTPService.saveOrUpdate(userOTP);
            }catch(Exception e){
                e.printStackTrace();
                throw new Exception("Send OTP.Please try after some time...");
            }
            String status = emailService.sendSimpleMail(email,otp);
            if(status.equals("success")){
                session.setAttribute("message","otpsent");
                redirectAttributes.addFlashAttribute("email",email);
                return "redirect:/forgotPasswordOTPLogin";

            }else{
                return "redirect:/forgotpassword?error";
            }
        }else{
            return "redirect:/forgotpassword?error";
        }
    }
    @PostMapping("/validateOTP")
    public String validateOTP(@ModelAttribute("userOTP")UserOtp userOTPRequest, HttpSession session,
                              RedirectAttributes redirectAttributes){
        // Retrieve email from session
        String email = (String) session.getAttribute("email");

        // Ensure email is not null
        if (email == null) {
            // Handle the case where email is null
            // For example, redirect to an error page or handle it appropriately
            return "redirect:/error";
        }

        // Fetch user OTP based on the retrieved email
        UserOtp userOTP = userOTPService.findByEmail(email);

        // Check if userOTP is not null before accessing its properties
        if (userOTP != null) {
            if (passwordEncoder.matches(userOTPRequest.getOneTimePassword(), userOTP.getOneTimePassword())) {
                // Navigate to signup page
                redirectAttributes.addFlashAttribute("email", userOTP.getEmail());
                return "redirect:/register";
            } else {
                // Handle incorrect OTP error
                return "redirect:/otpvalidation?error";
            }
        } else {
            // Handle the case where no UserOtp object is found for the provided email
            // For example, redirect to a page to request OTP again or handle it appropriately
            return "redirect:/requestOTP?email=" + email; // Redirect to a page to request OTP again
        }

    }

}
