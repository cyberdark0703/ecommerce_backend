package com.duc.ecommerce.controller;

import com.duc.ecommerce.dto.request.ProductCreateRequest;
import com.duc.ecommerce.dto.request.ProductUpdateRequest;
import com.duc.ecommerce.dto.response.ProductResponse;
import com.duc.ecommerce.entity.Product;
import com.duc.ecommerce.service.ProductService;
import lombok.AccessLevel;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping ("/product")
@FieldDefaults (level = AccessLevel.PRIVATE)
public class ProductController {
    @Autowired
    ProductService service;

    @PostMapping("/create")
    ProductResponse create (@RequestBody ProductCreateRequest request){
        return service.create(request);
    }

    @GetMapping("/read_one/{id}")
    ProductResponse read (@PathVariable String id){
        return service.read(id);
    }

    @GetMapping("/findbypricebetweenandcategory")
    List<ProductResponse> findByPriceBetweenAndCategory (@RequestParam BigDecimal minPrice,
                                                         @RequestParam BigDecimal maxPrice,
                                                         @RequestParam String categoryName){
        return service.findByPriceBetweenAndCategory(minPrice,maxPrice,categoryName);
    }
    @PutMapping("/update/{id}")
    ProductResponse update (@PathVariable String id, @RequestBody ProductUpdateRequest request){
        return service.update(id,request);
    }

    @DeleteMapping("/delete/{id}")
    void delete(@PathVariable String id){
         service.delete(id);
    }

    // nhận image
    @PostMapping("/upload-test")
    public String uploadTest(
            @RequestParam String productId,
            @RequestParam MultipartFile file)
    throws IOException {
        if (file.isEmpty()) {
            return "File is empty";
        }

        if (!"image/jpeg".equals(file.getContentType())
                && !"image/png".equals(file.getContentType())) {

            return "Invalid file type";
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            return "File is too large";
        }

        String uploadDir = "E:\\ecommerce\\upload";
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + "\\" + fileName;

        file.transferTo(new File(filePath));

        service.saveImageName(productId, fileName);

        System.out.println("FILE NAME = " + file.getOriginalFilename());
        System.out.println("NEW FILE NAME = " + fileName);
        System.out.println("FILE SIZE = " + file.getSize());
        System.out.println("FILE TYPE = " + file.getContentType());
        System.out.println("FILE EMPTY = " + file.isEmpty());

        return "Upload success";
    }
    // lấy image
    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName)
            throws IOException {

        String uploadDir = "E:\\ecommerce\\upload";
        File file = new File(uploadDir, imageName);

        Resource resource = new UrlResource(file.toURI());

        String contentType = Files.probeContentType(file.toPath());

        return ResponseEntity.ok()
                //.contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
    // cập nhật image
    @PutMapping("/image/{productId}")
    public String updateImage(@PathVariable String productId,
                              @RequestParam MultipartFile file)
            throws IOException {
        Product product = service.readEntity(productId);

        String oldFileName = product.getImageName();

        String uploadDir = "E:\\ecommerce\\upload";

        String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        String newFilePath = uploadDir + "\\" + newFileName;

        file.transferTo(new File(newFilePath));

        File oldFile = new File(uploadDir, oldFileName);

        if (oldFile.exists()) {
            oldFile.delete();
        }

        service.saveImageName(productId, newFileName);

        return "Update image success";
    }

}
