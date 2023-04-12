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
            * getSortedBooks <option> -- to see all books sorted by <option>. Possible options: NAME, PRICE, STATUS
            * setStatusToBookAndDeleteCorrespondingRequests <id> <status> -- to set status on certain book and also delete requests which corresponding with this book. Possible status: AVAILABLE, NOT_AVAILABLE, OUT_OF_STOCK
            * createRequest <book.id> -- to create and save request from a book (doesn't matter, what status)
            * createOrder <id1> <id2> ... -- Create and save order from a list of id of books. If book is out_of_stock, also created ans saves request. If any id is invalid, the order and request are not created.
            * changeStatusOfOrder <order.id> <status> -- to set status of the order to chosen by the user. Possible status: IN_PROCESS, COMPLETED, CANCELLED
            * getNumberOfRequestsOnBook <book.id> -- to see number of the requests on the book.
            * getBooksAndNumberOfRequests -- to see id of books and the number of requests on these books in descending order.
            * getIncomeForPeriod <LocalDateTime> <LocalDateTime> -- to see the income for the chosen period (order should have completed status).
            * getAllBooksFromOrder <order.id> -- to see all books from the certain order.
            """),
    UNKNOWN_COMMAND_MESSAGE("""
            This command is unknown.
            Try again.
            Type help to see all possible commands.
            """),
    SUCCESSFUL_LOGIN("""
            You've been successfully logged in
            """),
    UNSUCCESSFUL_LOGIN("""
            Invalid email or password.
            """),
    SUGGEST_EXIT("""
            You could leave by typing exit
            """);

    private final String value;
}