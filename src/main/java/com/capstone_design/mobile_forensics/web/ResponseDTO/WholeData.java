package com.capstone_design.mobile_forensics.web.ResponseDTO;

import lombok.*;

@Data
@Builder @ToString @AllArgsConstructor @NoArgsConstructor
public class WholeData {
    private Long images;
    private Long pictureTaken;
    private GPS gps;
    private Long appUsage;
    private Long cache;
    @Data @Builder @AllArgsConstructor @NoArgsConstructor @ToString
    public static class GPS {
        private Long metadata;
        private Long wifi;
    }
}
/*
* {
	"images": 5,
	"pictureTaken": 5,
	"gps": {
					"metadata": 5,
					"wifi": 5
					},
	"appUsage": 5,
	"cache": 5
}
*
* */