package jaemin.imgboard.controller;

import jaemin.imgboard.dto.ImageDto;
import jaemin.imgboard.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/")
    public String home() {
        return "form/home";
    }

    @PostMapping("/upload")
    public String uploadImage(@ModelAttribute ImageDto dto) throws Exception {
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
