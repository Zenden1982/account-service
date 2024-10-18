package com.simbir.health.account_service.Class;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPair {

    private String refreshToken;

    private String accessToken;
}
