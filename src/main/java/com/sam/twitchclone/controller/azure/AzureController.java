package com.sam.twitchclone.controller.azure;

import com.sam.twitchclone.model.BaseResponse;
import com.sam.twitchclone.service.AzureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AzureController {
    private final AzureService azureService;
    @PostMapping("/v1/azure/upload")
    public ResponseEntity<BaseResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = azureService.uploadImage(file);
        return ResponseEntity.ok(BaseResponse.builder().message("File uploaded successfully : " + fileUrl).build());
    }
}

