package com.andersentask.bookshop;

import com.andersentask.bookshop.console.BookstoreConsoleAppController;

public class BookshopApplication {
    public static void main(String[] args) {
        BookstoreConsoleAppController controller = new BookstoreConsoleAppController();
        controller.init();
    }
}
