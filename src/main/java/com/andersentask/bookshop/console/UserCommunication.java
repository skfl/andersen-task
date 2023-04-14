package com.andersentask.bookshop.console;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookSort;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.console.enums.ResultOfOperation;
import com.andersentask.bookshop.order.enums.OrderSort;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.request.entities.Request;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class UserCommunication {

    private final Commands commands;

    public void help() {
        log.info(DefaultMessages.HELP_MESSAGE.getValue());
    }

    public void getBooks(List<String> input) {
        BookSort bookSort;
        try {
            bookSort = BookSort.valueOf(input.get(0).toUpperCase());
            printList(commands.getSortedBooks(bookSort));
        } catch (RuntimeException e) {
            printList(commands.getSortedBooks(BookSort.ID));
        }
    }

    public void getOrders(List<String> input) {
        OrderSort orderSort;
        try {
            orderSort = OrderSort.valueOf(input.get(0).toUpperCase());
            printList(commands.getOrders(orderSort));
        } catch (RuntimeException e) {
            printList(commands.getOrders(OrderSort.ID));
        }
    }

    public void getRequests() {
        List<Request> requests = commands.getAllRequests();
        printList(requests);
    }

    public void changeBookStatus(List<String> input) {
        long id;
        BookStatus bookStatus;
        try {
            id = Long.parseLong(input.get(0));
            bookStatus = BookStatus.valueOf(input.get(1).toUpperCase());
            ResultOfOperation.SetBookStatus result = commands.setStatusToBookAndDeleteCorrespondingRequests(id, bookStatus);
            log.info(result.toString());
        } catch (RuntimeException e) {
            log.info(ResultOfOperation.SetBookStatus.INCORRECT_ENTRANCE_OF_BOOK_ID_OR_BOOK_STATUS.toString());
        }
    }

    public void createRequest(List<String> input) {
        long id;
        try {
            id = Long.parseLong(input.get(0));
            ResultOfOperation.CreateRequest result = commands.createRequest(id);
            log.info(result.toString());
        } catch (Exception e) {
            log.info(ResultOfOperation.CreateRequest.INCORRECT_ENTRANCE_OF_BOOK_ID.toString());
        }
    }

    public void createOrder(List<String> input) {
        List<Long> ids;
        try {
            ids = input.stream().map(Long::parseLong).toList();
            ResultOfOperation.CreateOrder result = commands.createOrder(ids);
            log.info(result.toString());
        } catch (RuntimeException e) {
            log.info(ResultOfOperation.CreateOrder.INCORRECT_ENTRANCE_OF_BOOK_ID.toString());
        }
    }

    public void changeOrderStatus(List<String> input) {
        long id;
        OrderStatus orderStatus;
        try {
            id = Long.parseLong(input.get(0));
            orderStatus = OrderStatus.valueOf(input.get(1).toUpperCase());
            ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck result =
                    commands.changeStatusOfOrderIncludingBooksCheck(id, orderStatus);
            log.info(result.toString());
        } catch (RuntimeException e) {
            log.info(ResultOfOperation.ChangeStatusOfOrderIncludingBooksCheck
                    .INCORRECT_ENTRANCE_OF_ORDER_ID_OR_BOOK_STATUS.toString());
        }
    }

    public void getNumberOfRequestsOnBook(List<String> input) {
        long id;
        try {
            id = Long.parseLong(input.get(0));
            Optional<Long> count = commands.getNumberOfRequestsOnBook(id);
            if (count.isPresent()) {
                log.info(count.get().toString());
            } else log.info(ResultOfOperation.GetNumberOfRequestsOnBook.WRONG_BOOK_ID.toString());
        } catch (RuntimeException e) {
            log.info(ResultOfOperation.GetNumberOfRequestsOnBook.INCORRECT_ENTRANCE_OF_BOOK_ID.toString());
        }
    }

    public void getBooksAndNumberOfRequests() {
        Map<Long, Long> map = commands.getBooksAndNumberOfRequests();
        if (map.isEmpty()) {
            log.info(ResultOfOperation.GetBooksAndNumberOfRequests.NO_REQUESTS.toString());
        } else {
            log.info(map.toString());
        }
    }

    public void getIncomeForPeriod(List<String> input) {
        LocalDateTime ltd1;
        LocalDateTime ldt2;
        try {
            ltd1 = LocalDateTime.parse(input.get(0));
            ldt2 = LocalDateTime.parse(input.get(1));
            double income = commands.getIncomeForPeriod(ltd1, ldt2);
            log.info(Double.toString(income));
        } catch (RuntimeException e) {
            log.info(ResultOfOperation.getIncomeForPeriod.WRONG_DATE.toString());
        }
    }

    public void getAllBooksFromOrder(List<String> input) {
        long id;
        try {
            id = Long.parseLong(input.get(0));
            Optional<List<Book>> optionalBooks = commands.getAllBooksFromOrder(id);
            if (optionalBooks.isPresent()) {
                printList(optionalBooks.get());
            } else {
                log.info(ResultOfOperation.GetAllBooksFromOrder.WRONG_ORDER_ID.toString());
            }
        } catch (RuntimeException e) {
            log.info(ResultOfOperation.GetAllBooksFromOrder.WRONG_ORDER_ID.toString());
        }
    }

    public void exit() {
        commands.exit();
    }

    private <T> void printList(List<T> list) {
        int count = 1;
        for (T el : list) {
            System.out.println(count + ") " + el.toString());
            count++;
        }
    }
}
