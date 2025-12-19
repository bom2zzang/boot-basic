package com.bom.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;


    // Lombok 없이 등록
//    @Autowired
//    public ItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    @GetMapping("/list")
    String list(Model model){

        List<Item> result = itemRepository.findAll();
        System.out.println(result.toString());

        model.addAttribute("items", result);
        var a = new Item();
        System.out.println(a.title);

        return "list.html";
    }

}
