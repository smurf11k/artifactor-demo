package com.renata.demoartifactor.persistance.repository.impl.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.contracts.UserRepository;
import java.util.Optional;
import java.util.Set;

final class UserJsonRepositoryImpl
    extends GenericJsonRepository<User>
    implements UserRepository {

    UserJsonRepositoryImpl(Gson gson) {
        super(gson, JsonPathFactory.USERS.getPath(), TypeToken
            .getParameterized(Set.class, User.class)
            .getType());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return entities.stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return entities.stream().filter(u -> u.getUsername().equals(email)).findFirst();
    }

    @Override
    public User add(User user) {
        super.add(user);
        
        JsonRepositoryFactory.getInstance().commit();

        return user;
    }
}
