package com.backend.domain.menu.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Controller
@Tag(name = "Controller 이름", description = "Controller 설명")
public class MenuController {
}
