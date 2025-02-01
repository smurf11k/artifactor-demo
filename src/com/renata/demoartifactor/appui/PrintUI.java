package com.renata.demoartifactor.appui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class PrintUI {

    public static void intro() {
        String filePath = String.valueOf(Path.of("data", "intro.txt"));

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                printYellowMessage(line);
            }
        } catch (IOException e) {
            System.out.println("Помилка при читанні файлу: " + e.getMessage());
        }
    }

    public static void printHeader(String message) {
        System.out.println(ColorCodes.BLUE.getCode() + message + ColorCodes.RESET.getCode());
    }

    public static void printGreenMessage(String message) {
        System.out.println(ColorCodes.GREEN.getCode() + message + ColorCodes.RESET.getCode());
    }

    public static void printRedMessage(String message) {
        System.out.println(ColorCodes.RED.getCode() + message + ColorCodes.RESET.getCode());
    }

    public static void printPurpleMessage(String message) {
        System.out.println(ColorCodes.PURPLE.getCode() + message + ColorCodes.RESET.getCode());
    }

    public static void printYellowMessage(String message) {
        System.out.println(ColorCodes.YELLOW.getCode() + message + ColorCodes.RESET.getCode());
    }

    enum ColorCodes {
        BLUE("\u001B[34m"),
        GREEN("\u001B[32m"),
        RED("\u001B[31m"),
        PURPLE("\u001B[35m"),
        YELLOW("\u001B[33m"),
        RESET("\u001B[0m");

        private final String code;

        ColorCodes(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }
}
