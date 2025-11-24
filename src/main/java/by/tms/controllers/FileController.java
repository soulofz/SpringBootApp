package by.tms.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {
    private final Path ROOT_FILE_PATH = Paths.get("data");


    @PostMapping("/upload")
    public ResponseEntity<HttpStatusCode> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty() || file == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Files.copy(file.getInputStream(), ROOT_FILE_PATH.resolve(file.getOriginalFilename()));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Path pathToFile = ROOT_FILE_PATH.resolve(filename);
        try {
            Resource resource = new UrlResource(pathToFile.toUri());
            if (resource.exists() || resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            }
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        Path pathToFile = ROOT_FILE_PATH.resolve(filename);
        File file = new File(pathToFile.toString());
        if (file.exists()) {
            if (file.delete()){
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllFileNames() {
        try {
            List<String> fileNames = Files.walk(ROOT_FILE_PATH)
                    .filter(path -> !path.equals(ROOT_FILE_PATH))
                    .map(Path::toString)
                    .map(path -> path.replaceAll("data\\\\", ""))
                    .toList();
            return new ResponseEntity<>(fileNames, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
