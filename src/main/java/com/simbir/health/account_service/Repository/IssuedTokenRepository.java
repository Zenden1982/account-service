package com.simbir.health.account_service.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simbir.health.account_service.Class.IssuedToken;

@Repository
public interface IssuedTokenRepository extends JpaRepository<IssuedToken, String> {
    List<IssuedToken> findAllByUserUsername(String username);
}
