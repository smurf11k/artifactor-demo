package com.renata.demoartifactor.domain.contract;

import com.renata.demoartifactor.domain.dto.UserAddDto;
import java.util.function.Supplier;

public interface SignUpService {

    void signUp(UserAddDto userAddDto, Supplier<String> waitForUserInput);
}
