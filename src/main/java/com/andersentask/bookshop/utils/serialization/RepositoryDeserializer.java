package com.andersentask.bookshop.utils.serialization;

import com.andersentask.bookshop.book.entities.Book;
import com.andersentask.bookshop.book.repositories.BookRepository;
import com.andersentask.bookshop.order.entities.Order;
import com.andersentask.bookshop.order.repositories.OrderRepository;
import com.andersentask.bookshop.request.entities.Request;
import com.andersentask.bookshop.request.repository.RequestRepository;
import com.andersentask.bookshop.user.entities.User;
import com.andersentask.bookshop.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Slf4j
public class RepositoryDeserializer {

    private static final String ERROR_MESSAGE = "Something went wrong file parsing ";
    private final ObjectMapper mapper = new ObjectMapper();

    public BookRepository deserializeAndWriteToBookRepository(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<Book> books = mapper.readValue(reader, new TypeReference<List<Book>>() {
            });
            BookRepository bookRepository = new BookRepository();
            for (Book book : books) {
                bookRepository.save(book);
            }
            return bookRepository;
        } catch (FileNotFoundException e) {
            return new BookRepository();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new IllegalStateException(ERROR_MESSAGE + filename);
        }
    }

    public UserRepository deserializeAndWriteToUserRepository(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<User> users = mapper.readValue(reader, new TypeReference<List<User>>() {
            });
            UserRepository userRepository = new UserRepository();
            for (User user : users) {
                userRepository.save(user);
            }
            return userRepository;
        } catch (FileNotFoundException e) {
            return new UserRepository();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IllegalStateException(ERROR_MESSAGE + filename);
        }
    }

    public OrderRepository deserializeAndWriteToOrderRepository(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<Order> orders = mapper.readValue(reader, new TypeReference<List<Order>>() {
            });
            OrderRepository orderRepository = new OrderRepository();
            for (Order order : orders) {
                orderRepository.save(order);
            }
            return orderRepository;
        } catch (FileNotFoundException e) {
            return new OrderRepository();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IllegalStateException(ERROR_MESSAGE + filename);
        }
    }

    public RequestRepository deserializeAndWriteToRequestRepository(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<Request> requests = mapper.readValue(reader, new TypeReference<List<Request>>() {
            });
            RequestRepository requestRepository = new RequestRepository();
            for (Request request : requests) {
                requestRepository.save(request);
            }
            return requestRepository;
        } catch (FileNotFoundException e) {
            return new RequestRepository();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IllegalStateException(ERROR_MESSAGE + filename);
        }
    }
}
