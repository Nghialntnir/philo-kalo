package com.nlnt.philokalo_server.controller;

import com.nlnt.philokalo_server.dto.response.ApiResponse;
import com.nlnt.philokalo_server.dto.response.CloudinaryUploadResponse;
import com.nlnt.philokalo_server.service.CloudinaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nghia
 */
@RestController
@RequestMapping("/cloudinary")
@Slf4j
public class CloudinaryController {

    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping("/{userId}/upload-avatar")
    ApiResponse<String> uploadAvatar(@PathVariable String userId,
            @RequestPart(value = "avatar") MultipartFile request) {

        return ApiResponse.<String>builder()
                .result(cloudinaryService.uploadAvatar(request, userId))
                .build();
    }
}
