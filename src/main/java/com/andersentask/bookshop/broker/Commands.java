package com.andersentask.bookshop.broker;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.book.services.BookService;
import com.andersentask.bookshop.broker.enums.ResultOfOperation;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.order.service.OrderService;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Commands {

    private final RequestService requestService;

    private final OrderService orderService;

    private final BookService bookService;

    private final EntityFactory entityFactory;


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
        return bookService
                .getBookById(bookId)
                .map(book -> setBookStatus(book, bookStatus))
                .orElse(ResultOfOperation.SetBookStatus.WRONG_BOOK_ID);
    }

    private ResultOfOperation.SetBookStatus setBookStatus(Book book, BookStatus bookStatus) {
        if (book.getStatus() == bookStatus) {
            return ResultOfOperation.SetBookStatus.BOOK_ALREADY_HAS_THIS_STATUS;
        }
        bookService.setStatusToBook(book.getId(), bookStatus);
        return ResultOfOperation.SetBookStatus.BOOK_STATUS_UPDATED;
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
        return bookService
                .getBookById(bookId)
                .map(this::createRequestFromBook)
                .orElse(ResultOfOperation.CreateRequest.WRONG_BOOK_ID);
    }

    private ResultOfOperation.CreateRequest createRequestFromBook(Book book) {
        Request request = entityFactory.buildRequest(book);
        requestService.saveRequest(request);
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
        return bookService.getSortedBooks(bookSort);
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
        List<Book> booksToOrder = bookService.getBooksByIds(ids);
        if (booksToOrder.size() != ids.size()) {
            return ResultOfOperation.CreateOrder.WRONG_BOOK_ID;
        }
        Order order = entityFactory.buildOrder(booksToOrder);
        return createOrderAndRequestsIfOrderHasOutOfStockBooks(order, booksToOrder);
    }

    private ResultOfOperation.CreateOrder createOrderAndRequestsIfOrderHasOutOfStockBooks(Order order, List<Book> booksToOrder) {
        orderService.saveOrder(order);
        List<Book> booksToRequest = bookService.getBooksOutOfStock(booksToOrder);
        if (!booksToRequest.isEmpty()) {
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
        return orderService
                .getOrderById(id)
                .map(order -> changeStatusOfOrder(order, orderStatus))
                .orElse(ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck.WRONG_ORDER_ID);
    }

    private ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck changeStatusOfOrder(Order order, OrderStatus orderStatus) {
        if (orderHasSameStatus(order, orderStatus)) {
            return ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck.ORDER_ALREADY_HAS_THIS_STATUS;
        }
        if (orderStatusNotToBeCompletedOrAllBooksAvailable(order, orderStatus)) {
            order = orderService.changeStatusOfOrder(order.getId(), orderStatus);
        }
        return orderStatusUpdateWasSuccessful(order, orderStatus);
    }

    private boolean orderHasSameStatus(Order order, OrderStatus orderStatus) {
        return order.getStatus() == orderStatus;
    }

    private boolean orderStatusNotToBeCompletedOrAllBooksAvailable(Order order, OrderStatus orderStatus) {
        boolean allBooksAvailable = bookService.allBooksAreAvailable(order.getBooks());
        return orderStatus != OrderStatus.COMPLETED || allBooksAvailable;
    }

    private ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck orderStatusUpdateWasSuccessful(Order order, OrderStatus orderStatus) {
        return order.getStatus() == orderStatus ?
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
    public List<Order> getSortedOrders(OrderSort orderSort) {
        return orderService.getSortedOrders(orderSort);
    }

    /**
     * return optional of number of the requests on the book
     * if the value of the optional is set, that means, that book id is entered correct
     * otherwise, the value of the optional is empty (means, book id is invalid)
     *
     * @param id id of the book and should be got from user
     * @return number of the request on the precise book
     */
    public Long getNumberOfRequestsOnBook(Long id) {
        return bookService
                .getBookById(id)
                .map(requestService::getNumberOfRequestsOnBook)
                .orElse(0L);
    }

    /**
     * return id of books and the number of requests on these books in descending order
     *
     * @return id of the book and number of requests on this book
     */
    public Map<Long, Long> getBooksAndNumberOfRequests() {
        Map<Long, Long> map = new HashMap<>();
        requestService.getAllBooksFromAllRequests()
                .stream()
                .distinct()
                .sorted(Comparator.comparing(book -> getNumberOfRequestsOnBook(book.getId()),
                        Comparator.reverseOrder()))
                .forEachOrdered(book -> map.put(book.getId(),
                        getNumberOfRequestsOnBook(book.getId())));
        return map;
    }

    /**
     * return order by Order id
     *
     * @param orderId id of the order and should be got from user
     * @return Order object
     */
    public Order getOrderById(Long orderId) {
        return orderService.getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("No order with such ID"));
    }

    /**
     * return all requests
     *
     * @return requests
     */
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

}
