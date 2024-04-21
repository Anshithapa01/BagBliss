package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.UserOtp;
import com.ecommerce.library.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request,HttpSession session, Authentication authentication){
        Object attribute=session.getAttribute("userLoginID");
        if(attribute!=null) {
            return "redirect:/home";
        }
        CustomerDto customerDto=new CustomerDto();
        if(customerDto.isBlocked())
            return "redirect:/login?blocked";



        return "login";
    }


    @GetMapping("/register")
    public String register(Model model){
        String email = (String) model.asMap().get("email");
        CustomerDto customerDto=new CustomerDto();
        customerDto.setEmail(email);
        model.addAttribute("customerDto",customerDto);
        return "register";
    }


    @PostMapping("/do-register")
    public String registerCustomer(@Valid @ModelAttribute("customerDto")
                                        CustomerDto customerDto,
                                        BindingResult result,
                                        Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                session.removeAttribute("error");
                model.addAttribute("customerDto", customerDto);
                return "register";
            }
            Customer customer=customerService.findByEmail(customerDto.getEmail());
            if(customer!=null){
                model.addAttribute("customerDto",customerDto);
                session.removeAttribute("success");
                session.setAttribute("error","Email has been registered");
                return "register";
            }
            if(customerDto.getPassword().equals(customerDto.getRepeatPassword())) {
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                customerService.saveCustomer(customerDto);
                session.removeAttribute("error");
                session.setAttribute("success", "Registered Successfully");
            } else{
                session.removeAttribute("success");
                session.setAttribute("error", "Password is not same");
                model.addAttribute("customerDto", customerDto);
                return "register";
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.removeAttribute("success");
            session.setAttribute("error", "Server is error, try again later!");
        }
        redirectAttributes.addAttribute("email", customerDto.getEmail());
        return "register";
    }




    @GetMapping("/verify-email")
    public String getForgotPassword(){

        return "verify-email";
    }



    @GetMapping("/verifyEmail")
    public String showVerifyEmail(){
        return "verify-email";
    }
    @GetMapping("/otpvalidation")
    public String showotpvalidationPage(Model model, HttpSession session){
        String email = (String) model.asMap().get("email");
        UserOtp userOTP = new UserOtp();
        userOTP.setEmail(email);
        session.setAttribute("email",email);
        model.addAttribute("userOTP",userOTP);
        return "verify-otp";
    }


    @GetMapping("/register/oauth2/code/google")
    public String googleOAuth2Callback() {
        return "home";
    }

}
