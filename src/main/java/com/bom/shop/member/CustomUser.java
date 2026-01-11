package com.bom.shop.member;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {
    @Getter
    private final Long id;
    @Getter
    private final String displayName;

    public CustomUser(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            String displayName,
            Long id
    ) {
        super(username, password, authorities);
        this.displayName = displayName;
        this.id = id;
    }

}
