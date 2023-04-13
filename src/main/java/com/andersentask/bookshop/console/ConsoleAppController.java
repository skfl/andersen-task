package com.andersentask.bookshop.console;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ConsoleAppController {

    private final Scanner scanner;
    private String input;
    private final UserCommunication userCommunication;
    private final ConsoleAppContextConfig appContextConfig;

    public ConsoleAppController() {
        this.scanner = new Scanner(System.in);
        userCommunication = new UserCommunication(new Commands());
        appContextConfig = new ConsoleAppContextConfig();
    }

    public void init() {
        log.info(DefaultMessages.HELLO_MESSAGE.getValue());
        waitForCommand();
    }

    private void waitForCommand() {
        while (true) {
            input = scanner.nextLine();
            if (input.equals("exit")) {
                System.exit(0);
                break;
            }
            handleConsoleInput(input.trim());
        }
    }

    private String waitForInput() {
        input = scanner.nextLine();
        if (input.trim().equals("exit")) {
            init();
        }
        return input;
    }

    public void handleConsoleInput(String input) {
        List<String> userInput = new java.util.ArrayList<>(Arrays.stream(input.split(" ")).toList());
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
            default -> waitForInput();
        }
    }

}