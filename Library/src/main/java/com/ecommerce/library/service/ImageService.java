package com.ecommerce.library.service;

import com.ecommerce.library.model.Image;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ImageService {
    List<Image> findProductImages(long id);

    List<Image> findAll();


    void deleteImageByName(String name);
}
