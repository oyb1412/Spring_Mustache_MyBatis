package kr.co.myproject.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.myproject.Util.CustomFileUploadProperties;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final CustomFileUploadProperties fileProps;

    public String saveFile(MultipartFile file) {
        String uploadDir = fileProps.getUploadDir(); 
        String originalName = file.getOriginalFilename();
        String savedName = UUID.randomUUID() + "_" + originalName;

        try {
            Path savePath = Paths.get(uploadDir, savedName);
            file.transferTo(savePath.toFile());
            return savedName;
        } catch (IOException e) {
            e.printStackTrace(); 
            return null; 
        }
    }

    public void deleteImagesFromHtml(String htmlContent, String rootPath) {
        Document doc = Jsoup.parse(htmlContent);
        Elements images = doc.select("img");

        for (Element img : images) {
            String src = img.attr("src");
            if (src != null && src.startsWith("/files/")) {
                File file = new File(rootPath + src);
                if (file.exists()) file.delete();
            }
        }
    }
}

