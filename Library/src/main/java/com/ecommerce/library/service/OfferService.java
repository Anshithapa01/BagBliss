package com.ecommerce.library.service;

import com.ecommerce.library.dto.OfferDto;
import com.ecommerce.library.model.Offer;

import java.util.List;

public interface OfferService {
    void saveOffer(OfferDto offerDto);
    List<Offer> findAllOffer();
    void enableOffer(Long id);
    void disableOffer(Long id);
    Offer findById(Long id);
    void updateOffer(OfferDto offerDto);
}
