package com.bom.shop.comment;


import com.bom.shop.member.CustomUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    @GetMapping("/comments/{id}")
    @ResponseBody
    public List<CommentDto> comments(@PathVariable Long id){

        var list = commentRepository.findByItemId(id); // itemId로 DB조회

        return list.stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getUsername(),
                        c.getContent(),
                        c.getItemId()))
                .toList();

    }

    @PostMapping("/comment")
    @ResponseBody
    String postComment(@RequestParam String content, @RequestParam Long itemId, Authentication auth,        @RequestHeader(value="X-Requested-With", required=false) String xrw
    ) {
        CustomUser user = (CustomUser) auth.getPrincipal();
        var data = new Comment();
        data.setContent(content);
        data.setUsername(user.getUsername());
        data.setItemId(itemId);
        System.out.println(data);
        commentRepository.save(data);
        if ("XMLHttpRequest".equals(xrw)) return "OK";

        return "OK";
    }

}

@Getter
class CommentDto {
    private Long id;
    private String username;
    private String content;
    private Long parentId;

    public CommentDto(Long id, String username, String content, Long parentId) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.parentId = parentId;
    }

}