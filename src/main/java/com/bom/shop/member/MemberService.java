package com.bom.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public void saveMember(String username, String password, String displayName) {

        var result = memberRepository.findByUsername(username);
        if(result.isPresent()){
            throw new IllegalArgumentException("Username already exists");
        }
        if(password.length() < 8){
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        Member member = new Member();
        member.setUsername(username);
        member.setPassword(passwordEncoder.encode(password));
        member.setDisplayName(displayName);
        memberRepository.save(member);
    }
}

