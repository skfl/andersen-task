package com.andersentask.bookshop.common;

import java.util.concurrent.atomic.AtomicLong;

public class IdGeneration {

    public static AtomicLong bookIdGenerating = new AtomicLong(1);
    public static AtomicLong userIdGenerating = new AtomicLong(1);
    public static AtomicLong requestIdGenerating = new AtomicLong(1);
    public static AtomicLong orderIdGenerating = new AtomicLong(1);

    public static long generateBookId(){
        return bookIdGenerating.getAndIncrement();
    }

    public static long generateUserId(){
        return userIdGenerating.getAndIncrement();
    }

    public static long generateRequestId(){
        return requestIdGenerating.getAndIncrement();
    }

    public static long generateOrderId(){
        return orderIdGenerating.getAndIncrement();
    }


}
