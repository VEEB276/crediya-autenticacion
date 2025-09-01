package co.com.pragma.crediya.model.usuario.login;

import java.time.Instant;

public record Login(
        String accessToken,
        Instant expiresAt,
        Long ttlMinutes
) {
}
