package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.OfferDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Offer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.CategoryRepository;
import com.ecommerce.library.repository.OfferRepository;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
    private OfferRepository offerRepository;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository,
                            ProductRepository productRepository,
                            CategoryRepository categoryRepository) {
        this.offerRepository = offerRepository;
        this.productRepository=productRepository;
        this.categoryRepository=categoryRepository;
    }

    @Override
    public void saveOffer(OfferDto offerDto) {
        Offer offer=new Offer();
        offer.setProduct(offerDto.getProduct());
        offer.setOfferName(offerDto.getOfferName());
        offer.setDescription(offerDto.getDescription());
        offer.setOfferPercentage(offerDto.getOfferPercentage());
        offer.setCategory(offerDto.getCategory());
        offer.setOfferType(offerDto.getOfferType());
        offer.setActive(false);
        offerRepository.save(offer);

    }

    public OfferServiceImpl() {
        super();
    }

    @Override
    public Offer findById(Long id) {
        return offerRepository.getReferenceById(id);

    }

    @Override
    public void updateOffer(OfferDto offerDto) {
        Offer offer=offerRepository.getReferenceById(offerDto.getId());

        offer.setOfferName(offerDto.getOfferName());
        offer.setDescription(offerDto.getDescription());
        offer.setOfferPercentage(offerDto.getOfferPercentage());



        offerRepository.save(offer);

    }

    @Override
    public List<Offer> findAllOffer() {
        return offerRepository.findAll();
    }

    @Override
    public void enableOffer(Long id) {
        Offer offer=offerRepository.getReferenceById(id);
        offer.setActive(true);
        offerRepository.save(offer);
        if(offer.getOfferType().equals("Product Offer")) {
            Product product = productRepository.getReferenceById(offer.getProduct().getId());
            double costprice = product.getCostPrice();
            int offerPercentage = offer.getOfferPercentage();
            double offers = costprice * offerPercentage / 100;
            product.setSalePrice(costprice - offers);
            productRepository.save(product);
        }
        else{
            Category category=categoryRepository.getReferenceById(offer.getCategory().getId());
            String categories=category.getName();
            List<Product> products=productRepository.findAllByCategory(categories);
            for(Product product:products){
                double costprice = product.getCostPrice();
                int offerPercentage = offer.getOfferPercentage();
                double offers = costprice * offerPercentage / 100;
                product.setSalePrice(costprice - offers);
                productRepository.save(product);
            }
        }
    }

    @Override
    public void disableOffer(Long id) {
        Offer offer=offerRepository.getReferenceById(id);
        offer.setActive(false);
        offerRepository.save(offer);
        if(offer.getOfferType().equals("Product Offer")) {
            Product product = productRepository.getReferenceById(offer.getProduct().getId());
            product.setSalePrice(product.getCostPrice());
            productRepository.save(product);
        }
        else{
            Category category=categoryRepository.getReferenceById(offer.getCategory().getId());
            String categories=category.getName();
            List<Product> products=productRepository.findAllByCategory(categories);
            for(Product product:products){
                product.setSalePrice(product.getCostPrice());
                productRepository.save(product);
            }

        }
    }
}
