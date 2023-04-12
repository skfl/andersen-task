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

    public ConsoleAppController() {
        this.scanner = new Scanner(System.in);
        userCommunication = new UserCommunication(new Commands());
    }

    public void init() {
        log.info(DefaultMessages.HELLO_MESSAGE.getValue());
        waitForCommand();
    }

    private void waitForCommand() {
        input = scanner.nextLine();
        if (input.equals("exit")) {
            init();
        }
        handleConsoleInput(input.trim());
    }

    private String waitForInput() {
        input = scanner.nextLine();
        if (input.trim().equals("exit")) {   //todo:try to get workaround with states
            init();
        }
        return input;
    }

    private void handleConsoleInput(String input) {
        List<String> userInput = Arrays.stream(input.split(" ")).toList();
        String command = userInput.remove(0);

        switch (command) {
            case "help" -> userCommunication.help(userInput);
            case "get-books" -> userCommunication.getBooks(userInput);
            case "" -> waitForCommand();
//            default -> handleUnknownCommand();
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