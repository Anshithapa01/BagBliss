package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.model.WishList;
import com.ecommerce.library.service.WishListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class WishListControll {
    private WishListService wishListService;

    public WishListControll(WishListService wishListService) {
        this.wishListService = wishListService;
    }
    @GetMapping("/wishList")
    public String showwishList(Principal principal, Model model){
        if(principal==null){
            return "redirect:/loginPage";
        }
        String username=principal.getName();
        List<WishList> wishLists=wishListService.findWishlistByCustomer(username);
        model.addAttribute("wishList",wishLists);

        return "wish-list";
    }
    @GetMapping("/addToWishList")
    public String showAddtowishList(@RequestParam("productId")Long id, Principal principal){
        if(principal==null){
            return "redirect:/loginPage";
        }
        String username=principal.getName();
        wishListService.addToWishList(username,id);
        return "redirect:/wishList";
    }
    @GetMapping("/removeFromWishList")
    public String showRemoveFromWishList(@RequestParam("wishListId")Long id){

        wishListService.DeletefromWishList(id);
        return "redirect:/wishList";
    }

}
