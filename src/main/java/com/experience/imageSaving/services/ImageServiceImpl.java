package com.experience.imageSaving.services;

import com.experience.imageSaving.entities.Image;
import com.experience.imageSaving.payload.ImageResponse;
import com.experience.imageSaving.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService{
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image save(Image image) throws NullPointerException {
        if (image == null)
            throw new NullPointerException("Image Data NULL");
        return imageRepository.save(image);
    }

    @Override
    public Image findByFileName(String fileName) {
        return this.imageRepository.findByName(fileName);
    }

    @Override
    public Image findByUuid(String uuid) {
        return this.imageRepository.findByUuid(uuid);
    }

    @Override
    public List<ImageResponse> findAllImageResponse() {
        return this.imageRepository.findAllImageResponse();
    }
}
