package com.bom.shop.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
            CustomUser user = (CustomUser) auth.getPrincipal(); // 타입캐스팅
            System.out.println("Welcome, " + user.displayName);
            return "mypage";
        }else{
            return "redirect:/login";
        }

    }

    @GetMapping("/user/1")
    @ResponseBody
    public MemberDto getUser(){
        var a = memberRepository.findById(1L);
        var result = a.get();
        var data = new MemberDto(result.getUsername(), result.getDisplayName());
        return data;
    }


}

@Getter
class MemberDto {
    String username;
    String displayName;
    MemberDto(String a, String b){ // 생성자로 깔끔하게 초기화
        this.username = a;
        this.displayName = b;
    }
}