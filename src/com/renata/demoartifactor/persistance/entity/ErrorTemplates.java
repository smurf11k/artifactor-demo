package com.renata.demoartifactor.persistance.entity;

public enum ErrorTemplates {
    REQUIRED("Поле %s є обов'язковим до заповнення."),
    MIN_LENGTH("Поле %s не може бути меншим за %d симв."),
    MAX_LENGTH("Поле %s не може бути більшим за %d симв."),
    ONLY_LATIN("Поле %s може містити лише латинські символи та символ '_'."),
    PASSWORD(
        "Поле %s може містити лише латинські символи, а також має містити хоча б одну букву з великої, одну з малої та хоча б одну цифра."),
    DATE("В полі %s не може бути використана дата з майбутнього.");

    private final String template;

    ErrorTemplates(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
