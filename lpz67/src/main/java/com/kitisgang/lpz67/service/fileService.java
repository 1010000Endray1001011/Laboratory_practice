package com.kitisgang.lpz67.service;
import com.kitisgang.lpz67.dto.fileContentResponse;
import com.kitisgang.lpz67.dto.fileInfoResponse;
import com.kitisgang.lpz67.exception.fileOperationException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class fileService {

    private final Path rootLocation;

    public fileService() {
        this.rootLocation = Paths.get("C:\\Users\\Rei\\documents\\Docum\\word\\");

    }

    public List<fileInfoResponse> listFiles(String directory) {
        try {
            Path dirPath = Path.of(rootLocation + directory);

            return Files.list(dirPath)
                    .map(path -> new fileInfoResponse(
                            path.getFileName().toString(),
                            Files.isDirectory(path),
                            getFileSize(path),
                            getLastModified(path)
                    ))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new fileOperationException("Failed to list files", e);
        }
    }

    public fileContentResponse readFileContent(String filePath) {
        try {
            Path path = Path.of(rootLocation + filePath);
            String content;
            String fileName = path.getFileName().toString();
            String fileExtension = getFileExtension(fileName);

            switch (fileExtension.toLowerCase()) {
                case "txt":
                    content = Files.readString(path);
                    break;
                case "docx":
                    content = readDocxFile(path);
                    break;
                default:
                    throw new fileOperationException("Unsupported file type: " + fileExtension);
            }

            return new fileContentResponse(
                    fileName,
                    content,
                    Files.size(path),
                    Files.getLastModifiedTime(path).toMillis()
            );
        } catch (IOException e) {
            throw new fileOperationException("Failed to read file", e);
        }
    }

    private String readDocxFile(Path path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path.toFile());
             XWPFDocument document = new XWPFDocument(fis)) {
            StringBuilder content = new StringBuilder();
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                content.append(paragraph.getText()).append("\n");
            }
            return content.toString();
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1);
    }

    public Resource loadFileAsResource(String filePath) {
        try {
            Path path = Path.of(rootLocation + filePath);

            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new fileOperationException("File not found: " + filePath);
            }
        } catch (IOException e) {
            throw new fileOperationException("Failed to load file", e);
        }
    }


    private long getFileSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            return 0L;
        }
    }

    private long getLastModified(Path path) {
        try {
            return Files.getLastModifiedTime(path).toMillis();
        } catch (IOException e) {
            return 0L;
        }
    }
}