package com.example.backend.source;


import com.example.backend.CommonResult;
import com.example.backend.user.User;
import com.example.backend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/source")
public class SourceController {

    @Value("${file.pic-dir}")
    private String pictureDir;
    @Value("${file.vid-dir}")
    private String videoDir;

    @Autowired
    private UserService userService;
    @Autowired
    private SourceMapper sourceMapper;
//    private List<String> images = new ArrayList<>();
//    private List<String> videos = new ArrayList<>();

    @PostMapping("/uploadImage")
    public CommonResult<?> uploadImage(@RequestHeader("Authorization") String accessToken,
                                       @RequestParam("files") MultipartFile[] files,
                                       @RequestParam("description") String description
    ) throws Exception {
        String token = accessToken.substring(7);
        User user = userService.getUserInfoByToken(token);
        for (MultipartFile file : files) {
            try {
                String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                System.out.println("文件名为" + uniqueFileName);
                File uploadedFile = new File(pictureDir + uniqueFileName);
                file.transferTo(uploadedFile);

                Picture picture = new Picture();
                picture.setUid(user.getUid());
                picture.setDescription(description);
                picture.setDate(new Date());
                picture.setUrl(uniqueFileName);
                sourceMapper.addPicture(picture);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }
        }
        return CommonResult.success(200);
    }

    @PostMapping("/uploadVideo")
    public CommonResult<?> uploadVideo(@RequestHeader("Authorization") String accessToken,
                                       @RequestParam("files") MultipartFile[] files,
                                       @RequestParam("description") String description) throws Exception {
        System.out.println("视频的文件夹路径为：" + videoDir);
        String token = accessToken.substring(7);
        User user = userService.getUserInfoByToken(token);
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            try {
                String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                System.out.println("文件名为" + uniqueFileName);
                File uploadedFile = new File(videoDir + uniqueFileName);
                file.transferTo(uploadedFile);

                Video video = new Video();
                video.setUid(user.getUid());
                video.setDescription(description);
                video.setDate(new Date());
                video.setUrl(uniqueFileName);
                sourceMapper.addVideo(video);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }
        }
        return CommonResult.success(200);
    }

    @GetMapping("/images")
    public CommonResult<List<String>> getImages(@RequestHeader("Authorization") String accessToken) throws Exception {
        System.out.println("start");
        String token = accessToken.substring(7);
        User user = userService.getUserInfoByToken(token);
        List<String> urls = sourceMapper.getAllPictureUrls(user.getUid());
        System.out.println(urls);
        return CommonResult.success(urls);
    }

    @GetMapping("/videos")
    public CommonResult<List<String>> getVideos(@RequestHeader("Authorization") String accessToken) throws Exception {
        String token = accessToken.substring(7);
        User user = userService.getUserInfoByToken(token);
        List<String> urls = sourceMapper.getAllVideoUrls(user.getUid());
        System.out.println("vidoes\n"+urls);
        return CommonResult.success(urls);
    }
//    @GetMapping("/images")
//    public List<String> getImages() {
//    }
//
//    @GetMapping("/videos")
//    public List<String> getVideos() {
//    }

//    private ResponseEntity<String> uploadFile(MultipartFile file, String type) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("File is empty");
//        }
//
//        try {
//            // Determine the file extension
//            String extension = getFileExtension(file.getOriginalFilename());
//            if (extension == null) {
//                return ResponseEntity.badRequest().body("Invalid file extension");
//            }
//
//            // Create directory if it doesn't exist
//            File dir = new File(uploadDir + File.separator + type);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            // Save the file
//            String filePath = dir.getAbsolutePath() + File.separator + file.getOriginalFilename();
//            file.transferTo(new File(filePath));
//
//            // Add to the corresponding list
//            if (type.equals("images")) {
//                images.add(filePath);
//            } else if (type.equals("videos")) {
//                videos.add(filePath);
//            }
//
//            return ResponseEntity.ok("File uploaded successfully");
//
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
//        }
//    }
//
//    private String getFileExtension(String fileName) {
//        if (fileName == null || fileName.lastIndexOf(".") == -1) {
//            return null;
//        }
//        return fileName.substring(fileName.lastIndexOf(".") + 1);
//    }
}

