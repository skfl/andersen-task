package com.andersentask.bookshop.http.servlets;

import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.console.Commands;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChangeBookStatusServlet extends JsonServlet {

    private final Commands commands;

    public ChangeBookStatusServlet(Commands commands) {
        this.commands = commands;
    }

    @Override
    Response post(String uri, Map<String, List<String>> body) {
        return new Response
                (commands.setStatusToBookAndDeleteCorrespondingRequests
                        (Long.parseLong(body.get("bookId").get(0)),
                                BookStatus.valueOf(body.get("status").get(0).toUpperCase(Locale.ROOT))));
    }
}
