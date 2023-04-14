package com.andersentask.bookshop.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleAppController {
    private final Scanner scanner;
    private final UserCommunication userCommunication;

    public ConsoleAppController() {
        this.scanner = new Scanner(System.in);
        userCommunication = new UserCommunication(new Commands());
    }

    public void run() {
        System.out.println(DefaultMessages.HELLO_MESSAGE.getValue());
        waitForCommand();
    }

    private void waitForCommand() {
        String input;
        while (true) {
            input = scanner.nextLine();
            if (input.equals("exit")) {
                userCommunication.exit();
                break;
            }
            handleConsoleInput(input.trim());
        }
    }

    public void handleConsoleInput(String input) {
        List<String> userInput = new ArrayList<>(Arrays.stream(input.split(" ")).toList());
        String command = userInput.remove(0);
        switch (command) {
            case "help" -> userCommunication.help();
            case "get-books" -> userCommunication.getBooks(userInput);
            case "get-orders" -> userCommunication.getOrders(userInput);
            case "get-requests" -> userCommunication.getRequests();
            case "get-number-of-requests" -> userCommunication.getNumberOfRequestsOnBook(userInput);
            case "get-books-and-requests" -> userCommunication.getBooksAndNumberOfRequests();
            case "get-income" -> userCommunication.getIncomeForPeriod(userInput);
            case "get-books-from-order" -> userCommunication.getAllBooksFromOrder(userInput);
            case "create-request" -> userCommunication.createRequest(userInput);
            case "create-order" -> userCommunication.createOrder(userInput);
            case "change-book-status" -> userCommunication.changeBookStatus(userInput);
            case "change-order-status" -> userCommunication.changeOrderStatus(userInput);
            case "" -> waitForCommand();
            default -> handleUnknownCommand();
        }
    }

    public void handleUnknownCommand() {
        System.out.println(DefaultMessages.UNKNOWN_COMMAND.getValue());
        waitForCommand();
    }
}