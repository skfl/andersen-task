package com.andersentask.bookshop.http.servlets;

import com.andersentask.bookshop.console.Commands;
import com.andersentask.bookshop.order.enums.OrderSort;

import java.util.Locale;
import java.util.Map;

public class GetOrderServlet extends JsonServlet {
    private final Commands commands;

    public GetOrderServlet(Commands commands) {
        this.commands = commands;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) {
        return new Response
                (commands.getOrders(OrderSort.valueOf
                        (parameters.getOrDefault("sort", new String[]{""})[0].toUpperCase(Locale.ROOT))));
    }
}
