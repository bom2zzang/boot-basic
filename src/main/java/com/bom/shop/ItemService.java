package com.bom.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    public void saveItem(String title, Integer price){

        if(price < 0){
            throw new IllegalArgumentException("Price cannot be negative");
        }
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);
    }

    public Optional<Item> getItem(Long id) throws Exception {
        Optional<Item> result =  itemRepository.findById(id);
        if(result.isPresent()){
            return result;
        }else{
            throw new Exception();
        }
    }
}
