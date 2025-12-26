package com.bom.shop.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    public void saveItem(String title, Integer price, String imageUrl) {

        if(price < 0){
            throw new IllegalArgumentException("Price cannot be negative");
        }
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setImageUrl(imageUrl);
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

    public void updateItem(Long id, String title, Integer price) throws Exception {
       if(price < 0){
           throw new IllegalArgumentException("Price cannot be negative");
        }
       if(title.length() > 100){
           throw new IllegalArgumentException("Title length exceeds limit");
       }
            Item item = new Item();
            item.setId(id);
            item.setTitle(title);
            item.setPrice(price);
            itemRepository.save(item);
        }
}
