package com.capstone_design.mobile_forensics.file;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageFileDTO {
    private String imageName;
    private byte[] imageByte;
    private String type; //camera, trash, soda, drive
    private LocalDateTime timestamp;
}
