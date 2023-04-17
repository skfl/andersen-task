package com.andersentask.bookshop;

import com.andersentask.bookshop.http.HttpInterface;

public class BookshopApplication {

    public static void main(String[] args) {
        HttpInterface httpInterface = new HttpInterface();
        httpInterface.start();
    }
}
