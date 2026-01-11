package com.bom.shop.sales;

import com.bom.shop.member.Member;
import com.bom.shop.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveOrder(Long memberId, Long itemId, Integer price, Integer count) {
        Sales sales = new Sales();
        sales.setItemId(itemId);
        sales.setPrice(price);
        sales.setCount(count);

        Member memberRef = memberRepository.getReferenceById(memberId);
        sales.setMember(memberRef);

        salesRepository.save(sales);
    }
}
