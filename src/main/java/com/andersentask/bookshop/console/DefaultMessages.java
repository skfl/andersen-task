package com.andersentask.bookshop.console;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DefaultMessages {
    HELLO_MESSAGE("""
            Welcome to Bookstore console application.
            This program was created by a team of developers to demonstrate their knowledge
            Type help to see all possible commands
            """),
    HELP_MESSAGE("""
            There is a list of possible commands:
            * help -- to see that message
            * get-books <option> -- to see all books sorted by <option>. Possible options: name, price, status, id
            * get-orders <option> -- to see all orders sorted by <option>. Possible options: cost, completion_date, status, id
            * get-requests -- to see all requests. No options needed
            * get-number-of-requests <book.id> -- to see number of the requests on the book
            * get-books-and-requests -- to see id of books and the number of requests on these books in descending order. No options needed
            * get-books-from-order <order.id> -- to see all books from the certain order
            * get-income <LocalDateTime> <LocalDateTime> -- to see the income for the chosen period
            * create-request <book.id> -- to create and save request from a book.id
            * create-order <id1> <id2> ... -- to create and save order from a list of id of books. If book is out_of_stock, also creates ans saves request
            * change-book-status <id> <status> -- to set status on certain book and also delete requests which corresponding with this book. Possible status: available, out_of_stock
            * change-order-status <order.id> <status> -- to set status of the order to chosen by the user. Possible status: in_process, completed, canceled
            """),
    SUGGEST_EXIT("""
            You could leave by typing exit
            """),
    UNKNOWN_COMMAND("""
            Sorry, this command is unknown.Type help to see all possible commands
            """);


    private final String value;
}