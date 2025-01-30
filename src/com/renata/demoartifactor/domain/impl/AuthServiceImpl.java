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
    }
}
