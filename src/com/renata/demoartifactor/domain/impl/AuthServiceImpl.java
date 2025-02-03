package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.exception.AuthException;
import com.renata.demoartifactor.domain.exception.UserAlreadyAuthException;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.contracts.UserRepository;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.mindrot.bcrypt.BCrypt;

final class AuthServiceImpl implements AuthService {

    private static final String SESSION_FILE = "session.txt";
    private static final int BCRYPT_ROUNDS = 12;
    private final UserRepository userRepository;
    private User user;
    private String sessionToken;

    AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        loadSession();
    }

    @Override
    public boolean authenticate(String username, String password) {
        if (user != null) {
            throw new UserAlreadyAuthException("Ви вже авторизувалися як: %s"
                .formatted(user.getUsername()));
        }

        User foundedUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthException("Користувача з таким логіном не знайдено."));

        if (!BCrypt.checkpw(password, foundedUser.getPassword())) {
            throw new AuthException("Невірний пароль.");
        }

        user = foundedUser;
        sessionToken = generateSessionToken();
        saveSession();
        return true;
    }

    @Override
    public boolean isAuthenticated() {
        return user != null;
    }

    @Override
    public User getUser() {
        if (user == null) {
            throw new AuthException("Немає активного авторизованого користувача.");
        }
        return user;
    }

    @Override
    public void logout() {
        if (user == null) {
            throw new AuthException("Ви ще не авторизовані.");
        }
        user = null;
        sessionToken = null;
        clearSession();
    }

    private void saveSession() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SESSION_FILE))) {
            writer.write(user.getUsername() + ":" + sessionToken);
        } catch (IOException e) {
            System.err.println("Не вдалося зберегти сесію: " + e.getMessage());
        }
    }

    private void loadSession() {
        try {

            Path sessionFilePath = Paths.get(SESSION_FILE);
            if (Files.exists(sessionFilePath)) {
                String data = Files.readString(sessionFilePath).trim();
                String[] parts = data.split(":");

                if (parts.length != 2) {
                    clearSession();
                    return;
                }

                String username = parts[0];
                String savedToken = parts[1];

                Optional<User> savedUser = userRepository.findByUsername(username);
                if (savedUser.isPresent() && BCrypt.checkpw(username, savedToken)) {
                    user = savedUser.get();
                    sessionToken = savedToken;
                } else {
                    clearSession();
                }
            }
        } catch (IOException e) {
            System.err.println("Не вдалося завантажити сесію: " + e.getMessage());
        }
    }

    private void clearSession() {
        try {
            Files.deleteIfExists(Paths.get(SESSION_FILE));
        } catch (IOException e) {
            System.err.println("Не вдалося видалити сесію: " + e.getMessage());
        }
    }

    private String generateSessionToken() {
        return BCrypt.hashpw(user.getUsername(), BCrypt.gensalt(BCRYPT_ROUNDS));
    }
}
