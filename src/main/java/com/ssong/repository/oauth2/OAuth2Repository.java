package com.ssong.repository.oauth2;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ssong.domain.auth.OAuth2;

import java.util.Optional;

public interface OAuth2Repository extends JpaRepository<OAuth2, Long> {
    Optional<OAuth2> findByRegistrationIdAndProviderId(String registrationId, String providerId);
}
