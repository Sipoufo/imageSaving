package com.experience.imageSaving.repositories;

import com.experience.imageSaving.entities.Image;
import com.experience.imageSaving.payload.ImageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByName(String fileName);

    Image findByUuid(String uuid);

    @Query(value = "select new com.experience.imageSaving.payload.ImageResponse(im.uuid, im.name, im.type, im.size) from com.experience.imageSaving.entities.Image im where im.status=true", nativeQuery = false)
    List<ImageResponse> findAllImageResponse();
}
