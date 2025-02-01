package com.renata.demoartifactor;

import static com.renata.demoartifactor.appui.PrintUI.intro;

import com.renata.demoartifactor.appui.PageFactory;
import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.User;
import java.io.IOException;

public class Startup {

    private final AuthService authService;
    ServiceFactory serviceFactory;
    PageFactory pageFactory;

    public Startup(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        this.authService = serviceFactory.getAuthService();
        this.pageFactory = PageFactory.getInstance(serviceFactory);
    }

    public void start() {
        try {
            intro();

            if (authService.isAuthenticated()) {
                User.Role userRole = authService.getUser().getRole();
                pageFactory.createMainMenuView(userRole).render();
            } else {
                pageFactory.createAuthView().render();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
