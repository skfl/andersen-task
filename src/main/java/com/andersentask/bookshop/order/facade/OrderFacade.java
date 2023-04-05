package com.andersentask.bookshop.order.facade;

import com.andersentask.bookshop.order.dtos.OrderDTO;
import com.andersentask.bookshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.List;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class OrderFacade {


    // The method should make orderDTO from console info
    // It also should takes books in list
    // I will try to make in on Thursday

    public OrderDTO buildOrder(UserDTO userDTO, List<BookDTO> allBooks){

        System.out.println("Please, choose wished books from list below. " +
                "You should enter name of books in one line, separated by space ");

        allBooks.stream()
                .forEach((x) -> System.out.println(x.getName() + "/t" + x.getPrice() + "/t" +
                x.getStatus() + "/n"));

        Scanner scanner = new Scanner(System.in);

        String booksString = scanner.nextLine();
        String[] bookNames = booksString.split(" ");

//        for (String bookName: bookNames){
//            for (Book)
//        }

        scanner.close();
        return null;
    }

}
