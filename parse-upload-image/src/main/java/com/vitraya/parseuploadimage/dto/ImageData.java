package com.vitraya.parseuploadimage.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "image_data_details")
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "extracted_text", length = 10000)
    private String extractedText;

    @Column(name = "bold_words")
    private String boldWords;
}
