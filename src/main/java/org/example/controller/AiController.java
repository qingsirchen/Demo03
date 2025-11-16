package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.OpenAiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {
    private final OpenAiService openAiService;
    @PostMapping("/unit-test")
    public String createUnitTest(@RequestBody String javaSource){
        return openAiService.generateUnitTest(javaSource);
    }
}
