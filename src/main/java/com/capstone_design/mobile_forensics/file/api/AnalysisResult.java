package com.capstone_design.mobile_forensics.file.api;

import com.google.cloud.vision.v1.Likelihood;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AnalysisResult {
    private Likelihood adult;
    private Likelihood violence;
    private Likelihood medical;
    private Likelihood racy;
}
