package com.andersentask.bookshop;

import com.andersentask.bookshop.console.ConsoleAppController;

import java.util.Scanner;

public class BookshopApplication {

    public static void main(String[] args) {

        ConsoleAppController consoleAppController = new ConsoleAppController();

        consoleAppController.init();

//        while (true) {
//            System.out.print(">");
//            Scanner scanner = new Scanner(System.in);
//            String input = scanner.nextLine();
//            consoleAppController.handleConsoleInput(input);
//        }
    }
}
