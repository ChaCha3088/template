package com.ssong.repository.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ssong.domain.auth.Jwt;

import java.util.Optional;

public interface JwtRepository extends JpaRepository<Jwt, Long>, JwtRepositoryQueryDsl {
    Optional<Jwt> findByRefreshToken(String refreshToken);
    Optional<Jwt> findByMemberId(Long memberId);
    void deleteByRefreshToken(String refreshToken);
}
