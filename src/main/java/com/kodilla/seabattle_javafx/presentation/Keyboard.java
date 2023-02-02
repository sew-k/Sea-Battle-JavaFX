package com.kodilla.seabattle_javafx.presentation;

import java.util.Scanner;

public class Keyboard {
    public String key;

    public String getString() {
        Scanner scanner = new Scanner(System.in);
        String result = scanner.next();
        return result;
    }

    public int getInt() {
        Scanner scanner = new Scanner(System.in);
        int result = scanner.nextInt();
        return result;
    }
}
