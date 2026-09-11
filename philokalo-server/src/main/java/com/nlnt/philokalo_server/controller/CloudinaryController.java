package com.nlnt.philokalo_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nlnt.philokalo_server.dto.response.ApiResponse;
import com.nlnt.philokalo_server.exception.AppException;
import com.nlnt.philokalo_server.exception.ErrorCode;
import com.nlnt.philokalo_server.model.User;
import com.nlnt.philokalo_server.repository.UserRepository;
import com.nlnt.philokalo_server.service.CloudinaryService;

import lombok.extern.slf4j.Slf4j;

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

    @Autowired
    UserRepository userRepository;

    @PostMapping("/{userId}/upload-avatar")
    ApiResponse<String> uploadAvatar(
            @PathVariable String userId, @RequestPart(value = "avatar") MultipartFile request) {

        String avatarUrl = cloudinaryService.uploadAvatar(request, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);

        return ApiResponse.<String>builder().result(avatarUrl).build();
    }
}
