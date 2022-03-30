package jaemin.imgboard.controller;

import jaemin.imgboard.dto.ImageRenderDto;
import jaemin.imgboard.dto.ImageUploadDto;
import jaemin.imgboard.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("images", imageService.getImageIdList());
        return "form/home";
    }

    @GetMapping("/image/{fileId}")
    public @ResponseBody
    ResponseEntity<Resource> showImage(@PathVariable Long fileId) {
        ImageRenderDto dto = imageService.loadImages(fileId);

        Resource resource = dto.getResource();
        String filePath = dto.getFilePath();

        if (!resource.exists()) {
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders header = new HttpHeaders();
        try {
            header.add("Content-Type", Files.probeContentType(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Resource>(resource,header,HttpStatus.OK);
    }

    @PostMapping("/upload")
    public String uploadImage(@ModelAttribute ImageUploadDto dto) throws Exception {
        imageService.saveUploadedImage(dto);
        log.info("Controller : dto = {}", dto);
        return "redirect:/";
    }

    @GetMapping("/delete/{fieldId}")
    public String deleteImage(@PathVariable Long fieldId) throws Exception {
        imageService.deleteImage(fieldId);
        return "redirect:/";
    }
}