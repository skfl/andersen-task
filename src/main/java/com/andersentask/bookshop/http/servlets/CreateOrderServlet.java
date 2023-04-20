package com.andersentask.bookshop.http.servlets;

import com.andersentask.bookshop.broker.Commands;

import java.util.List;
import java.util.Map;

public class CreateOrderServlet extends JsonServlet {

    private final Commands commands;

    public CreateOrderServlet(Commands commands) {
        this.commands = commands;
    }

    @Override
    Response post(String uri, Map<String, List<String>> body) {
        return new Response
                (commands.createOrder(body.get("ids").stream()
                        .map(Long::parseLong)
                        .toList()));
    }
}
