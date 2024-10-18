package com.simbir.health.account_service.Class;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class IssuedToken {

    @Id
    private String jti;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isRevoked = false;
}
