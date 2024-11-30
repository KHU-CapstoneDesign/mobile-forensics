package com.capstone_design.mobile_forensics.file;

import com.capstone_design.mobile_forensics.file.api.AnalysisResult;
import com.capstone_design.mobile_forensics.file.api.SafeSearchResponse;
import com.capstone_design.mobile_forensics.file.api.SafeSearchService;
import com.capstone_design.mobile_forensics.log.LogProcessService;
import com.capstone_design.mobile_forensics.web.UserData;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService{

    @Autowired
    private LogProcessService logService;
    @Autowired
    private SafeSearchService safeSearchService;
    @Autowired
    private ImageFileRepository imageRepository;

    public void fileUpload(MultipartFile file, String parentDir) throws IOException {
        String fileName = file.getOriginalFilename();
        log.info("File received.\tFile Name = {}", fileName);

        // 파일 확장자 확인
        // -> 이미지 파일이면 데이터베이스 저장
        // -> 로그 파일이면 로그 파싱 후 저장
        switch (getFileExtension(fileName).toLowerCase()) {
            case "txt" :
                ResponseEntity response = logService.parseLogs(file);// 로그파일 처리
                break;
            case "jpeg" :
            case "jpg" :
            case "png" :
                processImageFile(file, parentDir); // 이미지파일 처리
                break;
            default :
                throw new IllegalArgumentException("Unsupported File Type: " + fileName);
        }
    }

    // 파일 확장자 추출
    public String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            throw new IllegalArgumentException("No file extension found for file: " + fileName);
        }
        return fileName.substring(lastIndex + 1);
    }

    // 파일 이름을 LocalDateTime으로 변환하는 메서드
    public LocalDateTime getTimestamp(String fileName) {
        // 파일 이름에서 .jpg 확장자 제거
        String baseFileName = StringUtils.stripFilenameExtension(fileName);

        // 파일 이름이 yyyyMMdd_hhmmss 형식이라면 이를 LocalDateTime으로 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        try {
            return LocalDateTime.parse(baseFileName, formatter);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public String determineImageType(String parentDir) {
        switch (parentDir) {
            case "/Camera":
                return "camera";
            case "/Trash":
                return "trash";
            case "/Soda_cache":
                return "soda";  // Soda_cache는 soda로 구분
            case "/MYBOX_cache":
                return "mybox";  // MYBOX_cache는 mybox로 구분
            default:
                return "unknown";  // 기본값
        }
    }

    // 이미지 파일 처리(저장)
    public ResponseEntity processImageFile(MultipartFile file, String parentDir) throws IOException {
        String filename = file.getOriginalFilename();
        LocalDateTime timestamp = getTimestamp(filename);
        String fileType = determineImageType(parentDir);

        BufferedImage resizeImage = resizeImage(file);
        try {
            byte[] imageBytes = getByteData(resizeImage);
            ImageFile imageFile = new ImageFile(null, file.getOriginalFilename(), imageBytes, fileType, timestamp);
            ImageFile saved = imageRepository.save(imageFile);
            log.info("Saved Image File = {}", saved);
            return new ResponseEntity(HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            throw new RuntimeException("Failed to Process Image File", e);
        }
    }
    private BufferedImage resizeImage(MultipartFile file) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        try {
            // Graphics2D로 리사이징
            BufferedImage resizedImage = new BufferedImage(450, 450, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(originalImage, 0, 0, 450, 450, null);
            graphics2D.dispose();
            return resizedImage;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to Resizing Image.");
        }
    }
    private byte[] getByteData(BufferedImage image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", stream);
        } catch(IOException e) {
            // This *shouldn't* happen with a ByteArrayOutputStream, but if it
            // somehow does happen, then we don't want to just ignore it
            throw new RuntimeException(e);
        }
        return stream.toByteArray();
        // ByteArrayOutputStreams don't need to be closed (the documentation says so)
    }
    @Override
    public List<SafeSearchResponse> analyzeAndRespondImages(UserData userData, String parentDir) throws IOException {

        List<ImageFile> imageFiles = imageRepository.findAllWithin30Minutes(userData.getDateTime(), parentDir);

        List<SafeSearchResponse> responses = new ArrayList<>();

        if(!imageFiles.isEmpty()) {
            for (ImageFile imageFile : imageFiles) {
                SafeSearchAnnotation safeSearchAnnotation = safeSearchService.analyzeImage(imageFile.getImageByte());
                System.out.println("safeSearchAnnotation = " + safeSearchAnnotation.toString());
                String base64ImageData = Base64.getEncoder().encodeToString(imageFile.getImageByte());

                SafeSearchResponse response = new SafeSearchResponse(
                        imageFile.getImageName(),
                        imageFile.getTimestamp(),
                        imageFile.getType(),
                        base64ImageData,
                        new AnalysisResult(
                                safeSearchAnnotation.getAdult(),
                                safeSearchAnnotation.getViolence(),
                                safeSearchAnnotation.getMedical(),
                                safeSearchAnnotation.getRacy()
                        )
                );
                responses.add(response);
            }
            return responses;
        } else {
            return responses;
        }
    }


}
