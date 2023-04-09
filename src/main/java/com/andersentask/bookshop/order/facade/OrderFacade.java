package com.andersentask.bookshop.order.facade;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.services.BookService;
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

    BookService bookService;


    /**
     * <p>builds interim order from scanner information</p>>
     * <p>order includes list of books, that both available and out_of_stock</p>
     * <p>order next is processed by method createOrderAndRequest at OrderService</p>
     * @author Ulad Sachkouski
     * @param user user from scanner (UserService.createUser())
     * @return Order, if the user enters books, that exist in DB. Otherwise, recursively return this method
     */
    public Order buildOrder(User user){

        //toDo: this should be changed on Logger.info (added to default msg, if absent)
        System.out.println("Please, choose wished books from list below. " +
                "You should enter name of books in one line, separated by space ");

        List<Book> allBooksInShop = bookService.getAllBooks();

        //toDo: this should be changed on Logger.info (added to default msg, if absent)
        allBooksInShop.forEach(x -> System.out.println(x.getName() + "\t" + x.getPrice() + "\t" +
                x.getStatus()));

        //toDo: this should be changed for waitForInput from AppController
        try (Scanner scanner = new Scanner(System.in)) {
            String line = scanner.nextLine();
            List<String> bookNamesFromClient = Arrays.stream(line.split(" "))
                    .toList();

            List<Book> booksForCorrectCheck = allBooksInShop.stream()
                    .filter(x -> bookNamesFromClient.contains(x.getName()))
                    .toList();

            if (bookNamesFromClient.size() != booksForCorrectCheck.size()) {
                //toDo: this should be changed on Logger.warn (added to default msg, if absent)
                System.out.println("You entered non-existing book. Please, try again");
                return buildOrder(user);
            }


            return Order.builder()
                    .user(user)
                    .orderCost(0)
                    .orderStatus(OrderStatus.IN_PROCESS)
                    .timeOfCompletingOrder(LocalDateTime.now())
                    .booksInOrder(booksForCorrectCheck)
                    .build();
        }
    }

}
