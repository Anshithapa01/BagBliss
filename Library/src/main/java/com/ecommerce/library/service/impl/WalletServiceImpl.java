package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.Wallet;
import com.ecommerce.library.model.WalletHistory;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.repository.WalletHistoryRepository;
import com.ecommerce.library.repository.WalletRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.WalletService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {
    private WalletRepository walletRepository;
    private WalletHistoryRepository walletHistoryRepository;
    private OrderRepository orderRepository;
    private CustomerService customerService;
    private CustomerRepository customerRepository;


    public WalletServiceImpl(WalletRepository walletRepository, WalletHistoryRepository walletHistoryRepository,
                             OrderRepository orderRepository, CustomerService customerService, CustomerRepository customerRepository) {
        this.walletRepository = walletRepository;
        this.walletHistoryRepository = walletHistoryRepository;
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.customerRepository=customerRepository;
    }

    @Override
    public void addToRefundAmount(Long orderId) {
        Order order=orderRepository.getReferenceById(orderId);
        Wallet wallet=walletRepository.findByCustomer(order.getCustomer().getCustomer_id());


        if(wallet!=null) {
            double newBalance =Math.round((wallet.getBalance() + order.getGrandTotalPrize())* 100.0)/ 100.0;
            wallet.setBalance(newBalance);
        }
        else{
            wallet=new Wallet();
            wallet.setCustomer(order.getCustomer());
            double newBalance =Math.round(order.getGrandTotalPrize()* 100.0)/ 100.0;
            wallet.setBalance(newBalance);
        }
        walletRepository.save(wallet);



        WalletHistory walletHistory=new WalletHistory();
        walletHistory.setWallet(wallet);
        walletHistory.setOrder(order);
        walletHistory.setAmount(order.getGrandTotalPrize());
        walletHistory.setTrasactionStatus("Credit");
        walletHistory.setTrasactionType("Refund amount");
        walletHistory.setTransationDate(new Date());
        walletHistoryRepository.save(walletHistory);
    }

    @Override
    public Wallet findByCustomer(Long customerId) {
        return walletRepository.findByCustomer(customerId);
    }

    @Override
    public Wallet findByCustomerByUsername(String username) {
        return walletRepository.findByCustomerByUsername(username);
    }

    @Override
    public List<WalletHistory> findAllByCustomer(Long id) {
        return walletHistoryRepository.findAllByCustomer(id);
    }

    @Override
    public List<WalletHistory> findAllByCustomerName(String username) {
        return walletHistoryRepository.findAllByCustomerName(username);
    }

//    @Override
//    public void setWalletAmount(Long id,double amount) {
//        Wallet wallet=walletRepository.findByCustomer(id);
//        wallet.setBalance(wallet.getBalance()-amount);
//        walletRepository.save(wallet);
//        WalletHistory walletHistory=new WalletHistory();
//        walletHistory.setWallet(wallet);
//        walletHistory.setAmount(amount);
//        walletHistory.setTrasactionStatus("Debit");
//        walletHistory.setTrasactionType("Buy Product");
//        walletHistory.setTransationDate(new Date());
//        walletHistoryRepository.save(walletHistory);
//    }

    @Override
    public void setWalletAmount(String username,double amount) {
        Wallet wallet=walletRepository.findByCustomerByUsername(username);
        double newBalance =Math.round((wallet.getBalance()-amount)* 100.0)/ 100.0;
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        WalletHistory walletHistory=new WalletHistory();
        walletHistory.setWallet(wallet);
        walletHistory.setAmount(amount);
        walletHistory.setTrasactionStatus("Debit");
        walletHistory.setTrasactionType("Buy Product");
        walletHistory.setTransationDate(new Date());
        walletHistoryRepository.save(walletHistory);
    }

    @Override
    public void addWalletToReferalEarn(Long customerId) {
        Customer customer=customerRepository.getReferenceById(customerId);
        Wallet wallet=walletRepository.findByCustomer(customerId);
        customer.setReferrals(customer.getReferrals() + 1);
        if(wallet!=null){
            wallet.setBalance(wallet.getBalance()+100);
        }
        else{
            wallet=new Wallet();
            wallet.setCustomer(customer);
            wallet.setBalance(100);
        }
        walletRepository.save(wallet);
        WalletHistory walletHistory=new WalletHistory();
        walletHistory.setWallet(wallet);

        walletHistory.setAmount(100);
        walletHistory.setTrasactionStatus("Credit");
        walletHistory.setTrasactionType("Referal Earning");
        walletHistory.setTransationDate(new Date());
        walletHistoryRepository.save(walletHistory);
    }
}
