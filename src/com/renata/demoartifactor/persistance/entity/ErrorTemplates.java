package com.renata.demoartifactor.persistance.entity;

public enum ErrorTemplates {
    REQUIRED("Поле %s є обов'язковим до заповнення."),
    MIN_LENGTH("Поле %s не може бути меншим за %d симв."),
    MAX_LENGTH("Поле %s не може бути більшим за %d симв."),
    ONLY_LATIN("Поле %s лише латинські символи та символ _."),
    PASSWORD(
        "Поле %s латинські символи, хоча б одна буква з великої, одна з малої та хоча б одна цифра.");

    private final String template;

    ErrorTemplates(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
