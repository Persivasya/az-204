package com.chekotovsky.azstorage;

import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file)
        throws IOException {
        storageService.uploadFile(
            "mycontainer",
            file.getOriginalFilename(),
            file.getInputStream()
        );
        return storageService.getFileUrl(
            "mycontainer",
            file.getOriginalFilename()
        );
    }

    @GetMapping("/get-url")
    public String downloadFile(@RequestParam("file") String file) {
        return storageService.getFileUrl("mycontainer", file);
    }

    @PostMapping("/create-container")
    public String createContainer(@RequestParam("container") String container) {
        storageService.createContainer(container);
        return "Container created successfully";
    }
}
