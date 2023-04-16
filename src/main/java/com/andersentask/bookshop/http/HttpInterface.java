package com.andersentask.bookshop.http;

import com.andersentask.bookshop.console.Commands;
import com.andersentask.bookshop.http.servlets.*;
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

    //todo: rework end-points
    private void addServlets(ServletHandler servletHandler) {
        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetBookServlet(commands)),
                        "/get-books");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new CreateOrderServlet(commands)),
                        "/create-order");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetOrderServlet(commands)),
                        "/get-orders");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetRequestsServlet(commands)),
                        "/get-requests");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetNumberOfRequestsOnBookServlet(commands)),
                        "/get-number-of-request");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetBooksAndNumberOfRequestsServlet(commands)),
                        "/get-books-and-number-of-requests");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new GetAllBooksFromOrderServlet(commands)),
                        "/get-all-books-from-order");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new CreateRequestServlet(commands)),
                        "/create-request");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new ChangeBookStatusServlet(commands)),
                        "/change-book-status");

        servletHandler
                .addServletWithMapping(new ServletHolder
                                (new ChangeOrderStatusServlet(commands)),
                        "/change-order-status");
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