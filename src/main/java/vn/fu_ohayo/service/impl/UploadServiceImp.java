package vn.fu_ohayo.service.impl;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import jakarta.servlet.ServletContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.fu_ohayo.service.UploadService;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.List;

@Service
public class UploadServiceImp implements UploadService {

    private final ServletContext servletContext;

    private final YouTube  youtubeService;


    public UploadServiceImp(ServletContext servletContext, YouTube youtubeService) {
        this.servletContext = servletContext;
        this.youtubeService = youtubeService;
    }

    @Override
    public String handleUploadFile( MultipartFile file, String targetFolder) {
        // don't upload file
        if (file.isEmpty()) {
            return "";
        }
        // relative path: absolute path
        String rootPath = this.servletContext.getRealPath("/resources");
        String finalName = "";
        try {
            byte[] bytes = file.getBytes();

            File dir = new File(rootPath + File.separator + targetFolder);
            if (!dir.exists())
                dir.mkdirs();

            // Create the file on server
            finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);
            // uuid - monggoDB - 100 năm mới cso khả năng trùng
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalName;
    }

    @Override
    public String handleUploadFileToYoutube(MultipartFile file, String subjectName) throws GeneralSecurityException, IOException {
        // Convert MultipartFile to File
        String originalExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        File tempFile = File.createTempFile("video", originalExtension);
        file.transferTo(tempFile);

        Video video = new Video();

        // add the snippet object property
        Calendar cal = Calendar.getInstance();
        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(subjectName);
        snippet.setDescription("""
        This video is about the subject: %s
        Uploaded on: %s
        """.formatted(subjectName, cal.getTime()));

        //add status
        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus("public"); // or "private" or "unlisted"
        video.setStatus(status);

        video.setSnippet(snippet);

        // set the video file content

        InputStreamContent mediaContent = new InputStreamContent(
                "video/*", new BufferedInputStream(new FileInputStream(tempFile)));
        mediaContent.setLength(tempFile.length());

        // Create YouTube service

        YouTube.Videos.Insert request = youtubeService.videos().insert(List.of("snippet,status"), video, mediaContent);

        Video response = request.execute();

        tempFile.delete();

        return "https://www.youtube.com/watch?v=" + response.getId();
    }


}
