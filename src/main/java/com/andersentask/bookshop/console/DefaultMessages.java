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
            * login -- to login as a User
            * exit -- leave to main menu
            * get-book-list -- to see list of all books in store
            * get-book-list-sorted <option> -- to see all books sorted by <option>. Possible options: name, price, availability  
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
