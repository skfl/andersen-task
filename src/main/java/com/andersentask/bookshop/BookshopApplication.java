package com.andersentask.bookshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookshopApplication {
    public static void main(String[] args) {
        try{
            SpringApplication.run(BookshopApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
