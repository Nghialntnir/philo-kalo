package com.nlnt.philokalo_server.service;

import java.util.regex.Pattern;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryCleanupService {

    private static final Pattern VERSION = Pattern.compile("^v\\d+/");
    private final Cloudinary cloudinary;

    @Async
    public void deleteAsync(String secureUrl) {
        String publicId = publicIdFromUrl(secureUrl);
        if (publicId == null) {
            return;
        }
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
        } catch (Exception ex) {
            log.error("Could not delete Cloudinary asset asynchronously, publicId={}", publicId, ex);
        }
    }

    private String publicIdFromUrl(String secureUrl) {
        if (secureUrl == null || secureUrl.isBlank()) {
            return null;
        }
        int uploadIndex = secureUrl.indexOf("/upload/");
        if (uploadIndex < 0) {
            return null;
        }
        String path = secureUrl.substring(uploadIndex + "/upload/".length());
        String[] segments = path.split("/");
        int start = 0;
        while (start < segments.length && (segments[start].startsWith("c_")
                || segments[start].startsWith("w_")
                || segments[start].startsWith("h_")
                || segments[start].startsWith("q_")
                || segments[start].startsWith("f_"))) {
            start++;
        }
        StringBuilder value = new StringBuilder();
        for (int i = start; i < segments.length; i++) {
            if (i == start && VERSION.matcher(segments[i]).find()) {
                continue;
            }
            if (value.length() > 0) {
                value.append('/');
            }
            value.append(segments[i]);
        }
        int extension = value.lastIndexOf(".");
        if (extension > 0) {
            value.delete(extension, value.length());
        }
        return value.length() == 0 ? null : value.toString();
    }
}
