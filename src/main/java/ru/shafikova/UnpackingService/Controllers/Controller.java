package ru.shafikova.UnpackingService.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.shafikova.UnpackingService.Services.UnpackingService;

import java.io.File;
import java.io.IOException;
@RestController
@RequestMapping("/unpack")
public class Controller {
    private final UnpackingService unpackingService;

    @Autowired
    public Controller(UnpackingService unpackingService) {
        this.unpackingService = unpackingService;
    }

    @PostMapping()
    public ResponseEntity<Object> put(@RequestParam("file") MultipartFile file) throws IOException {
        File unpackedFile = unpackingService.unpack(file);

        System.out.println(unpackedFile.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + unpackedFile.getName())
                .body(new FileSystemResource(unpackedFile));
    }
}
