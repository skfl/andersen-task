package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.user.entities.User;
import com.andersentask.bookshop.user.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
public class BookstoreConsoleAppController {
    private final BookService bookService;
    private final UserService userService;
    private final Scanner scanner;
    private User user;
    private String input;

    public BookstoreConsoleAppController() {
        BookstoreConsoleAppContextConfig context = new BookstoreConsoleAppContextConfig();
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
            case "help" -> handleHelpCommand();
            case "login" -> handleLoginCommand();
            case "logout" -> handleLogoutCommand();
            case "get-book-list" -> handleGetBookListCommand();
            case "get-book-list-sorted" -> handleGetBookListSortedCommand(option);
            case "" -> waitForCommand();
            default -> handleUnknownCommand();
        }
    }

    private void handleGetBookListSortedCommand(String option) {
        switch (option) {
            case "name" -> printListData(bookService.getBooksSortedByName());
            case "price" -> printListData(bookService.getBooksSortedByPrice());
            case "availability" -> printListData(bookService.getBooksSortedByAvailability());
            default -> handleUnknownCommand();
        }
        waitForCommand();
    }

    private void handleGetBookListCommand() {
        log.info("There are books");
        log.info("");
        printListData(bookService.getAllBooks());
        waitForCommand();
    }

    private <T> void printListData(List<T> list) {
        int counter = 1;
        for (T element : list) {
            log.info(counter + ") " + element.toString());
            counter++;
        }
    }

    private void handleLogoutCommand() {
        user = null;
        log.info("You've been logged out");
        init();
    }

    private void handleHelpCommand() {
        log.info(DefaultMessages.HELP_MESSAGE.getValue());
        waitForCommand();
    }

    private void handleUnknownCommand() {
        log.info(DefaultMessages.UNKNOWN_COMMAND_MESSAGE.getValue());
        waitForCommand();
    }

    private void handleLoginCommand() {
        log.info("Type your email...");
        String email = waitForInput();
        log.info("Type your password...");
        String password = waitForInput();
        Optional<User> toAuth = userService.findByEmail(email);
        if (toAuth.isEmpty() || !toAuth.get().getPassword().equals(password)) {
            onUnsuccessfulLogin();
        }
        user = toAuth.get();
        log.info(DefaultMessages.SUCCESSFUL_LOGIN.getValue());
        waitForCommand();
    }

    private void onUnsuccessfulLogin() {
        log.info(DefaultMessages.UNSUCCESSFUL_LOGIN.getValue());
        init();
    }

}
