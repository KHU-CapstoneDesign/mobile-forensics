package com.capstone_design.mobile_forensics.file.api;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class SafeSearchService {

    public SafeSearchAnnotation analyzeImage(byte[] imageData) throws IOException {
        try (ImageAnnotatorClient visionClient = ImageAnnotatorClient.create()) {
            ByteString byteString = ByteString.copyFrom(imageData);

            Image image = Image.newBuilder().setContent(byteString).build();

            Feature feature = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feature)
                    .setImage(image)
                    .build();

            BatchAnnotateImagesResponse response = visionClient.batchAnnotateImages(List.of(request));
            AnnotateImageResponse imageResponse = response.getResponses(0);

            if(imageResponse.hasSafeSearchAnnotation()) {
                return imageResponse.getSafeSearchAnnotation();
            } else {
                throw new RuntimeException("No SafeSearchAnnotation available for the image.");
            }
        }
    }
}
