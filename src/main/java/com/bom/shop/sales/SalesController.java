package com.bom.shop.sales;

import com.bom.shop.member.CustomUser;
import com.bom.shop.member.Member;
import com.bom.shop.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SalesController {

    private final SalesRepository salesRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/order")
    @ResponseBody
    String order(Long itemId, Integer price, Integer count, Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();

        Sales sales = new Sales();
        sales.setItemId(itemId);
        sales.setPrice(price);
        sales.setCount(count);

        Member memberRef = memberRepository.getReferenceById(user.getId());
        sales.setMember(memberRef);
        salesRepository.save(sales);
        return "OK";
    }

    @GetMapping("/order/all")
    String getOrderAll(){
        List<Sales> result = salesRepository.findAll();
        System.out.println(result.get(0));
        return "order";
    }
}
