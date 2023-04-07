package com.andersentask.bookshop.order.facade;

import com.andersentask.bookshop.order.dtos.OrderDTO;

import com.andersentask.bookshop.order.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {


    // The method should make orderDTO from console info
    // It also should takes books in list
    // I will try to make in on Thursday

    public OrderDTO buildOrder(UserDTO userDTO, List<BookDTO> allBooksInShop){

        System.out.println("Please, choose wished books from list below. " +
                "You should enter name of books in one line, separated by space ");

        allBooksInShop.forEach((x) -> System.out.println(x.getName() + "/t" + x.getPrice() + "/t" +
                x.getStatus() + "/n"));

        Scanner scanner = new Scanner(System.in);
        List<String> bookNamesFromClient = Arrays.stream(scanner.nextLine().split(" "))
                .toList();
        scanner.close();

        // Check if user enters the correct data
        // The list of books in shops is filtered by names, entered by a client
        // if the received list size equals to list size of entered books by the client, the data is correct
        List<BookDTO> booksDTOToOrder = allBooksInShop.stream()
                .filter((x) -> bookNamesFromClient.contains(x.getName()))
                .toList();

        if (bookNamesFromClient.size() != booksDTOToOrder.size()) {
            throw new NoSuchBookException();
        }

        return OrderDTO.builder()
                .user(userDTO)
                .orderCost(0)  // in order still can be books, that are out_of_stock. Calculation of cost will be later
                .orderStatus(OrderStatus.IN_PROCESS)
                .timeOfCompletingOrder(LocalDateTime.now())
                .booksInOrder(booksDTOToOrder)
                .build();
    }

}
