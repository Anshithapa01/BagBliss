package com.ecommerce.customer.Customer.controller;


import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller

public class ShoppingCartController {
    ShoppingCartService shopCartService;

    ProductService productService;
    CustomerService customerService;
    @Autowired
    public ShoppingCartController(ShoppingCartService shopCartService, ProductService productService,
                                  CustomerService customerService) {
        this.shopCartService = shopCartService;
        this.productService = productService;
        this.customerService = customerService;
    }

    @GetMapping("/cart")
    public String showCart(Model model, Principal principal, HttpSession session){
        if (principal==null){
            return "redirect:/login";
        }
        String username=principal.getName();
        List<ShoppingCart> shoppingCart=shopCartService.findShoppingCartByCustomer(username);

        double total1=shopCartService.grandTotal(username);
        double total=Math.round(total1 * 100.0) / 100.0;
        double shippingFee=shopCartService.shippingFee(username);
        double finalTotal=shopCartService.finalGrandTotal(username);
        model.addAttribute("total",total);
        model.addAttribute("shippingFee",shippingFee);
        model.addAttribute("finalTotal",finalTotal);
        model.addAttribute("title", "Cart");
        model.addAttribute("shoppingCart",shoppingCart);
//        session.setAttribute("totalItems", shoppingCart.getTotalItems());

        return "cart";
    }



    @GetMapping("/addToCart")
    public String showAddToCart(@RequestParam("productId")Long id,
                                CustomerDto customerDto,
                                Principal principal,
                                Model model,
                                HttpSession session)
    {
        if(principal==null){
            return "redirect:/login";
        }
        int quantity=1;
        String username=principal.getName();
        Customer customer=customerService.findByEmail(username);
        ShoppingCart shoppingCart =shopCartService.addItemToCart(id,quantity,customer.getCustomer_id());
        model.addAttribute("shoppingCart", shoppingCart);
        return "redirect:/cart";

    }


    @PostMapping("/updateQuantity")
    public ResponseEntity<String> updateQuantity(@RequestParam("cartId") Long cartId,
                                                 @RequestParam("action") String action,
                                                 @RequestParam("productId") Long productId,
                                                 Principal principal) {
        ShoppingCart cart=shopCartService.findById(cartId);
        String username=principal.getName();
        if (action.equals("add")) {
            shopCartService.increment(cartId, productId);
        } else if (action.equals("remove")) {
            shopCartService.decrement(cartId);
        }
        int quantity=cart.getQuantity();
        double total=cart.getTotalPrice();
        double grandTotal=shopCartService.grandTotal(username);
        double shippingFee=shopCartService.shippingFee(username);
        double finalTotal=shopCartService.finalGrandTotal(username);
        String updatedHtmlContent = "<span name=\"quantity\" id=\"quantity\" th:text=\"${cart.quantity}\">" +
                quantity+"</span>";
        updatedHtmlContent +="<td class=\"text-right\" data-title=\"Cart\" >\n" +
                "<span th:text=\"${cart.totalPrice}\">₹" +total+
                " </span>\n</td>";
        updatedHtmlContent +="<td class=\"text-right\" data-title=\"Cart\" >\n" +
                "<span th:text=\"${cart.totalPrice}\">₹" +total+
                " </span>\n</td>";
        updatedHtmlContent +="<td class=\"cart_total_amount\">" +
                "<span class=\"font-lg fw-900 text-brand\" th:id=\"'grandTotal_' + ${cart.id}\" th:text=\"${total}\" >₹" +
                grandTotal+"</span></td>";
        updatedHtmlContent +="<td class=\"cart_total_amount\">" +
                "<span class=\"font-lg fw-900 text-brand\" th:id=\"'grandTotal_' + ${cart.id}\" th:text=\"${total}\" >₹" +
                shippingFee+"</span></td>";
        updatedHtmlContent +="<td class=\"cart_total_amount\"><strong>" +
                "<span class=\"font-xl fw-900 text-brand\" id=\"finalTotal\" th:text=\"${total}\" >₹" +
                finalTotal+"</span></strong></td>";


        return ResponseEntity.ok()
                .header("quantity", String.valueOf(quantity))
                .header("totalPrice", String.valueOf(total))
                .header("grandTotal", String.valueOf(grandTotal))
                .header("shippingFee", String.valueOf(shippingFee))
                .header("finalTotal", String.valueOf(finalTotal))
                .body(updatedHtmlContent);
    }


    @GetMapping("/deleteCartItem")
    public String showDelete(@RequestParam("cartId")Long id,Principal principal){
        List<Customer> customerDto1=customerService.findAll();
        shopCartService.deleteById(id);
        return "redirect:/cart";
    }



    @GetMapping("/incrementQuantity")
    public String showQuantityIncrement(@RequestParam("cartId")Long id,@RequestParam("productId") Long pId){
        shopCartService.increment(id,pId);
        return "redirect:/cart";
    }




    @GetMapping("/decrementQuantity")
    public String showQuantityDecrement(@RequestParam("cartId")Long id){
        shopCartService.decrement(id);
        return "redirect:/cart";
    }


    @PostMapping("/updateUnitPrice")
    public ResponseEntity<String> updateUnitPrice(@RequestParam("cartId") Long cartId,
                                                  @RequestParam("newUnitPrice") Double newUnitPrice) {
        ShoppingCart cart = shopCartService.findById(cartId);
        double newTotal=newUnitPrice*cart.getQuantity();
        if (cart != null) {
            cart.setUnitPrice(newUnitPrice);
            cart.setTotalPrice(newTotal);
            shopCartService.save(cart);
            String updatedHtmlContent = "<td class=\"price\" data-title=\"Price\" " +
                    "th:id=\"'cartPrice_'+${cart.id}\" th:text=\"${cart.unitPrice}\"><span>₹" +
                    newUnitPrice+"</span></td>";
            return ResponseEntity.ok()
                    .header("newUnitPrice", String.valueOf(newUnitPrice))
                    .header("newTotal", String.valueOf(newTotal))
                    .body(updatedHtmlContent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
