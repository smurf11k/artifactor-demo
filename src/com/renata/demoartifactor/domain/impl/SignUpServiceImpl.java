package com.renata.demoartifactor.domain.impl;

import com.renata.demoartifactor.domain.contract.SignUpService;
import com.renata.demoartifactor.domain.contract.UserService;
import com.renata.demoartifactor.domain.dto.UserAddDto;
import com.renata.demoartifactor.domain.exception.SignUpException;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import java.util.function.Supplier;

final class SignUpServiceImpl implements SignUpService {

    private static final int VERIFICATION_CODE_EXPIRATION_MINUTES = 1;
    private static LocalDateTime codeCreationTime;
    private final UserService userService;

    SignUpServiceImpl(UserService userService) {
        this.userService = userService;
    }

    private static void sendVerificationCodeEmail(String email, String verificationCode) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("munkacsyrenata@gmail.com", "tejwjnbwsafaglyu");
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(
                new InternetAddress("munkacsyrenata@gmail.com"));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

            message.setSubject("Код підтвердження");

            message.setText("Ваш код підтвердження: " + verificationCode);

            Transport.send(message);

            System.out.println("Повідомлення успішно відправлено.");

        } catch (MessagingException e) {
            throw new RuntimeException(
                "Помилка при відправці електронного листа: " + e.getMessage());
        }
    }

    private static String generateAndSendVerificationCode(String email) {
        String verificationCode = String.valueOf((int) (Math.random() * 900000 + 100000));

        sendVerificationCodeEmail(email, verificationCode);

        codeCreationTime = LocalDateTime.now();

        return verificationCode;
    }

    private static void verifyCode(String inputCode, String generatedCode) {
        LocalDateTime currentTime = LocalDateTime.now();
        long minutesElapsed = ChronoUnit.MINUTES.between(codeCreationTime, currentTime);

        if (minutesElapsed > VERIFICATION_CODE_EXPIRATION_MINUTES) {
            throw new SignUpException("Час верифікації вийшов. Спробуйте ще раз.");
        }

        if (!inputCode.equals(generatedCode)) {
            throw new SignUpException("Невірний код підтвердження.");
        }

        codeCreationTime = null;
    }

    //TODO uncomment after testing
    public void signUp(UserAddDto userAddDto, Supplier<String> waitForUserInput) {
        //String verificationCode = generateAndSendVerificationCode(userAddDto.email());
        //String userInputCode = waitForUserInput.get();

        //verifyCode(userInputCode, verificationCode);

        userService.add(userAddDto);
    }
}
