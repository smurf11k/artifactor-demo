package com.renata.demoartifactor.appui.pages;

import static com.renata.demoartifactor.appui.PrintUI.printBlue;
import static com.renata.demoartifactor.appui.PrintUI.printGreenMessage;
import static com.renata.demoartifactor.appui.PrintUI.printPromptBlue;
import static com.renata.demoartifactor.appui.PrintUI.printPurpleMessage;
import static com.renata.demoartifactor.appui.PrintUI.printRedMessage;
import static com.renata.demoartifactor.appui.pages.AuthView.AuthMenu.EXIT;
import static com.renata.demoartifactor.appui.pages.AuthView.AuthMenu.SIGN_IN;
import static com.renata.demoartifactor.appui.pages.AuthView.AuthMenu.SIGN_UP;

import com.renata.demoartifactor.appui.Renderable;
import com.renata.demoartifactor.domain.contract.AuthService;
import com.renata.demoartifactor.domain.contract.SignUpService;
import com.renata.demoartifactor.domain.dto.UserAddDto;
import com.renata.demoartifactor.domain.exception.AuthException;
import com.renata.demoartifactor.domain.exception.SignUpException;
import com.renata.demoartifactor.domain.impl.ServiceFactory;
import com.renata.demoartifactor.persistance.entity.impl.User;
import com.renata.demoartifactor.persistance.repository.impl.json.JsonRepositoryFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public final class AuthView implements Renderable {

    private final ServiceFactory serviceFactory;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public AuthView(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    private void process(AuthMenu selectedItem) throws IOException {
        AuthService authService = serviceFactory.getAuthService();
        SignUpService signUpService = serviceFactory.getSignUpService();

        switch (selectedItem) {
            case SIGN_IN -> {
                printPromptBlue("Впишіть ваш логін: ");
                String username = reader.readLine();

                printPromptBlue("Впишіть ваш пароль: ");
                String password = reader.readLine();

                try {
                    boolean authenticate = authService.authenticate(username, password);
                    if (authenticate) {
                        User user = authService.getUser();
                        System.out.printf("Аутентифікація успішна. Роль: %s%n", user.getRole());

                        new MainMenuView(user.getRole(), serviceFactory).render();
                    } else {
                        System.err.println("Аутентифікація неуспішна.");
                    }
                } catch (AuthException e) {
                    System.err.println("Помилка аутентифікації: " + e.getMessage());
                }
            }
            case SIGN_UP -> {

                printPromptBlue("Впишіть ваш логін: ");
                String username = reader.readLine();

                printPromptBlue("Впишіть ваш пароль: ");
                String password = reader.readLine();

                printPromptBlue("Вкажіть вашу електронну пошту: ");
                String email = reader.readLine();

                try {
                    UserAddDto userAddDto = new UserAddDto(
                        UUID.randomUUID(),
                        username,
                        password,
                        email
                    );

                    signUpService.signUp(userAddDto, () -> {
                        printPromptBlue("Введіть код підтвердження з вашої пошти: ");
                        try {
                            return reader.readLine();
                        } catch (IOException e) {
                            throw new SignUpException("Помилка коду підтвердження: " + e);
                        }
                    });
                    JsonRepositoryFactory.getInstance().commit();

                    printGreenMessage("Реєстрація успішна!");
                    boolean authenticate = authService.authenticate(username, password);
                    if (authenticate) {
                        User user = authService.getUser();
                        System.out.printf("Аутентифікація успішна. Роль: %s%n", user.getRole());

                        new MainMenuView(user.getRole(), serviceFactory).render();
                    } else {
                        printRedMessage("Автоматична аутентифікація не вдалася.");
                    }
                } catch (SignUpException e) {
                    System.err.println("Помилка реєстрації: \n" + e.getMessage());
                }
            }
            case EXIT -> printRedMessage("Вихід з програми...");
            default -> printRedMessage("Неправильний вибір");

        }
    }

    @Override
    public void render() throws IOException {
        while (true) {
            printPurpleMessage("\n\n=== Меню ===");
            System.out.println("1. " + SIGN_IN.getName());
            System.out.println("2. " + SIGN_UP.getName());
            System.out.println("0. " + EXIT.getName());
            System.out.print(printBlue("Зробіть вибір: "));

            String choice = reader.readLine();
            AuthMenu selectedItem;

            try {
                selectedItem = switch (choice) {
                    case "1" -> SIGN_IN;
                    case "2" -> SIGN_UP;
                    case "0" -> EXIT;
                    default -> throw new IllegalArgumentException("Неправильний вибір");
                };

                if (selectedItem == EXIT) {
                    process(EXIT);
                    System.exit(0);
                    break;
                }

                process(selectedItem);
            } catch (IllegalArgumentException e) {
                System.err.println("Помилка:" + e.getMessage());
            }
        }
    }

    enum AuthMenu {
        SIGN_IN("Авторизація"),
        SIGN_UP("Реєстрація"),
        EXIT("Вихід");

        private final String name;

        AuthMenu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
