package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class UserCommunication {

    private final Commands commands;

    public void help(){
//        List<String> commandsToOutput = Stream.of(commands)
//                .map(Object::toString)
//                .toList();
//        System.out.println(commandsToOutput);
        System.out.println(DefaultMessages.HELP_MESSAGE.getValue());
    }


    public void getSortedBooks(String parameter) {
        System.out.println(commands.getSortedBooks(BookSort.valueOf(parameter)));
    }

    public void setStatusToBookAndDeleteCorrespondingRequests(String parameter, String parameter2) {
        Long id = Long.valueOf(parameter);
        BookStatus bookStatus = BookStatus.valueOf(parameter2);
        commands.setStatusToBookAndDeleteCorrespondingRequests(id, bookStatus);
        System.out.println("setStatusToBookAndDeleteCorrespondingRequests command is completed");
    }

    public void createRequest(String input) {
        commands.createRequest(Long.valueOf(input));
        System.out.println("createRequest command is completed");

    }

    public void createOrder(String input) {
        List<String> userInput = Arrays.stream(input.split(" ")).toList();
        List<Long> ids = userInput.stream().map(Long::parseLong).toList();
        commands.createOrder(ids);
        System.out.println("createOrder command is completed");
    }

    public void changeStatusOfOrder(String parameter, String parameter2) {
        Long id = Long.valueOf(parameter);
        OrderStatus orderStatus = OrderStatus.valueOf(parameter2);
        commands.changeStatusOfOrder(id, orderStatus);
        System.out.println("changeStatusOfOrder command is completed");

    }

    public void getOrders(String parameter) {
        System.out.println(commands.getOrders(OrderSort.valueOf(parameter)));
    }

    public void getNumberOfRequestsOnBook(String parameter) {
        System.out.println(commands.getNumberOfRequestsOnBook(Long.valueOf(parameter)));
    }

    public void getBooksAndNumberOfRequests() {
        System.out.println(commands.getBooksAndNumberOfRequests().toString());
    }

    public void getIncomeForPeriod(String parameter, String parameter2) {
        LocalDateTime ltd1 = LocalDateTime.parse(parameter);
        LocalDateTime ldt2 = LocalDateTime.parse(parameter2);
        System.out.println(commands.getIncomeForPeriod(ltd1, ldt2));
    }

    public void getAllBooksFromOrder(String parameter) {
        System.out.println(commands.getAllBooksFromOrder(Long.valueOf(parameter)));
    }
}
