//package com.andersentask.bookshop.console;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.List;
//import java.util.Scanner;
//
//@Slf4j
//@AllArgsConstructor
//public class Test1 {
//
//    Commands commands;
//    private final Scanner scanner;
//    private String input;
//
//    public void handleHelpCommand() {
//        log.info(DefaultMessages.HELP_MESSAGE.getValue());
//        waitForCommand();
//    }
//
//    public void handleUnknownCommand() {
//        log.info(DefaultMessages.UNKNOWN_COMMAND_MESSAGE.getValue());
//        waitForCommand();
//    }
//
//    public void createOrder(List<String> list) {
//        // validation
//        Commands.createOrder(list.get(0));
//        System.out.println("xxx");
//        waitForCommand();
//    }
//
//}
