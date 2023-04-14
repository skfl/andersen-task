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
    private final RepositorySerializer serializer = new RepositorySerializer();

    /**
     * Set status of book
     * This method also deletes all requests on book,
     * if it's status changed from out_of_stock to available
     *
     * @param id         the id of the book and should be got from user
     * @param bookStatus bookStatus of the book and got from user
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
     * Create and save request from a book (doesn't matter, what status)
     *
     * @param id book object and should be got from user
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
     *
     * @param bookSort can be name, price or status and should be got from user
     * @return new list of books, sorted by entered param
     */
    public List<Book> getSortedBooks(BookSort bookSort) {
        return appContextConfig.getBookService().getSortedBooks(bookSort);
    }

    /**
     * Create and save order from a list of id of books
     * If book is out_of_stock, also created ans saves request
     * if any id is invalid, the order and request are not created
     *
     * @param ids list of id of books and should be got from user
     * @return Optional<Order> returns empty optional empty, if order is not created
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
     * set status of the order to chosen by the user:
     * completed: only if all books are available. Also setting time of completion
     * cancelled: only if the current status of order is in_processing
     * in_processing: only if the current status of order us cancelled
     *
     * @param id          id of order and should be got from user
     * @param orderStatus can be completed, cancelled or in_proccesing and should be got from user
     * @return ENUM       returns status of operation
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
     *
     * @param orderSort can be cost, completion_date or status and should be got from user
     * @return new list of orders, sorted by entered param
     */
    public List<Order> getOrders(OrderSort orderSort) {
        return appContextConfig.getOrderService().getSortedOrders(orderSort);
    }

    /**
     * return number of the requests on the book
     *
     * @param id id of the book and should be got from user
     * @return Long number of the request on the precise book
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
     * @return Map<Long, Long> id of the book and number of requests on this book
     */
    public Map<Long, Long> getBooksAndNumberOfRequests() {
        Map<Long, Long> map = new HashMap<>();
        List<Book> books = appContextConfig.getRequestService().getAllBooksFromRequests();
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
     * @return double income for the chosen period
     */
    public BigDecimal getIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {
        return appContextConfig.getOrderService().getIncomeForPeriod(startOfPeriod, endOfPeriod);
    }

    /**
     * return all books from the order
     *
     * @param id id of the order and should be got from user
     * @return List<BooK> list of books
     */
    public Optional<List<Book>> getAllBooksFromOrder(Long id) {
        Optional<Order> optionalOrder = appContextConfig.getOrderService().getOrderById(id);
        if (optionalOrder.isPresent()) {
            return Optional.of(appContextConfig.getOrderService().getAllBooksFromOrder(id));
        } else return Optional.empty();

    }

    public List<Request> getAllRequests() {
        return appContextConfig.getRequestService().getAllRequests();
    }

    public void exit() {
        serializer.serializeAndWriteToFile(appContextConfig.getBookService().getAllBooks(), "books.json");
        serializer.serializeAndWriteToFile(appContextConfig.getOrderService().getAllOrders(), "orders.json");
        serializer.serializeAndWriteToFile(appContextConfig.getRequestService().getAllRequests(), "requests.json");
        System.exit(0);
    }
}
