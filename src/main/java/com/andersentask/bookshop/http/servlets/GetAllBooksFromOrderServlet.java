package com.andersentask.bookshop.http.servlets;

import com.andersentask.bookshop.broker.Commands;

import java.util.Map;

public class GetAllBooksFromOrderServlet extends JsonServlet {

    private final Commands commands;

    public GetAllBooksFromOrderServlet(Commands commands) {
        this.commands = commands;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        return new Response
                (commands.getAllBooksFromOrder
                        (Long.parseLong(parameters.getOrDefault("orderId", new String[]{""})[0])));
    }
}
