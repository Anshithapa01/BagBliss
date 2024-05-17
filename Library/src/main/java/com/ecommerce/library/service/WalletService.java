package com.ecommerce.library.service;

import com.ecommerce.library.model.Wallet;
import com.ecommerce.library.model.WalletHistory;

import java.util.List;

public interface WalletService {

    void addToRefundAmount(Long orderId);
    Wallet findByCustomer(Long username);

    Wallet findByCustomerByUsername(String username);

    List<WalletHistory> findAllByCustomer(Long id);

    List<WalletHistory> findAllByCustomerName(String username);

//    void setWalletAmount(Long id, double amount);

    void setWalletAmount(String username, double amount);

    void addWalletToReferalEarn(Long customerId);


}
