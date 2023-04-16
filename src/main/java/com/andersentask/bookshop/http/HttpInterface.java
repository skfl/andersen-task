package com.andersentask.bookshop.http;

import com.andersentask.bookshop.console.Commands;
import com.andersentask.bookshop.http.servlets.CreateOrderServlet;
import com.andersentask.bookshop.http.servlets.GetBookServlet;
import com.andersentask.bookshop.http.servlets.GetOrderServlet;
import com.andersentask.bookshop.http.servlets.GetRequestsServlet;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

@Slf4j
public class HttpInterface {
    private final Commands commands = new Commands();
    private Server server;
    ;

    private void addServlets(ServletHandler servletHandler) {
        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetBookServlet(commands)),
                        "/getbooks");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new CreateOrderServlet(commands)),
                        "/createorder");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetOrderServlet(commands)),
                        "/getorders");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetRequestsServlet(commands)),
                        "/getrequests");
    }

    private void configure() {
        server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);

        ServletHandler servletHandler = new ServletHandler();
        addServlets(servletHandler);

        server.setHandler(servletHandler);
        server.setConnectors(new Connector[]{connector});
    }

    public void start() {
        configure();
        try {
            server.start();
        } catch (Exception e) {
            log.error("Server hasn't started");
            throw new RuntimeException(e);
        }
    }
}