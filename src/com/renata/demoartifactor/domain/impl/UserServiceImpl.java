package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.UserService;
import com.renata.demoartifactor.domain.dto.UserAddDto;
import com.renata.demoartifactor.domain.exception.EntityNotFoundException;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.contracts.UserRepository;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import org.mindrot.bcrypt.BCrypt;

final class UserServiceImpl extends GenericService<User> implements UserService {

    private final UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Такої пошти користувача не існує."));
    }

    @Override
    public Set<User> getAll() {
        return getAll(u -> true);
    }

    @Override
    public Set<User> getAll(Predicate<User> filter) {
        return new TreeSet<>(userRepository.findAll(filter));
    }

    @Override
    public User add(UserAddDto userAddDto) {
        var user = new User(userAddDto.getId(),
            BCrypt.hashpw(userAddDto.rawPassword(), BCrypt.gensalt()),
            userAddDto.email(),
            userAddDto.username(),
            userAddDto.role());
        userRepository.add(user);
        return user;
    }
}
