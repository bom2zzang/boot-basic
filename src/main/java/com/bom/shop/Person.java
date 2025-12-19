package com.bom.shop;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Person {

    @Id
    private Long id;

    private Integer age;
    private String name;

    public void plusOne(){
        this.age = this.age+1;
    }

    public void setAge(Integer a){
        if(  a  > 0 && a < 100){
            this.age = a;
        }
    }
}
