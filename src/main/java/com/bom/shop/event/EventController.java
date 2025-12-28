package com.bom.shop.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller // 화면을 리턴해야 하므로 Controller 사용
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // 1. 화면 보여주기 (Thymeleaf)
    // 브라우저 주소창: http://localhost:8080/event-page
    @GetMapping("/event-page")
    public String eventPage(Model model) {
        List<Event> events = eventService.getEventList();
        model.addAttribute("events", events);
        return "event";
    }

    // 2. API: 선착순 참여 (AJAX 요청용)
    // 화면 이동 없이 데이터만 리턴해야 하므로 @ResponseBody 붙임
    @GetMapping("/apply")
    @ResponseBody
    public String apply(@RequestParam Long eventId, @RequestParam String userId) {
        // ID를 받아서 처리
        return eventService.applyEvent(eventId, userId);
    }
}