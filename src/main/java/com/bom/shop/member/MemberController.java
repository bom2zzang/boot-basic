package com.bom.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/join")
    String join(){
        return "join";
    }

    @PostMapping("/join")
    String postJoin(String id, String password, String name){
        memberService.saveMember(id, password, name);
        return "redirect:/list";
    }
}
