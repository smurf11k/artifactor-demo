package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.exception.AuthException;
import com.renata.demoartifactor.domain.exception.UserAlreadyAuthException;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.contracts.UserRepository;
import org.mindrot.bcrypt.BCrypt;

final class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private User user;

    AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String username, String password) {
        // Перевіряємо, чи вже існує аутентифікований користувач
        if (user != null) {
            throw new UserAlreadyAuthException("Ви вже авторизувалися як: %s"
                .formatted(user.getUsername()));
        }

        User foundedUser = userRepository.findByUsername(username)
            .orElseThrow(AuthException::new);

        if (!BCrypt.checkpw(password, foundedUser.getPassword())) {
            return false;
        }

        user = foundedUser;
        return true;
    }

    public boolean isAuthenticated() {
        return user != null;
    }

    public User getUser() {
        return user;
    }

    public void logout() {
        if (user == null) {
            throw new UserAlreadyAuthException("Ви ще не авторизовані.");
        }
        user = null;
    }
}
