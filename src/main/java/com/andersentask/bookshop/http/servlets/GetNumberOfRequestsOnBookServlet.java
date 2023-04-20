package com.andersentask.bookshop.http.servlets;

import com.andersentask.bookshop.broker.Commands;

import java.util.Map;

public class GetNumberOfRequestsOnBookServlet extends JsonServlet {

    private final Commands commands;

    public GetNumberOfRequestsOnBookServlet(Commands commands) {
        this.commands = commands;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        return new Response
                (commands.getNumberOfRequestsOnBook
                        (Long.parseLong(parameters.getOrDefault("bookId", new String[]{""})[0])).orElse(0L));
    }
}
