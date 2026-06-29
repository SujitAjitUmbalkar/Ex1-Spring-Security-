package com.example.demo4.SecurityApp.exceptions;


public class NoSuchResourcesException extends RuntimeException
{
    public NoSuchResourcesException(String message)
    {
        super(message);
    }
}
