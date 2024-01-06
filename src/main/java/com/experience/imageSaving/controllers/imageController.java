package com.experience.imageSaving.controllers;

import com.experience.imageSaving.entities.Image;
import com.experience.imageSaving.helpers.FileNameHelper;
import com.experience.imageSaving.payload.ImageResponse;
import com.experience.imageSaving.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class imageController {
    @Autowired
    private ImageService imageService;

    private FileNameHelper fileHelper = new FileNameHelper();

    /**
     * Get all images information without data.
     *
     * @return return list of all images information.
     */
    @GetMapping("/images")
    public ResponseEntity<List<ImageResponse>> getAllImageInfo() throws Exception {
        List<ImageResponse> imageResponses = imageService.findAllImageResponse();
        return ResponseEntity.ok().body(imageResponses);
    }

    /**
     * Upload single file to database.
     *
     * @param file file data
     * @return return saved image info with ImageResponse class.
     */
    @PostMapping("/upload")
    public ImageResponse uploadSingleFile(@RequestParam("file") MultipartFile file) {
        Image image = Image.buildImage(file, fileHelper);
        imageService.save(image);
        return new ImageResponse(image);
    }

    /**
     * Upload multiple files to database.
     *
     * @param files files data
     * @return return saved images info list with ImageResponse class.
     */
    @PostMapping("/uploads")
    public List<ImageResponse> uploadMultiFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files).stream().map(file -> uploadSingleFile(file)).collect(Collectors.toList());
    }

    /**
     * get Image by name. If image is null return default image from asset.
     *
     * @param name the name of image
     * @return valid image or default image
     */
    public Image getImageByName(String name) throws Exception {
        Image image = imageService.findByFileName(name);
        if (image == null) {
            return Image.defaultImage();
        }
        return image;
    }

    /**
     * get scaled Image by name, width and height. If image is null return default
     * image from asset.
     *
     * @param name   the name of image
     * @param width  width size of image
     * @param height height size of image
     * @return valid scaled image or default scaled image
     */
    public Image getImageByName(String name, int width, int height) throws Exception {
        Image image = imageService.findByFileName(name);
        if (image == null) {
            Image defImage = Image.defaultImage();
            defImage.scale(width, height);
            return defImage;
        }
        image.scale(width, height);
        return image;
    }

    /**
     * get Image by uuid. If image is null return default image from asset.
     *
     * @param uuid the uuid of image
     * @return valid image or default image
     */
    public Image getImageByUuid(String uuid) throws Exception {
        Image image = imageService.findByUuid(uuid);
        if (image == null) {
            return Image.defaultImage();
        }
        return image;
    }

    /**
     * get scaled Image by uuid, width and height. If image is null return default
     * image from asset.
     *
     * @param uuid   the uuid of image
     * @param width  width size of image
     * @param height height size of image
     * @return valid scaled image or default scaled image
     */
    public Image getImageByUuid(String uuid, int width, int height) throws Exception {
        Image image = imageService.findByUuid(uuid);
        if (image == null) {
            Image defImage = Image.defaultImage();
            defImage.scale(width, height);
            return defImage;
        }
        image.scale(width, height);
        return image;
    }

    /**
     * Sends valid or default image bytes with given fileName pathVariable.
     *
     * @param fileName
     * @return return valid byte array
     */
    @GetMapping("/show/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws Exception {
        Image image = getImageByName(fileName);
        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getType())).body(image.getData());
    }

    /**
     * Sends valid or default image bytes with given fileName or uuid request
     * params.
     *
     * @param name image name
     * @param uuid image uuid
     * @return return valid byte array
     */
    @GetMapping("/show")
    public ResponseEntity<byte[]> getImageWithRequestParam(@RequestParam(required = false, value = "uuid") String uuid,
                                                           @RequestParam(required = false, value = "name") String name) throws Exception {

        if (uuid != null) {
            Image image = getImageByUuid(uuid);
            return ResponseEntity.ok().contentType(MediaType.valueOf(image.getType())).body(image.getData());
        }
        if (name != null) {
            return getImage(name);
        }
        Image defaultImage = Image.defaultImage();
        return ResponseEntity.ok().contentType(MediaType.valueOf(defaultImage.getType()))
                .body(defaultImage.getData());

    }
    /**
     * Sends valid or default scaled image bytes with given fileName.
     *
     * @param fileName image name
     * @param width    image width
     * @param height   image height
     * @return return valid byte array
     */
    @GetMapping("/show/{width}/{height}/{fileName:.+}")
    public ResponseEntity<byte[]> getScaledImage(@PathVariable int width, @PathVariable int height,
                                                 @PathVariable String fileName) throws Exception {
        Image image = getImageByName(fileName, width, height);
        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getType())).body(image.getData());
    }
}
