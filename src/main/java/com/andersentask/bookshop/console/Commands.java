package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.utils.serialization.RepositorySerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class Commands {

    private final ConsoleAppContextConfig appContextConfig = new ConsoleAppContextConfig();


    /**
     * Sets the status of the book with the given id and deletes corresponding requests if applicable.
     *
     * @param id         the id of the book to set the status for.
     * @param bookStatus the new status to set for the book.
     * @throws RuntimeException if the book with the given id is not found.
     */
    public void setStatusToBookAndDeleteCorrespondingRequests(Long id, BookStatus bookStatus) {

        Book book = appContextConfig.getBookService()
                .getBookById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getStatus() == BookStatus.OUT_OF_STOCK && bookStatus == BookStatus.AVAILABLE) {
            appContextConfig.getRequestService().deleteRequest(book);
        }

        appContextConfig.getBookService().setStatusToBook(id, bookStatus);
    }

    /**
     * Create and save request from a book id (doesn't matter, what status of book)
     *
     * @param id book object and should be got from user
     */
    public void createRequest(Long id) {
        appContextConfig.getBookService()
                .getBookById(id)
                .ifPresent(book -> {
                    Request request = appContextConfig.getEntityFactory().buildRequest(book);
                    appContextConfig.getRequestService().saveRequest(request);
                });
    }

    /**
     * return list of books, sorted by param bookSort
     * if no correct sort values entered, return list sorted by id
     *
     * @param bookSort can be name, price, status or id and should be got from user
     * @return books, optionally sorted by entered param
     */
    public List<Book> getSortedBooks(BookSort bookSort) {
        return appContextConfig.getBookService().getSortedBooks(bookSort);
    }

    /**
     * Creates a new order for a list of books, and generates requests for any books that are out of stock.
     *
     * @param bookIds the list of IDs of the books to include in the order
     */
    public void createOrder(List<Long> bookIds) {
        List<Book> booksToOrder = appContextConfig.getBookService().getBooksByIds(bookIds);

        if (booksToOrder.size() != 0) {
            Order order = appContextConfig.getEntityFactory().buildOrder(booksToOrder);
            appContextConfig.getOrderService().saveOrder(order);
        }
        createRequestForOutOfStockBooksFromOrder(booksToOrder);
    }

    /**
     * Creates a request for each book in the given list that is currently out of stock.
     *
     * @param bookList the list of books to check for out-of-stock status
     */
    private void createRequestForOutOfStockBooksFromOrder(List<Book> bookList) {
        appContextConfig.getBookService()
                .getBooksOutOfStock(bookList)
                .forEach(book -> {
                    Request request = appContextConfig.getEntityFactory().buildRequest(book);
                    appContextConfig.getRequestService().saveRequest(request);
                });
    }

    /**
     * method set new status to order with following logic:
     * in_processing => canceled, in_processing => completed
     * canceled => in_processing
     * completed can't be changed. To be completed, order should have all of it books available
     *
     * @param id          id of order and should be got from user
     * @param orderStatus can be completed, canceled or in_proccesing and should be got from user
     */
    public void changeStatusOfOrderIncludingBooksCheck(Long id, OrderStatus orderStatus) {
        appContextConfig.getOrderService()
                .getOrderById(id)
                .filter(order -> order.getOrderStatus() != orderStatus)
                .filter(order -> orderStatus != OrderStatus.COMPLETED || appContextConfig.getBookService().allBooksAreAvailable(order.getBooksInOrder()))
                .ifPresent(order -> appContextConfig.getOrderService().changeStatusOfOrder(order.getOrderId(), orderStatus));
    }

    /**
     * return list of orders, sorted by param orderSort
     * if no correct sort values entered, return list sorted by id
     *
     * @param orderSort can be cost, completion_date or status and should be got from user
     * @return orders, optionally sorted by entered param
     */
    public List<Order> getSortedOrders(OrderSort orderSort) {
        return appContextConfig.getOrderService().getSortedOrders(orderSort);
    }

    /**
     * return optional of number of the requests on the book
     * if the value of the optional is set, that means, that book id is entered correct
     * otherwise, the value of the optional is empty (means, book id is invalid)
     *
     * @param bookId id of the book and should be got from user
     * @return number of the request on the precise book
     */
    public Optional<Long> getNumberOfRequestsOnBook(Long bookId) {
        return appContextConfig.getBookService()
                .getBookById(bookId)
                .map(book -> appContextConfig.getRequestService()
                        .getNumberOfRequestsOnBook(bookId));
    }

    /**
     * return id of books and the number of requests on these books in descending order
     *
     * @return id of the book and number of requests on this book
     */
    public Map<Long, Long> getBooksAndNumberOfRequests() {
        Map<Long, Long> map = new HashMap<>();
        appContextConfig.getRequestService()
                .getAllBooksFromRequests()
                .stream()
                .distinct()
                .sorted(Comparator.comparing(x -> getNumberOfRequestsOnBook(x.getId()).orElse(0L), Comparator.reverseOrder()))
                .forEachOrdered(book -> map.put(book.getId(), getNumberOfRequestsOnBook(book.getId()).orElse(0L)));
        return map;
    }

    /**
     * return the income for the chosen period (order should have completed status)
     *
     * @param startOfPeriod start of the period
     * @param endOfPeriod   end of the period
     * @return income for the chosen period
     */
    public BigDecimal getIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {
        return appContextConfig.getOrderService()
                .getIncomeForPeriod(startOfPeriod, endOfPeriod);
    }

    /**
     * return all books from the order by order id
     *
     * @param id id of the order and should be got from user
     * @return books from the chosen order
     */
    public List<Book> getAllBooksFromOrder(Long id) {
        return appContextConfig.getOrderService()
                .getAllBooksFromOrder(id);
    }

    /**
     * return all requests
     *
     * @return requests
     */
    public List<Request> getAllRequests() {
        return appContextConfig.getRequestService()
                .getAllRequests();
    }

    /**
     * write to file updated information on books, orders and requests,
     * if the user exits from the program by typing "exit"
     */
    public void exit() {
        RepositorySerializer serializer = appContextConfig.getSerializer();
        serializer.serializeAndWriteToFile(appContextConfig.getBookService().getAllBooks(), "books.json");
        serializer.serializeAndWriteToFile(appContextConfig.getOrderService().getAllOrders(), "orders.json");
        serializer.serializeAndWriteToFile(appContextConfig.getRequestService().getAllRequests(), "requests.json");
        System.exit(0);
    }
}
