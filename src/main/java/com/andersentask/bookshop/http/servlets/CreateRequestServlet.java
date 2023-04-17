package com.andersentask.bookshop.http.servlets;

import com.andersentask.bookshop.console.Commands;

import java.util.Map;

public class CreateRequestServlet extends JsonServlet {

    private final Commands commands;

    public CreateRequestServlet(Commands commands) {
        this.commands = commands;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        return new Response
                (commands.createRequest
                        (Long.parseLong(parameters.getOrDefault("bookId",new String[]{""})[0])));
    }
}
