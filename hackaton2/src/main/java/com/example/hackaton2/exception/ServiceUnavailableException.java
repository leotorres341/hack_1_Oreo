package com.example.hackaton2.exception;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String msg) { super(msg); }
}