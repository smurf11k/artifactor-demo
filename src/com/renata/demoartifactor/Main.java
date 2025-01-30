package com.renata.demoartifactor;

import static com.renata.demoartifactor.appui.PrintUI.intro;

import com.renata.demoartifactor.appui.pages.AuthView;
import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.contract.SignUpService;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        JsonRepositoryFactory repositoryFactory = JsonRepositoryFactory.getInstance();
        ServiceFactory serviceFactory = ServiceFactory.getInstance(repositoryFactory);
        AuthService authService = serviceFactory.getAuthService();
        SignUpService signUpService = serviceFactory.getSignUpService();

        AuthView authView = new AuthView(authService, signUpService);
        
        try {
            intro();
            authView.render();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
