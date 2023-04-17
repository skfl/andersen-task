package com.andersentask.bookshop.http.servlets;

import com.andersentask.bookshop.console.Commands;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class ExitServlet extends JsonServlet{

    private final Commands commands;

    public ExitServlet(Commands commands) {
        this.commands = commands;
    }

    @Override
    Response get(String uri, Map<String, String[]> parameters) { //todo:try to find better workaround
        commands.exit();
        return new Response(HttpServletResponse.SC_OK);
    }
}
