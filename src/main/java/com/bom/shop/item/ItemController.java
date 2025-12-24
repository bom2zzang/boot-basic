package com.bom.shop.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;


    @GetMapping("/list")
    String list(Model model){
        List<Item> result = itemRepository.findAll();
        model.addAttribute("items", result);
        return "list";
    }

    @GetMapping("/write")
    String write(Authentication auth)

    {

        if(auth != null && auth.isAuthenticated()){
            return "redirect:/list";
        }else{
            return "write";
        }
    }

    @PostMapping("/add")
    String addPost(String title, Integer price){
        itemService.saveItem(title, price);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model) throws Exception {
        Optional<Item> item = itemService.getItem(id);
            model.addAttribute("item", item.get());
            return "detail";
    }

    @GetMapping("/update/{id}")
    String modify(@PathVariable Long id, Model model) throws Exception {
        Optional<Item> item = itemService.getItem(id);
        model.addAttribute("item", item.get());
        return "modify";
    }

    @PostMapping("/update")
    String update(Long id, String title, Integer price) throws Exception {
            itemService.updateItem(id, title, price);
        return "redirect:/list";
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        System.out.println("test1"+id);
        itemRepository.deleteById(id);
        return ResponseEntity.status(200).body("삭제완료");
    }

    @GetMapping("/list/page/{pageNo}")
    String getListPage(Model model, @PathVariable("pageNo") Integer PageNo){
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(PageNo-1,3));
        model.addAttribute("items", result);
        model.addAttribute("totalPages", result.getTotalPages());
        return "list";
    }


}
