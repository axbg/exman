package com.web.poc1.service;

import com.web.poc1.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    int uploadDocument(MultipartFile document) throws CustomException;
}
