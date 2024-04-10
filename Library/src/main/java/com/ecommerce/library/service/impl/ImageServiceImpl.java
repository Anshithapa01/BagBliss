package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Image;
import com.ecommerce.library.repository.ImageRepository;
import com.ecommerce.library.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Image> findProductImages(long id) {
        return imageRepository.findImageBy(id);
    }

    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }
}
