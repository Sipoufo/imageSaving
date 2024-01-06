package com.experience.imageSaving.entities;

import com.experience.imageSaving.helpers.FileNameHelper;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Image", schema = "public")
public class Image extends BaseEntity {
    private String name;
    private String type;
    private long size;
    private String uuid;
    private String systemName;

    private byte[] data;

    /**
     * Create new Image class.
     *
     * @return new Image.
     */
    @Transient
    public static Image build() {
        String uuid = UUID.randomUUID().toString();
        Image image = new Image();
        Date now = new Date();
        image.setUuid(uuid);
        image.setCreatedDate(now);
        image.setUpdatedDate(now);
        image.setCreatedBy("default");
        image.setSystemName("default");
        image.setUpdatedBy("default");
        image.setStatus(true);
        return image;
    }

    @Transient
    public void setFiles(MultipartFile file) {
        setType(file.getContentType());
        setSize(file.getSize());
    }

    /**
     * Scale image data with given width and height.
     *
     * @param width  scale width
     * @param height scale height
     * @return scaled image byte array and change to class data.
     */
    @Transient
    public byte[] scale(int width, int height) throws Exception {

        if (width == 0 || height == 0)
            return data;

        ByteArrayInputStream in = new ByteArrayInputStream(data);

        try {
            BufferedImage img = ImageIO.read(in);

            java.awt.Image scaledImage = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
            BufferedImage imgBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imgBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(imgBuff, "jpg", buffer);
            setData(buffer.toByteArray());
            return buffer.toByteArray();

        } catch (Exception e) {
            throw new Exception("IOException in scale");
        }
    }

    /**
     * @param fileName - filename of the resources.
     *
     * @return inputstream for image
     * */
    private static InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = Image.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    /**
     * Generate no context image with `notfound.jpg` image in asset.
     *
     * @return create default image.
     */
    @Transient
    public static Image defaultImage() throws Exception {
        InputStream is = getResourceFileAsInputStream("notfound.jpg");
        String fileType = "image/jpeg";
        byte[] bdata = FileCopyUtils.copyToByteArray(is);
        Image image = new Image(null, fileType, 0, null, null, bdata);
        return image;
    }

    /**
     * Generate scaled no context image with `notfound.jpg` image in asset with
     * given width and height.
     *
     * @param width  scale width
     * @param height scale height
     * @return create scaled default image.
     */
    @Transient
    public static Image defaultImage(int width, int height) throws Exception {
        Image defaultImage = defaultImage();
        defaultImage.scale(width, height);
        return defaultImage;
    }

    /**
     * Generate scaled no context image with `notfound.jpg` image in asset with
     * given width and height.
     *
     * @param file   multipartfile data to build.
     * @param helper filenamehelper class to generate name.
     * @return return new Image class related with file.
     */
    @Transient
    public static Image buildImage(MultipartFile file, FileNameHelper helper) {
        String fileName = helper.generateDisplayName(file.getOriginalFilename());

        Image image = Image.build();
        image.setName(fileName);
        image.setFiles(file);

        try {
            image.setData(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
