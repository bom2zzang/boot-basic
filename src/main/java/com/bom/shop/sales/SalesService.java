package com.bom.shop.sales;

import com.bom.shop.item.Item;
import com.bom.shop.item.ItemRepository;
import com.bom.shop.member.Member;
import com.bom.shop.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void saveOrder(Long memberId, Long itemId, Integer price, Integer count) {

        // 재고 빼기
        Optional<Item> result = itemRepository.findById(itemId);
        if (result.isPresent()){
            var item = result.get();
            item.setCount(item.getCount() - count);
            itemRepository.save(item);
        }

//        if (true){
//            throw new RuntimeException("에러이유");
//        }


        // 주문
        Sales sales = new Sales();
        sales.setItemId(itemId);
        sales.setPrice(price);
        sales.setCount(count);

        Member memberRef = memberRepository.getReferenceById(memberId);
        sales.setMember(memberRef);

        salesRepository.save(sales);
    }
}
