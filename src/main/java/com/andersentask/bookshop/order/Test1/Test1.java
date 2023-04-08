package com.andersentask.bookshop.order.Test1;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.enums.BookStatus;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.facade.OrderFacade;
import com.andersentask.bookshop.order.repositories.OrderCollectionRepository;
import com.andersentask.bookshop.order.service.OrderService;
import com.andersentask.bookshop.user.entities.User;
import com.andersentask.bookshop.user.enums.Role;

import java.util.ArrayList;
import java.util.List;

//toDo:
public class Test1 {

    public static void main(String[] args) {

        OrderFacade orderFacade = new OrderFacade();
        OrderService orderService = new OrderService(new OrderCollectionRepository());

        User user1 = new User(1L,"Ulad","Sachkouski","vlad@gmail.com","123", Role.ROLE_USER);
        User user2 = new User(2L,"Nikita","Sidorov","nikita@gmail.com","1234", Role.ROLE_USER);

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"1", BookStatus.AVAILABLE,1.0));
        books.add(new Book(2L,"2", BookStatus.AVAILABLE,2.0));
        books.add(new Book(3L,"3", BookStatus.OUT_OF_STOCK,3.0));

        Order order1 = orderFacade.buildOrder(user1,books);
        Order order2 = orderFacade.buildOrder(user2,books);

        // Явно задал айди быть такими значениями, но все равно нулл выдает, собака...

        orderService.createOrder(order1);
        orderService.createOrder(order2);
        orderService.cancelOrder(0L);

        System.out.println(orderService.getAllOrders());




    }


}
