package com.example.demo1.module1.exceptions;

public class ZeroDetailException extends Exception{
    public ZeroDetailException() {
        System.out.println("Недопустимое количество деталей ");
    }
}
