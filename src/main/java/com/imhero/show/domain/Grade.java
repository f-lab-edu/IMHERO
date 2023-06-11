package com.imhero.show.domain;

import lombok.Getter;

@Getter
public enum Grade {

    A("A", 99_000),
    R("R", 149_000),
    VIP("VIP", 199_000);

    private String name;
    private int price;

    Grade(String name, int price) {
        this.name = name;
        this.price = price;
    }
}