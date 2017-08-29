package ru.saydumarov;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String translation = Translator.translate(scanner.nextLine());
                System.out.println("Перевод: " + translation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
