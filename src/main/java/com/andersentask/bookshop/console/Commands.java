package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.request.entities.Request;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;


public class Commands {

    private final ConsoleAppContextConfig appContextConfig = new ConsoleAppContextConfig();


    /**
     * Set status of book
     * This method also deletes all requests on book,
     * if it's status changed from out_of_stock to available
     *
     * @param id         the id of the book and should be got from user
     * @param bookStatus bookStatus of the book and got from user
     */
    public void setStatusToBookAndDeleteCorrespondingRequests(Long id, BookStatus bookStatus) {

        Optional<Book> optionalBook = appContextConfig.getBookService().setStatusToBook(id,bookStatus);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (book.getStatus().equals(BookStatus.OUT_OF_STOCK) && bookStatus.equals(BookStatus.AVAILABLE)) {
                appContextConfig.getRequestService().deleteRequest(book);
            }
        }
    }

    /**
     * Create and save request from a book (doesn't matter, what status)
     *
     * @param id book object and should be got from user
     */
    public void createRequest(Long id) {
        Optional<Book> optionalBook = appContextConfig.getBookService().getBookById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            Request request = appContextConfig.getEntityFactory().buildRequest(book);
            appContextConfig.getRequestService().saveRequest(request);
        }
    }

    /**
     * return list of books, sorted by param bookSort
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
     */
    public void createOrder(List<Long> ids) {
        List<Book> booksToOrder = appContextConfig.getBookService().getBooksByIds(ids);

        if (booksToOrder.size() == ids.size()) {
            Order order = appContextConfig.getEntityFactory().buildOrder(booksToOrder);
            appContextConfig.getOrderService().saveOrder(order);

            List<Book> booksToRequest = appContextConfig.getBookService().getBooksOutOfStock(booksToOrder);
            booksToRequest.forEach(x -> appContextConfig.getEntityFactory().buildRequest(x));
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

            if (orderStatus.equals(OrderStatus.COMPLETED)) {
                boolean allBooksAreAvailable = appContextConfig.getBookService().allBooksAreAvailable(order.getBooksInOrder());
                if (allBooksAreAvailable){
                    appContextConfig.getOrderService().changeStatusOfOrder(id,orderStatus);
                }
            } else {
                appContextConfig.getOrderService().changeStatusOfOrder(id, orderStatus);
            }
        }
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
    public Long getNumberOfRequestsOnBook(Long id) {
        return appContextConfig.getRequestService().getNumberOfRequestsOnBook(id);
    }

    /**
     * return id of books and the number of requests on these books in descending order
     *
     * @return Map<Long, Long> id of the book and number of requests on this book
     */
    public Map<Long, Long> getBooksAndNumberOfRequests() {
        Map<Long, Long> map = new HashMap<>();
        List<Book> books = appContextConfig.getRequestService().getAllBooksFromRequests();
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
        return appContextConfig.getOrderService().getIncomeForPeriod(startOfPeriod,endOfPeriod);
    }

    /**
     * return all books from the order
     *
     * @param id id of the order and should be got from user
     * @return List<BooK> list of books
     */
    public List<Book> getAllBooksFromOrder(Long id) {
        return appContextConfig.getOrderService().getAllBooksFromOrder(id);
    }



}
