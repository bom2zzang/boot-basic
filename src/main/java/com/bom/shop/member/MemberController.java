package com.bom.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/login")
    String login(){
        var result = memberRepository.findByUsername("bom2zzang");
        System.out.println(result.get());
        return "login";
    }

    @GetMapping("/my-page")
    String mypage(Authentication auth){
        if(auth != null && auth.isAuthenticated()){
            return "mypage";
        }else{
            return "redirect:/login";
        }

    }


}
