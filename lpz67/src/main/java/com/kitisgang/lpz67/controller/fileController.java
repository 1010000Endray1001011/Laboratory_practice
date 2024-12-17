package com.kitisgang.lpz67.controller;
import com.kitisgang.lpz67.dto.fileContentResponse;
import com.kitisgang.lpz67.dto.fileInfoResponse;
import com.kitisgang.lpz67.service.fileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class fileController {

    private final fileService fileService;

    public fileController(fileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<fileInfoResponse>> listFiles(@RequestParam(defaultValue = "/") String path) {
        return ResponseEntity.ok(fileService.listFiles(path));
    }

    @GetMapping("/read")
    public ResponseEntity<fileContentResponse> readFile(@RequestParam String path) {
        return ResponseEntity.ok(fileService.readFileContent(path));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String path) {
        Resource resource = fileService.loadFileAsResource(path);
        String filename = resource.getFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}

