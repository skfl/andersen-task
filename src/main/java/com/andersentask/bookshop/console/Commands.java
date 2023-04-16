package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.console.enums.ResultOfOperation;
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
     * Method set new status to book
     * Returns result of operation:
     * WRONG_BOOK_ID => if the input can be parsed to Long, but there is no such ID for books
     * BOOK_ALREADY_HAS_THIS_STATUS => if input has the same bookstatus, as the book
     * BOOK_STATUS_UPDATED => if status was successfully updated
     * If the book status changed from OUT_OF_STOCK to AVAILABLE,
     * all requests for this book are deleted
     *
     * @param id         the id of the book and should be got from user
     * @param bookStatus bookStatus of the book and got from user
     * @return the status of method completion as ENUM
     */
    public ResultOfOperation.SetBookStatus setStatusToBookAndDeleteCorrespondingRequests(Long id, BookStatus bookStatus) {
        Optional<Book> optionalBook = appContextConfig.getBookService().getBookById(id);
        ResultOfOperation.SetBookStatus resultOfMethod = ResultOfOperation.SetBookStatus.WRONG_BOOK_ID;

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            resultOfMethod = ResultOfOperation.SetBookStatus.BOOK_ALREADY_HAS_THIS_STATUS;

            if (!book.getStatus().equals(bookStatus)) {

                if (book.getStatus().equals(BookStatus.OUT_OF_STOCK) && bookStatus.equals(BookStatus.AVAILABLE)) {
                    appContextConfig.getRequestService().deleteRequest(book);
                }

                appContextConfig.getBookService().setStatusToBook(id, bookStatus);
                resultOfMethod = ResultOfOperation.SetBookStatus.BOOK_STATUS_UPDATED;
            }
        }
        return resultOfMethod;
    }

    /**
     * Create and save request from a book (doesn't matter, what status of book)
     * Returns result of operation as ENUM:
     * WRONG_BOOK_ID => if the input can be parsed to Long, but there is no such ID for books
     * REQUEST_CREATED => if the request was successfully created
     *
     * @param id book object and should be got from user
     * @return the status of method completion as ENUM
     */
    public ResultOfOperation.CreateRequest createRequest(Long id) {
        Optional<Book> optionalBook = appContextConfig.getBookService().getBookById(id);
        ResultOfOperation.CreateRequest resultOfMethod = ResultOfOperation.CreateRequest.WRONG_BOOK_ID;
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            Request request = appContextConfig.getEntityFactory().buildRequest(book);
            appContextConfig.getRequestService().saveRequest(request);
            resultOfMethod = ResultOfOperation.CreateRequest.REQUEST_CREATED;
        }
        return resultOfMethod;
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
     * If book is out_of_stock, also create ans save request
     * Returns result of operation as ENUM:
     * WRONG_BOOK_ID => if the input can be parsed to Long, but there is no such ID for books
     * ORDER_CREATED => if the order was successfully created and saved
     * ORDER_AND_REQUESTS_CREATED => of the order and requests (n=>1) were successfully created ans saved
     *
     * @param ids list of id of books and should be got from user
     * @return the status of method completion as ENUM
     */
    public ResultOfOperation.CreateOrder createOrder(List<Long> ids) {
        List<Book> booksToOrder = appContextConfig.getBookService().getBooksByIds(ids);

        ResultOfOperation.CreateOrder resultOfMethod = ResultOfOperation.CreateOrder.WRONG_BOOK_ID;

        if (booksToOrder.size() == ids.size()) {
            Order order = appContextConfig.getEntityFactory().buildOrder(booksToOrder);
            appContextConfig.getOrderService().saveOrder(order);
            resultOfMethod = ResultOfOperation.CreateOrder.ORDER_CREATED;

            List<Book> booksToRequest = appContextConfig.getBookService().getBooksOutOfStock(booksToOrder);

            if (!booksToRequest.isEmpty()) {

                for (Book book : booksToRequest) {
                    Request request = appContextConfig.getEntityFactory().buildRequest(book);
                    appContextConfig.getRequestService().saveRequest(request);
                    resultOfMethod = ResultOfOperation.CreateOrder.ORDER_AND_REQUESTS_CREATED;
                }
            }
        }
        return resultOfMethod;
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
     * @param orderStatus can be completed, canceled or in_proccesing and should be got from user
     * @return the status of method completion as ENUM
     */
    public ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck changeStatusOfOrderIncludingBooksCheck(Long id, OrderStatus orderStatus) {
        Optional<Order> optionalOrder = appContextConfig.getOrderService().getOrderById(id);
        ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck resultOfMethod
                = ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck.WRONG_ORDER_ID;

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderStatus previousStatus = order.getOrderStatus();
            if (orderStatus.equals(OrderStatus.COMPLETED)) {
                if (appContextConfig.getBookService().allBooksAreAvailable(order.getBooksInOrder())) {
                    appContextConfig.getOrderService().changeStatusOfOrder(id, orderStatus);
                }
            } else appContextConfig.getOrderService().changeStatusOfOrder(id, orderStatus);

            if (previousStatus.equals(order.getOrderStatus())) {
                resultOfMethod = ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck.ORDER_STATUS_CAN_NOT_BE_UPDATED;
            } else resultOfMethod = ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck.STATUS_UPDATED;
        }
        return resultOfMethod;
    }

    /**
     * return list of orders, sorted by param orderSort
     * if no correct sort values entered, return list sorted by id
     *
     * @param orderSort can be cost, completion_date or status and should be got from user
     * @return orders, optionally sorted by entered param
     */
    public List<Order> getOrders(OrderSort orderSort) {
        return appContextConfig.getOrderService().getSortedOrders(orderSort);
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
        Optional<Book> optionalBook = appContextConfig.getBookService().getBookById(id);
        if (optionalBook.isPresent()) {
            return Optional.of(appContextConfig.getRequestService().getNumberOfRequestsOnBook(id));
        } else return Optional.empty();
    }

    /**
     * return id of books and the number of requests on these books in descending order
     *
     * @return id of the book and number of requests on this book
     */
    public Map<Long, Long> getBooksAndNumberOfRequests() {
        Map<Long, Long> map = new HashMap<>();
        List<Book> books = appContextConfig.getRequestService().getAllBooksFromAllRequests();
        List<Book> booksDistinctSortedByNumber = books.stream().distinct().sorted(Comparator.comparing(x -> getNumberOfRequestsOnBook(x.getId()).get(), Comparator.reverseOrder())).toList();
        for (Book book : booksDistinctSortedByNumber) {
            map.put(book.getId(), getNumberOfRequestsOnBook(book.getId()).get());
        }
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
        return appContextConfig.getOrderService().getIncomeForPeriod(startOfPeriod, endOfPeriod);
    }

    /**
     * return all books from the order by order id
     *
     * @param id id of the order and should be got from user
     * @return books from the chosen order
     */
    public List<Book> getAllBooksFromOrder(Long id) {
        return appContextConfig.getOrderService().getAllBooksFromOrder(id);
    }

    /**
     * return all requests
     *
     * @return requests
     */
    public List<Request> getAllRequests() {
        return appContextConfig.getRequestService().getAllRequests();
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
