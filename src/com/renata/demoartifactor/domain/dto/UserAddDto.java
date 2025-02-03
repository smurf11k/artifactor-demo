package com.renata.demoartifactor.domain.dto;

import com.renata.demoartifactor.domain.exception.SignUpException;
import com.renata.demoartifactor.persistance.entity.Entity;
import com.renata.demoartifactor.persistance.entity.ErrorTemplates;
import com.renata.demoartifactor.persistance.entity.impl.User.Role;
import java.util.UUID;
import java.util.regex.Pattern;

public final class UserAddDto extends Entity {

    private final String username;
    private final String rawPassword;
    private final String email;
    private final Role role;

    public UserAddDto(UUID id,
        String username,
        String rawPassword,
        String email) {
        super(id);
        this.username = validatedUsername(username);
        this.rawPassword = validatedPassword(rawPassword);
        this.email = validatedEmail(email);
        this.role = Role.GENERAL;

        if (!this.errors.isEmpty()) {
            throw new SignUpException(String.join("\n", errors));
        }
    }

    private String validatedPassword(String rawPassword) {
        final String templateName = "паролю";

        if (rawPassword.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }
        if (rawPassword.length() < 8) {
            errors.add(ErrorTemplates.MIN_LENGTH.getTemplate().formatted(templateName, 8));
        }
        if (rawPassword.length() > 32) {
            errors.add(ErrorTemplates.MAX_LENGTH.getTemplate().formatted(templateName, 32));
        }
        var pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$");
        if (!pattern.matcher(rawPassword).matches()) {
            errors.add(ErrorTemplates.PASSWORD.getTemplate().formatted(templateName, 24));
        }

        return rawPassword;
    }

    public String validatedUsername(String username) {
        final String templateName = "логіну";

        if (username.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }
        if (username.length() < 4) {
            errors.add(ErrorTemplates.MIN_LENGTH.getTemplate().formatted(templateName, 4));
        }
        if (username.length() > 24) {
            errors.add(ErrorTemplates.MAX_LENGTH.getTemplate().formatted(templateName, 24));
        }
        var pattern = Pattern.compile("^[a-zA-Z0-9_]+$");
        if (!pattern.matcher(username).matches()) {
            errors.add(ErrorTemplates.ONLY_LATIN.getTemplate().formatted(templateName));
        }

        return username;
    }

    public String username() {
        return username;
    }

    public String rawPassword() {
        return rawPassword;
    }

    public String validatedEmail(String email) {
        final String templateName = "електронної пошти";

        if (email.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }
        var pattern = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        if (!pattern.matcher(email).matches()) {
            errors.add("Поле %s має бути валідною електронною поштою.".formatted(templateName));
        }

        return email;
    }

    public String email() {
        return email;
    }

    public Role role() {
        return role;
    }
}
