package co.com.pragma.crediya.model.usuario.login;

public record Login(
        String accessToken,
        long expiresMinutes
) {
}
