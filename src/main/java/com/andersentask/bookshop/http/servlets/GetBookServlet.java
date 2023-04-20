package com.andersentask.bookshop.http.servlets;

import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.broker.Commands;

import java.util.Locale;
import java.util.Map;

public class GetBookServlet extends JsonServlet {

    private final Commands commands;

    public GetBookServlet(Commands commands) {
        this.commands = commands;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        return new Response
                (commands.getSortedBooks(BookSort.valueOf
                        (parameters.getOrDefault("sort", new String[]{"ID"})[0].toUpperCase(Locale.ROOT))));
    }
}
