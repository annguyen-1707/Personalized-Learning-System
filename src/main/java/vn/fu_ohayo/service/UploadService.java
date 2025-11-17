package vn.fu_ohayo.service;

import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.security.GeneralSecurityException;

public interface UploadService {
    String handleUploadFile(MultipartFile file, String targetFolder);

    String handleUploadFileToYoutube(MultipartFile file, String subjectName) throws GeneralSecurityException, IOException;
}
