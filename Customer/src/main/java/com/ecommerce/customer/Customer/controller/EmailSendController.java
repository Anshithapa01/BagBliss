package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.model.UserOtp;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.EmailService;
import com.ecommerce.library.service.OtpService;
import com.ecommerce.library.service.UserOtpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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
                if(session.getAttribute("token")!=null) {
                    Object token=session.getAttribute("token");
                    session.setAttribute("token",token);
                }
                session.setAttribute("message","otpsent");
                session.setAttribute("email",email);
                redirectAttributes.addFlashAttribute("email",email);
                session.removeAttribute("error");
                return "redirect:/otpvalidation";

            }else{
                return "redirect:/verifyEmail?error";
            }
        }else{
            return "redirect:/verifyEmail?existUser";
        }

    }
    @PostMapping("/sendEmailOTPLogin")
    public String sendEmailOTPLogin(HttpSession session, RedirectAttributes redirectAttributes) throws Exception {

        String email =(String) session.getAttribute("email");
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
                session.setAttribute("email",email);
                redirectAttributes.addFlashAttribute("email",email);
                session.removeAttribute("error");
                return "redirect:/otpvalidation";

            }else{
                return "redirect:/verifyEmail?error";
            }
        }else{
            return "redirect:/verifyEmail?existUser";
        }
    }

    @PostMapping("/validateOTP")
    public String validateOTP(@ModelAttribute("userOTP")UserOtp userOTPRequest, HttpSession session,
                              RedirectAttributes redirectAttributes, Model model){

        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/error";
        }
        UserOtp userOTP = userOTPService.findByEmail(email);
        if (userOTP != null) {
            if (passwordEncoder.matches(userOTPRequest.getOneTimePassword(), userOTP.getOneTimePassword())) {
                long currentTime = System.currentTimeMillis();
                long otpRequestedTime = userOTP.getOtpRequestedTime().getTime(); // Assuming getOtpRequestedTime() returns a Date object
                long timeDifference = currentTime - otpRequestedTime;
                if (timeDifference < 60000) { // Check if time difference is less than 1 minute (60,000 milliseconds)
                    session.setAttribute("email",email);
                    redirectAttributes.addFlashAttribute("email", userOTP.getEmail());
                    if (session.getAttribute("token")==null)
                        return "redirect:/register";
                    else{
                        Object token=session.getAttribute("token");
                        model.addAttribute("token",token);
                        return "redirect:/referral_signup";
                    }
                } else {
                    session.setAttribute("error","Sorry, the OTP has timed out. Please request a new one.");
                    return "redirect:/otpvalidation";
                }
            } else {
                session.setAttribute("error","The OTP entered is incorrect. Please try again.");
                return "redirect:/otpvalidation";
            }
        } else {
            return "redirect:/requestOTP?email=" + email;
        }
    }





    @PostMapping("/sendNotifyEmail")
    public String sendNotify(
            @RequestParam("product")String product
            , HttpSession session,
            RedirectAttributes redirectAttributes, Principal principal) throws Exception {
        String email = principal.getName();
        String status = emailService.sendSimpleMail(email, product);
        if (status.equals("success")) {
            session.setAttribute("message", "otpsent");
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/find-products";

        } else {
            return "redirect:/find-products?error";

        }
    }
}
