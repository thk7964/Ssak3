package com.example.ssak3.domain.cart.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "carts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id", unique = true)
    private User user;

    public Cart(User user) {
        this.user = user;
    }

}
