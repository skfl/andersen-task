package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.user.entities.User;
import com.andersentask.bookshop.user.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Scanner;

@Slf4j
public class ConsoleAppController {
    private final BookService bookService;
    private final UserService userService;
    private final Scanner scanner;
    private User user;
    private String input;

    public ConsoleAppController() {
        ConsoleAppContextConfig context = new ConsoleAppContextConfig();
        this.bookService = context.getBookService();
        this.userService = context.getUserService();
        this.scanner = new Scanner(System.in);
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
        String[] commandArr = input.split(" ");
        String command = commandArr[0];  //todo: find better workaround
        String option = "";
        if (commandArr.length > 1) {
            option = commandArr[1];
        }
        switch (command) {
//            case "help" -> handleHelpCommand();
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