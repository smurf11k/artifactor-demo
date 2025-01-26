package com.renata.demoartifactor.domain.contract;


import com.renata.demoartifactor.domain.Reportable;
import com.renata.demoartifactor.domain.Service;
import com.renata.demoartifactor.domain.dto.UserAddDto;
import com.renata.demoartifactor.persistance.entity.impl.User;

public interface UserService extends Service<User>, Reportable<User> {

    User getByUsername(String username);

    User getByEmail(String email);

    User add(UserAddDto userAddDto);
}
