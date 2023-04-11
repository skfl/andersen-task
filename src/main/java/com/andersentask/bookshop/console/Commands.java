package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.request.entities.Request;

import java.time.LocalDateTime;
import java.util.*;

public class Commands {

    private final ConsoleAppContextConfig appContextConfig = new ConsoleAppContextConfig();


    /**
     * Set status of book, if bookStatus of param is opposite to the current status
     * This method also deletes all requests on book, if status is changed to available
     *
     * @param id         the id of the book and should be got from user
     * @param bookStatus bookStatus of the book and got from user
     */
    public void setStatusToBookAndDeleteCorrespondingRequests(Long id, BookStatus bookStatus) {
        Optional<Book> optionalBook = appContextConfig.getBookService().getBookById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            switch (bookStatus) {
                case OUT_OF_STOCK -> book.setStatus(BookStatus.AVAILABLE);
                case AVAILABLE -> {
                    if (book.getStatus().equals(BookStatus.OUT_OF_STOCK)) {
                        book.setStatus(BookStatus.AVAILABLE);
                        appContextConfig.getRequestService().deleteRequest(book);
                    }
                }
            }
        }
    }


    /**
     * return list of books, sorted by param bookSort
     *
     * @param bookSort can be name, price or status and should be got from user
     * @return new list of books, sorted by entered param
     */
    public List<Book> getSortedBooks(BookSort bookSort) {
        List<Book> books = appContextConfig.getBookService().getAllBooks();
        List<Book> booksToReturn = new ArrayList<>();
        switch (bookSort) {
            case NAME -> booksToReturn = books.stream()
                    .sorted(Comparator.comparing(Book::getName))
                    .toList();
            case PRICE -> booksToReturn = books.stream()
                    .sorted(Comparator.comparing(Book::getPrice))
                    .toList();
            case STATUS -> booksToReturn = books.stream()
                    .sorted(Comparator.comparing(x -> x.getStatus().ordinal()))
                    .toList();
        }
        return booksToReturn;
    }


    /**
     * Create and save request from a book (doesn't matter, what status)
     *
     * @param book book object and should be got from user
     */
    public void createRequest(Book book) {
        Request request = appContextConfig.getEntityFactory().buildRequest(book);
        appContextConfig.getRequestService().saveRequest(request);
    }


    /**
     * Create and save order from a list of book
     * If book is out_of_stock, also created ans saves request
     *
     * @param books list of books and should be got from user
     */
    //toDO: change books
    public void createOrder(List<Book> books) {
        Order order = appContextConfig.getEntityFactory().buildOrder(books);
        appContextConfig.getOrderService().saveOrder(order);

        List<Book> booksOutOfStock = books.stream()
                .filter(x -> x.getStatus().equals(BookStatus.OUT_OF_STOCK))
                .toList();
        if (!booksOutOfStock.isEmpty()) {
            for (Book book : booksOutOfStock) {
                appContextConfig.getEntityFactory().buildRequest(book);
            }
        }
    }

    /**
     * set status of the order to chosen by the user:
     * completed: only if all books are available. Also setting time of completion
     * cancelled: only if the current status of order is in_processing
     * in_processing: only if the current status of order us cancelled
     *
     * @param id          id of order and should be got from user
     * @param orderStatus can be completed, cancelled or in_proccesing and should be got from user
     */
    public void changeStatusOfOrder(Long id, OrderStatus orderStatus) {
        Optional<Order> optionalOrder = appContextConfig.getOrderService().getOrderById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            switch (orderStatus) {
                case COMPLETED -> {
                    boolean allBooksAreAvailable = order.getBooksInOrder().stream()
                            .allMatch(x -> x.getStatus().equals(BookStatus.AVAILABLE));
                    if (allBooksAreAvailable) {
                        order.setOrderStatus(OrderStatus.COMPLETED);
                        order.setTimeOfCompletingOrder(LocalDateTime.now());
                    }
                }
                case CANCELED -> {
                    if (order.getOrderStatus().equals(OrderStatus.IN_PROCESS)) {
                        order.setOrderStatus(OrderStatus.CANCELED);
                    }
                }
                case IN_PROCESS -> {
                    if (order.getOrderStatus().equals(OrderStatus.CANCELED)) {
                        order.setOrderStatus(OrderStatus.IN_PROCESS);
                    }
                }
            }
        }
    }


    /**
     * return list of orders, sorted by param orderSort
     *
     * @param orderSort can be cost, completion_date or status and should be got from user
     * @return new list of orders, sorted by entered param
     */
    public List<Order> getSortedOrders(OrderSort orderSort) {
        List<Order> orders = appContextConfig.getOrderService().getAllOrders();
        List<Order> ordersToReturn = new ArrayList<>();
        switch (orderSort) {
            case COST -> ordersToReturn = orders.stream()
                    .sorted(Comparator.comparing(Order::getOrderCost))
                    .toList();
            case COMPLETION_DATE -> ordersToReturn = orders.stream()
                    .sorted(Comparator.comparing(Order::getTimeOfCompletingOrder).reversed())
                    .toList();
            case STATUS -> ordersToReturn = orders.stream()
                    .sorted(Comparator.comparing(x -> x.getOrderStatus().ordinal()))
                    .toList();
        }
        return ordersToReturn;
    }

    /**
     * return number of the requests on the book
     *
     * @param id id of the book and should be got from user
     * @return Long number of the request on the precise book
     */
    public Long getNumberOfRequestsOnBook(Long id) {
        return appContextConfig.getRequestService().getAllRequests().stream()
                .map(Request::getBook)
                .filter(x -> x.getId().equals(id))
                .count();
    }

    /**
     * return books and the number of requests on these books in descending order
     *
     * @return Map<String, Long> name of the book and number of requests on this book
     */
    private Map<Long, Long> getBooksAndNumberOfRequests() {
        Map<Long, Long> map = new HashMap<>();
        List<Book> books = appContextConfig.getRequestService().getAllRequests().stream()
                .map(Request::getBook)
                .toList();
        List<Book> booksDistinctSortedByNumber = books.stream()
                .distinct()
                .sorted(Comparator.comparing(x -> getNumberOfRequestsOnBook(x.getId()), Comparator.reverseOrder()))
                .toList();
        for (Book book : booksDistinctSortedByNumber) {
            map.put(book.getId(), getNumberOfRequestsOnBook(book.getId()));
        }
        return map;
    }


    /**
     * return the income for the chosen period (order should have completed status)
     *
     * @param startOfPeriod start of the period
     * @param endOfPeriod   end of the period
     * @return double income for the chosen period
     */
    public double getIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {
        return appContextConfig.getOrderService().getAllOrders().stream()
                .filter(x -> x.getOrderStatus().equals(OrderStatus.COMPLETED))
                .filter(x -> x.getTimeOfCompletingOrder().isAfter(startOfPeriod) &&
                        x.getTimeOfCompletingOrder().isBefore(endOfPeriod))
                .map(Order::getOrderCost)
                .reduce(0D, Double::sum);
    }

    /**
     * return all books from the order
     *
     * @return List<BooK> list of books
     */
    public List<Book> getAllBooksFromOrder(Order order) {
        return order.getBooksInOrder().stream().toList();
    }


}
