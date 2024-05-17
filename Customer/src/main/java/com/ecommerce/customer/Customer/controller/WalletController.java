package com.ecommerce.customer.Customer.controller;

import com.ecommerce.customer.Customer.config.CustomerDetails;
import com.ecommerce.library.model.Wallet;
import com.ecommerce.library.model.WalletHistory;
import com.ecommerce.library.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class WalletController {

    private WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/wallet")
    public String showWallet(Principal principal, Model model, Authentication authentication){
        if(principal==null){
            return "redirect:/loginPage";
        }
        CustomerDetails customUser= (CustomerDetails) authentication.getPrincipal();
        Wallet wallet=walletService.findByCustomerByUsername(customUser.getUsername());
        List<WalletHistory> walletHistories=walletService.findAllByCustomerName(customUser.getUsername());
        String name=customUser.getUsername();
        model.addAttribute("wallet",wallet);
        model.addAttribute("walletHistory",walletHistories);
        model.addAttribute("name",name);

        return "wallet";
    }
}
