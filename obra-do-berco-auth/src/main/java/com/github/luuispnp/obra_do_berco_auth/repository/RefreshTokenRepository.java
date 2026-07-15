package com.github.luuispnp.obra_do_berco_auth.repository;

import com.github.luuispnp.obra_do_berco_auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
}
