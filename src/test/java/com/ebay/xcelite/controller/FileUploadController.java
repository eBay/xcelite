package com.ebay.xcelite.controller;

import com.ebay.xcelite.annotations.XlsParam;
import com.ebay.xcelite.model.UploadData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileUploadController {

    @PostMapping("/")
    public UploadData handleFileUpload(@XlsParam List<UploadData> data) {
        return data.stream()
                .filter(uploadData -> uploadData.getName().equals("Virginia"))
                .findFirst()
                .orElse(null);
    }
}
