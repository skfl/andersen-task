package com.andersentask.bookshop.order.service;

import com.andersentask.bookshop.order.dtos.OrderDTO;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.enums.OrderStatus;
import com.andersentask.bookshop.order.exceptions.NoSuchOrderException;
import com.andersentask.bookshop.order.mappers.OrderMapper;
import com.andersentask.bookshop.order.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


// To rewrite to DTO

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    // We should decide how to take data from console. Thus, parameters of method can be changed
    // The method creates an order and saves it to the database
    // Before creation, method finds out books, that are out_of_stock, and calls request method
    @Override
    @Transactional
    public void createOrder(UserDTO userDTO, List<BookDTO> booksDTO) {

        User user = dtoToEntity(userDTO);
        List<Book> books = dtoToEntity(booksDTO);

        List<Book> booksToOrder = books.stream()
                .filter((x) -> x.getStatus().equals(BookStatus.AVAILABLE))
                .toList();
        List<Book> booksToRequest = books.stream()
                .filter((x) -> x.getStatus().equals(BookStatus.OUT_OF_STOCK))
                .toList();

        Double orderCost = booksToOrder.stream()
                .map(Book::getPrice)
                .reduce(0D,Double::sum);


        orderRepository.save(Order.builder()
                .user(user)
                .orderCost(orderCost)
                .orderStatus(OrderStatus.IN_PROCESS)
                .timeOfCompletingOrder(LocalDateTime.now())
                .booksInOrder(booksToOrder)
                .build());

        makeRequest(booksToRequest); // Method to create request from order
    }

    // The method takes request and adds it to the order with status "in process"
    // The method does not check, if books are available or not
    @Override
    @Transactional
    public void createOrderFromRequest(RequestDTO requestDTO){

        Request request = dtoToEntity(requestDTO);

        double requestCost = request.getBooksInRequest().stream()
                        .map(Book::getPrice)
                        .reduce(0D,Double::sum);

        orderRepository.save(Order.builder()
                .user(request.getUser())
                .orderCost(requestCost)
                .orderStatus(OrderStatus.IN_PROCESS)
                .timeOfCompletingOrder(LocalDateTime.now())
                .booksInOrder(request.getBooksInRequest())
                .build());
    }

    // It can be done easier, I think... how better to rewrite the code?
    @Override
    @Transactional
    public void completeOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new NoSuchOrderException();
        }
        Order order = orderRepository.findById(id).get();
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setTimeOfCompletingOrder(LocalDateTime.now());

        // If the order is completed, books' status should be changed to out_of_stock
        order.getBooksInOrder().stream().forEach((x) -> x.setBookStatus(BookStatus.OUT_OF_STOCK));

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) throws NoSuchOrderException {
        if (!orderRepository.existsById(id)) {
            throw new NoSuchOrderException();
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<OrderDTO> getAllOrders() {
        return OrderMapper.entityListToDtoList(orderRepository.findAll());
    }

    @Override
    @Transactional
    public String getInfoAboutOrders() {
        StringBuilder info = new StringBuilder();
        List<OrderDTO> orders = getAllOrders().stream().toList();
        for (OrderDTO orderDTO : orders) {
            info.append(orderDTO.getUserDTO().getId()).append(orderDTO.getBooksInOrder().toString()).append("/n");
        }
        return info.toString();
    }

    @Override
    @Transactional
    public double getIncomeForPeriod(LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {

        return getAllOrders().stream()
                .filter((x) -> x.getOrderStatus().equals(OrderStatus.COMPLETED))
                .filter((x) -> x.getTimeOfCompletingOrder().isAfter(startOfPeriod) &&
                        x.getTimeOfCompletingOrder().isBefore(endOfPeriod))
                .map(OrderDTO::getOrderCost)
                .reduce(0D, Double::sum);
    }

    @Override
    @Transactional
    public List<OrderDTO> getOrdersSorted() {
        return getAllOrders().stream()
                .sorted(Comparator.comparing(OrderDTO::getOrderCost, Comparator.reverseOrder())
                        .thenComparing(OrderDTO::getTimeOfCompletingOrder, Comparator.reverseOrder())
                        .thenComparing(OrderDTO::getOrderStatus))
                .toList();
    }

    @Override
    @Transactional
    public List<OrderDTO> getOrdersSortedByCost() {
        return getAllOrders().stream()
                .sorted(Comparator.comparing(OrderDTO::getOrderCost, Comparator.reverseOrder()))
                .toList();
    }

    @Override
    @Transactional
    public List<OrderDTO> getOrdersSortedByDate() {
        return getAllOrders().stream()
                .sorted(Comparator.comparing(OrderDTO::getTimeOfCompletingOrder, Comparator.reverseOrder()))
                .toList();
    }

    @Override
    @Transactional
    public List<OrderDTO> getOrdersSortedByStatus() {
        return getAllOrders().stream()
                .sorted(Comparator.comparingInt(x -> x.getOrderStatus().getOrdinalOfOrderEnum()))
                .toList();
    }


}
