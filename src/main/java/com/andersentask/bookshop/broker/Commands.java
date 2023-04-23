package com.andersentask.bookshop.broker;

import com.andersentask.bookshop.AppContextConfig;
import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.broker.enums.ResultOfOperation;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.request.entities.Request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Commands {

    private final AppContextConfig appContextConfig = new AppContextConfig();

    /**
     * Method set new status to book
     * Returns result of operation:
     * WRONG_BOOK_ID => if the input can be parsed to Long, but there is no such ID for books
     * BOOK_ALREADY_HAS_THIS_STATUS => if input has the same bookstatus, as the book
     * BOOK_STATUS_UPDATED => if status was successfully updated
     * If the book status changed from OUT_OF_STOCK to AVAILABLE,
     * all requests for this book are deleted
     *
     * @param bookId     the bookId of the book and should be got from user
     * @param bookStatus bookStatus of the book and got from user
     * @return the status of method completion as ENUM
     */
    public ResultOfOperation.SetBookStatus setStatusToBookAndDeleteCorrespondingRequests(Long bookId, BookStatus bookStatus) {
        return appContextConfig.getBookService()
                .getBookById(bookId)
                .map(book -> setBookStatus(book, bookStatus))
                .orElse(ResultOfOperation.SetBookStatus.WRONG_BOOK_ID);
    }

    private ResultOfOperation.SetBookStatus setBookStatus(Book book, BookStatus bookStatus) {
        if (bookhasSameStatus(book, bookStatus)) {
            return ResultOfOperation.SetBookStatus.BOOK_ALREADY_HAS_THIS_STATUS;
        }
        if (bookWillBecomeAvailable(book)) {
            appContextConfig.getRequestService()
                    .deleteRequest(book);
        }
        appContextConfig.getBookService()
                .setStatusToBook(book.getId(), bookStatus);
        return ResultOfOperation.SetBookStatus.BOOK_STATUS_UPDATED;
    }

    private boolean bookhasSameStatus(Book book, BookStatus bookStatus) {
        return book.getStatus() == bookStatus;
    }

    private boolean bookWillBecomeAvailable(Book book) {
        return book.getStatus() == BookStatus.OUT_OF_STOCK;
    }

    /**
     * Create and save request from a book (doesn't matter, what status of book)
     * Returns result of operation as ENUM:
     * WRONG_BOOK_ID => if the input can be parsed to Long, but there is no such ID for books
     * REQUEST_CREATED => if the request was successfully created
     *
     * @param bookId book object and should be got from user
     * @return the status of method completion as ENUM
     */
    public ResultOfOperation.CreateRequest createRequest(Long bookId) {
        return appContextConfig.getBookService()
                .getBookById(bookId)
                .map(this::createRequestFromBook)
                .orElse(ResultOfOperation.CreateRequest.WRONG_BOOK_ID);
    }

    private ResultOfOperation.CreateRequest createRequestFromBook(Book book) {
        Request request = appContextConfig.getEntityFactory()
                .buildRequest(book);
        appContextConfig.getRequestService()
                .saveRequest(request);
        return ResultOfOperation.CreateRequest.REQUEST_CREATED;
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
     * Create and save order from a list of id of books
     * If book is out_of_stock, also create and save request
     * Returns result of operation as ENUM:
     * WRONG_BOOK_ID => if the input can be parsed to Long, but there is no such ID for books
     * ORDER_CREATED => if the order was successfully created and saved
     * ORDER_AND_REQUESTS_CREATED => of the order and requests (n=>1) were successfully created and saved
     *
     * @param ids list of id of books and should be got from user
     * @return the status of method completion as ENUM
     */
    public ResultOfOperation.CreateOrder createOrder(List<Long> ids) {
        List<Book> booksToOrder = appContextConfig.getBookService()
                .getBooksByIds(ids);
        if (booksToOrder.size() != ids.size()) {
            return ResultOfOperation.CreateOrder.WRONG_BOOK_ID;
        }
        Order order = appContextConfig.getEntityFactory()
                .buildOrder(booksToOrder);
        appContextConfig.getOrderService().saveOrder(order);
        return createRequestIfOrderHasOutOfStockBooks(booksToOrder);
    }

    private ResultOfOperation.CreateOrder createRequestIfOrderHasOutOfStockBooks(List<Book> booksToOrder) {
        List<Book> booksToRequest = appContextConfig.getBookService()
                .getBooksOutOfStock(booksToOrder);
        if (!booksToRequest.isEmpty()) {
            booksToRequest.forEach(this::createRequestFromBook);
            return ResultOfOperation.CreateOrder.ORDER_AND_REQUESTS_CREATED;
        }
        return ResultOfOperation.CreateOrder.ORDER_CREATED;
    }

    /**
     * method set new status to order with following logic:
     * in_processing => canceled, in_processing => completed
     * canceled => in_processing
     * completed can't be changed. To be completed, order should have all of it books available
     * Returns result of operation as ENUM:
     * WRONG_ORDER_ID => if the input can be parsed to Long, but there is no such ID for order
     * ORDER_STATUS_CAN_NOT_BE_UPDATED => if the logic is not met
     * STATUS_UPDATED => if the order status was successfully updated
     *
     * @param id          id of order and should be got from user
     * @param orderStatus can be completed, canceled or in_processing and should be got from user
     * @return the status of method completion as ENUM
     */
    public ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck changeStatusOfOrderIncludingBooksCheck(Long id, OrderStatus orderStatus) {
        return appContextConfig.getOrderService()
                .getOrderById(id)
                .map(order -> changeStatusOfOrder(order, orderStatus))
                .orElse(ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck.WRONG_ORDER_ID);
    }

    private ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck changeStatusOfOrder(Order order, OrderStatus orderStatus) {
        if (orderHasSameStatus(order, orderStatus)) {
            return ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck.ORDER_ALREADY_HAS_THIS_STATUS;
        }
        if (orderStatusNotToBeCompletedOrAllBooksAvailable(order, orderStatus)) {
            order = appContextConfig.getOrderService()
                    .changeStatusOfOrder(order.getOrderId(), orderStatus);
        }
        return orderStatusUpdateWasSuccessful(order, orderStatus);
    }

    private boolean orderHasSameStatus(Order order, OrderStatus orderStatus) {
        return order.getOrderStatus() == orderStatus;
    }

    private boolean orderStatusNotToBeCompletedOrAllBooksAvailable(Order order, OrderStatus orderStatus) {
        boolean allBooksAvailable = appContextConfig.getBookService()
                .allBooksAreAvailable(order.getBooks());
        return orderStatus != OrderStatus.COMPLETED || allBooksAvailable;
    }

    private ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck orderStatusUpdateWasSuccessful(Order order, OrderStatus orderStatus) {
        return order.getOrderStatus() == orderStatus ?
                ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck.STATUS_UPDATED :
                ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck.ORDER_STATUS_CAN_NOT_BE_UPDATED;
    }

    /**
     * return list of orders, sorted by param orderSort
     * if no correct sort values entered, return list sorted by id
     *
     * @param orderSort can be cost, completion_date or status and should be got from user
     * @return orders, optionally sorted by entered param
     */
    public List<Order> getOrders(OrderSort orderSort) {
        return appContextConfig.getOrderService()
                .getSortedOrders(orderSort);
    }

    /**
     * return optional of number of the requests on the book
     * if the value of the optional is set, that means, that book id is entered correct
     * otherwise, the value of the optional is empty (means, book id is invalid)
     *
     * @param id id of the book and should be got from user
     * @return number of the request on the precise book
     */
    public Optional<Long> getNumberOfRequestsOnBook(Long id) {
        return appContextConfig.getBookService()
                .getBookById(id)
                .map(book -> appContextConfig.getRequestService()
                        .getNumberOfRequestsOnBook(book.getId()));
    }

    /**
     * return id of books and the number of requests on these books in descending order
     *
     * @return id of the book and number of requests on this book
     */
    public Map<Long, Long> getBooksAndNumberOfRequests() {
        Map<Long, Long> map = new HashMap<>();
        appContextConfig.getRequestService()
                .getAllBooksFromAllRequests()
                .stream()
                .distinct()
                .sorted(Comparator.comparing(book -> getNumberOfRequestsOnBook(book.getId()).orElse(0L),
                        Comparator.reverseOrder()))
                .forEachOrdered(book -> map.put(book.getId(),
                        getNumberOfRequestsOnBook(book.getId()).orElse(0L)));
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
        System.exit(0);
    }
}
