package com.capstone_design.mobile_forensics.web.ResponseDTO;

import lombok.*;

@Data
@Builder @ToString @AllArgsConstructor @NoArgsConstructor
public class WholeData {
    private Long images;
    private Long pictureTaken;
    private GPS gps;
    private AppUsage appUsage;
    private Cache cache;
    @Data @Builder @AllArgsConstructor @NoArgsConstructor @ToString
    public static class GPS {
        private Long metadata;
        private Long wifi;
    }
    @Data @Builder @AllArgsConstructor @NoArgsConstructor @ToString
    public static class AppUsage {
        private Long cloud;
        private Long camera;
    }
    @Data @Builder @AllArgsConstructor @NoArgsConstructor @ToString
    public static class Cache {
        private Long soda;
        private Long mybox;
    }
}
