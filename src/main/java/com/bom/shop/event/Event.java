package com.bom.shop.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;       // 이벤트 제목 (예: 선착순 A)
    private int limitCount;     // 총 수량 (예: 100개)
    private int remainCount;    // 남은 수량 (예: 99개...)

    public Event(String title, int limitCount) {
        this.title = title;
        this.limitCount = limitCount;
        this.remainCount = limitCount; // 처음엔 남은 수량 = 총 수량
    }

    // 비즈니스 로직: 재고 감소 (DDD 방식)
    public void decrease() {
        if (this.remainCount > 0) {
            this.remainCount--;
        }
    }
}