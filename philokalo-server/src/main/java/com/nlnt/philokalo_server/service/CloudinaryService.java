package com.nlnt.philokalo_server.service;

import com.nlnt.philokalo_server.dto.response.CloudinaryUploadResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nghia
 */
public interface CloudinaryService {

    String uploadAvatar(MultipartFile file, String userId);

    CloudinaryUploadResponse uploadPhoto(MultipartFile file, String userId);

    void deleteFile(String publicId);
}
