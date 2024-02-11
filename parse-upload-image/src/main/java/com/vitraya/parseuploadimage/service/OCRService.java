package com.vitraya.parseuploadimage.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitraya.parseuploadimage.dto.ImageData;
import com.vitraya.parseuploadimage.repositories.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.swing.text.AttributeSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.text.StyleConstants.isBold;

@Slf4j
@Service
public class OCRService {

    @Autowired
    private ImageRepository imageRepository;

    public void extractTextFromImage(MultipartFile imageFile, String fileName) throws IOException {
        File tempImageFile = File.createTempFile("temp", ".png");
        imageFile.transferTo(tempImageFile);
        Tesseract tesseract = new Tesseract();
        //path to ocr tesseract executable
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        tesseract.setLanguage("eng");
        String extractedText = "";
        try {
            extractedText =  tesseract.doOCR(tempImageFile);
        } catch (TesseractException ex) {
            log.error("Error occurred while extracting the text {}", ex.getMessage());
            throw new RuntimeException("Error extracting text from image.");
       } finally {
            tempImageFile.delete();
        }

        String boldWordsJson = extractBoldWords(extractedText);

        updateDb(fileName, extractedText, boldWordsJson);


    }

    public String extractBoldWords(String text) {
        List<String> boldWords = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\*\\*(.*?)\\*\\*");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String boldWord = matcher.group(1);
            boldWords.add(boldWord);
        }

        String jsonString = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonString = objectMapper.writeValueAsString(boldWords);
        } catch (JsonProcessingException ex) {
            log.error("Unable to parse list to string {}", ex.getMessage());
            throw new RuntimeException("Parsing exception");
        }
        return jsonString;
    }

    public void updateDb(String fileName, String extractedText, String boldWordsJson) {
        try {
            ImageData imageData = new ImageData();
            imageData.setFileName(fileName);
            imageData.setExtractedText(extractedText.substring(1));
            imageData.setBoldWords(boldWordsJson);
            imageRepository.save(imageData);
        } catch (Exception ex) {
            log.error("unable to store the parsed image date to database, {}", ex.getMessage());
            throw ex;
        }
    }
}
