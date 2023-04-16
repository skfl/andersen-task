package com.andersentask.bookshop.http.servlets;

import com.andersentask.bookshop.console.Commands;
import com.andersentask.bookshop.order.enums.OrderStatus;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChangeOrderStatusServlet extends JsonServlet {

    private final Commands commands;

    public ChangeOrderStatusServlet(Commands commands) {
        this.commands = commands;
    }

    @Override
    Response post(String uri, Map<String, List<String>> body) {
        return new Response
                (commands.changeStatusOfOrderIncludingBooksCheck
                        (Long.parseLong(body.get("orderId").get(0)),
                                OrderStatus.valueOf(body.get("status").get(0).toUpperCase(Locale.ROOT))));
    }
}
