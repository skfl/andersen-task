package com.andersentask.bookshop.command;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.console.ConsoleAppContextConfig;
import com.andersentask.bookshop.console.ConsoleAppController;

import java.util.List;

public class GetBooks implements Command {

    ConsoleAppContextConfig config;

    @Override
    public void printDescription() {
        System.out.println("Print command 'get-books (param)' and you will get list of books, " +
                "sorted by params ('NAME', 'PRICE', 'STATUS')");
    }

    /**
     * return list of books, sorted by param bookSort
     * @param bookSort can be name, price or status and should be got from user
     * @return new list of books, sorted by entered param
     */
    public List<Book> getBooks(BookSort bookSort) {
        return config.getBookService().getSortedBooks(bookSort);
    }
}
