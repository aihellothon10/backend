package com.example.memoservice.domain.analizer;

import com.example.memoservice.domain.analizer.model.JobType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzeRequestForm {
    private String query;
    private List<String> links;
    private JobType jobType;
    private List<MultipartFile> images;
}
