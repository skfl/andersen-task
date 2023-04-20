package com.andersentask.bookshop.http.servlets;

import com.andersentask.bookshop.broker.Commands;

import java.util.Map;

public class GetBooksAndNumberOfRequestsServlet extends JsonServlet {

    private final Commands commands;

    public GetBooksAndNumberOfRequestsServlet(Commands commands) {
        this.commands = commands;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        return new Response(commands.getBooksAndNumberOfRequests());
    }
}
