package com.andersentask.bookshop.order.facade;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.user.entities.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;


@RequiredArgsConstructor
public class OrderFacade {

    private long counter = 0;

    public Order buildOrder(User user, List<Book> allBooksInShop){

        System.out.println("Please, choose wished books from list below. " +
                "You should enter name of books in one line, separated by space ");

        allBooksInShop.forEach((x) -> System.out.println(x.getName() + "\t" + x.getPrice() + "\t" +
                x.getStatus()));

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        List<String> bookNamesFromClient = Arrays.stream(line.split(" "))
                .toList();

        // Check if user enters the correct data
        // The list of books in shops is filtered by names, entered by a client
        // if the received list size equals to list size of entered books by the client => correct
        // if data is not correct, the method calls itself
        List<Book> booksDTOToOrder = allBooksInShop.stream()
                .filter((x) -> bookNamesFromClient.contains(x.getName()))
                .toList();

        if (bookNamesFromClient.size() != booksDTOToOrder.size()) {
            System.out.println("You entered non-existing book. Please, try again");
            return buildOrder(user,allBooksInShop);
        }

        return Order.builder()
                .orderId(counter++)
                .user(user)
                .orderCost(0)  // in order still can be books, that are out_of_stock. Calculation of cost will be later
                .orderStatus(OrderStatus.IN_PROCESS)
                .timeOfCompletingOrder(LocalDateTime.now())
                .booksInOrder(booksDTOToOrder)
                .build();
    }

}
