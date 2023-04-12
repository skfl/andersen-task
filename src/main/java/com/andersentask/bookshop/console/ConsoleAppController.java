package com.andersentask.bookshop.console;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ConsoleAppController {

    private final Scanner scanner;
    private String input;
    UserCommunication userCommunication;

    private String parameter, parameter2;

    public ConsoleAppController() {
        this.scanner = new Scanner(System.in);
        userCommunication = new UserCommunication(new Commands());
    }

    public void init() {
        log.info(DefaultMessages.HELLO_MESSAGE.getValue());
        waitForCommand();
    }

    private void waitForCommand() {
        while (true) {
            input = scanner.nextLine();
            if (input.equals("exit")) {
                init();
            }
            handleConsoleInput(input.trim());
        }
    }

    private String waitForInput() {
        input = scanner.nextLine();
        if (input.trim().equals("exit")) {   //todo:try to get workaround with states
            init();
        }
        return input;
    }

    public void handleConsoleInput(String input) {
        List<String> userInput = Arrays.stream(input.split(" ")).toList();
        String command = userInput.get(0);
        if (userInput.size() > 1) {
            this.parameter = userInput.get(1);
        }
        if (userInput.size() > 2) {
            this.parameter2 = userInput.get(2);
        }
        switch (command) {
            case "help" -> userCommunication.help();
            case "getSortedBooks" -> userCommunication.getSortedBooks(parameter);
            case "setStatusToBookAndDeleteCorrespondingRequests" ->
                    userCommunication.setStatusToBookAndDeleteCorrespondingRequests(parameter, parameter2);
            case "createRequest" -> userCommunication.createRequest(parameter);
            case "createOrder" -> userCommunication.createOrder(input);
            case "changeStatusOfOrder" -> userCommunication.changeStatusOfOrder(parameter, parameter2);
            case "getOrders" -> userCommunication.getOrders(parameter);
            case "getNumberOfRequestsOnBook" -> userCommunication.getNumberOfRequestsOnBook(parameter);
            case "getBooksAndNumberOfRequests" -> userCommunication.getBooksAndNumberOfRequests();
            case "getIncomeForPeriod" -> userCommunication.getIncomeForPeriod(parameter, parameter2);
            case "getAllBooksFromOrder" -> userCommunication.getAllBooksFromOrder(parameter);
            case "" -> waitForCommand();
            default -> waitForInput();
        }
    }


    private <T> void printListData(List<T> list) {
        int counter = 1;
        for (T element : list) {
            log.info(counter + ") " + element.toString());
            counter++;
        }
    }


}