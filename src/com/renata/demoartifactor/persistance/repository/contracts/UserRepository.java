package com.renata.demoartifactor.persistance.repository.contracts;

import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.Repository;
import java.util.Optional;

public interface UserRepository extends Repository<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
