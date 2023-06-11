package com.imhero.user.domain;

import com.imhero.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    private String delYn;

    public static User of(String email, String password, String username, String delYn) {
        return new User(email, password, username, delYn);
    }

    private User(String email, String password, String username, String delYn) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.delYn = delYn;
    }

}
