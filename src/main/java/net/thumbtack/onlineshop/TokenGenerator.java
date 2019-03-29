package net.thumbtack.onlineshop;

import java.util.UUID;

public class TokenGenerator {

    public String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
