package com.capstone_design.mobile_forensics.file.api;

import com.capstone_design.mobile_forensics.GoogleInitializer;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SafeSearchService {

    @Autowired
    private GoogleInitializer googleInitializer;

    public SafeSearchAnnotation analyzeImage(byte[] imageData) throws IOException {
        try {
            if (imageData == null || imageData.length == 0) {
                throw new IllegalArgumentException("Image data is null or empty.");
            }
            ImageAnnotatorClient visionClient = googleInitializer.getVisionClient();
            System.out.println("visionClient = " + visionClient);
            ArrayList<AnnotateImageRequest> requests = new ArrayList<>();

            ByteString byteString = ByteString.copyFrom(imageData);

            Image image = Image.newBuilder().setContent(byteString).build();

            Feature feature = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feature)
                    .setImage(image)
                    .build();
            System.out.println("request = " + request);
            requests.add(request);

            BatchAnnotateImagesResponse response = visionClient.batchAnnotateImages(requests);
            System.out.println("response = " + response);

            if (response.getResponsesCount() == 0 || response.getResponses(0) == null) {
                throw new RuntimeException("Empty response from Vision API.");
            }
            AnnotateImageResponse imageResponse = response.getResponses(0);
            System.out.println("imageResponse = " + imageResponse);
            if (imageResponse.hasError()) {
                log.error("Vision API error: " + imageResponse.getError().getMessage());
                throw new RuntimeException("Vision API returned an error: " + imageResponse.getError().getMessage());
            }

            if(imageResponse.hasSafeSearchAnnotation()) {
                return imageResponse.getSafeSearchAnnotation();
            } else {
                throw new RuntimeException("No SafeSearchAnnotation available for the image.");
            }
        } catch (ApiException e) {
            log.error("Vision API returned an error: ", e);
            throw new RuntimeException("Vision API request failed.");
        } catch(Exception e) {
            log.error("Unexpected Error: ", e);
            throw new RuntimeException("An Unexpected error occurred while analyzing the image.");
        }
    }

}
