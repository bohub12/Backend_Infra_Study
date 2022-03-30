package jaemin.imgboard.controller;

import jaemin.imgboard.repository.ImageRepository;
import jaemin.imgboard.repository.LocalImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final LocalImageRepository imageRepository;

    @GetMapping
    @ResponseBody
    public String test() throws Exception {
        imageRepository.testInit();
        return "test";
    }
}
