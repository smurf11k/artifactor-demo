package com.renata.demoartifactor.domain.contract;

import com.renata.demoartifactor.persistance.entity.impl.User;

public interface AuthService {

    boolean authenticate(String username, String password);

    boolean isAuthenticated();

    User getUser();

    void logout();
}
