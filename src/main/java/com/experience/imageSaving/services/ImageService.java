package com.experience.imageSaving.services;

import com.experience.imageSaving.entities.Image;
import com.experience.imageSaving.payload.ImageResponse;

import java.util.List;

public interface ImageService {

    public Image save(Image image);

    public Image findByFileName(String fileName);

    public Image findByUuid(String uuid);

    public List<ImageResponse> findAllImageResponse();
}
