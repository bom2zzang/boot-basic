package com.bom.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;


    @GetMapping("/list")
    String list(Model model){
        List<Item> result = itemRepository.findAll();
        System.out.println(result.toString());
        model.addAttribute("items", result);
        return "list";
    }

    @GetMapping("/write")
    String write(){
        return "write";
    }

    @PostMapping("/add")
    String addPost(Item item){
        itemRepository.save(item);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model){

        Optional<Item> result = itemRepository.findById(id);
        if(result.isPresent()){
            System.out.println(result.get());
            model.addAttribute("item", result.get());
            return "detail";
        }else{
            return "list";
        }
    }

}
