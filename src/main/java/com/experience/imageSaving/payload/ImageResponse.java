package com.experience.imageSaving.payload;

import com.experience.imageSaving.entities.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
    private String uuid;
    private String name;
    private String type;
    private long size;

    public ImageResponse(Image image) {
        setUuid(image.getUuid());
        setName(image.getName());
        setType(image.getType());
        setSize(image.getSize());
    }
}
