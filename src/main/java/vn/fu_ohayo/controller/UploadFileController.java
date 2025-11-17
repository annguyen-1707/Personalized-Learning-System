package vn.fu_ohayo.controller;

import jakarta.persistence.Table;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.service.UploadService;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/uploadfile")
public class UploadFileController {
    private final UploadService uploadService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public UploadFileController(UploadService uploadService, OAuth2AuthorizedClientService authorizedClientService) {
        this.uploadService = uploadService;
        this.authorizedClientService =  authorizedClientService;
    }

    @PostMapping
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("targetFolder") String targetFolder) {
            String url = uploadService.handleUploadFile(file, targetFolder) ;
            return ApiResponse.<String>builder()
                    .code("200")
                    .status("success")
                    .message("File image uploaded successfully")
                    .data(url)
                    .build();
        }

        @PostMapping("/youtube")
        public ApiResponse<String> uploadFileYoutube(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("lessonName") String lessonName) throws GeneralSecurityException, IOException {


            String videoUrl = uploadService.handleUploadFileToYoutube(file, lessonName);
                return ApiResponse.<String>builder()
                    .code("200")
                    .status("success")
                    .message("File youtube uploaded successfully")
                    .data(videoUrl)
                    .build();
        }
}

