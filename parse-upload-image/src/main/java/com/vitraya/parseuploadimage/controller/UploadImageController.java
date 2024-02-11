package com.vitraya.parseuploadimage.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitraya.parseuploadimage.dto.ImageData;
import com.vitraya.parseuploadimage.repositories.ImageRepository;
import com.vitraya.parseuploadimage.service.OCRService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UploadImageController {

    @Autowired
    private OCRService ocrService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            ocrService.extractTextFromImage(file, file.getOriginalFilename());
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException ex) {
            log.error("Error occurred while trying to upload the image {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        } catch (Exception ex) {
            log.error("Unexpected Error occurred while trying to upload the image {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }


}
