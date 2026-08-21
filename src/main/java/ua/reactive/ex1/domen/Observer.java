package ua.reactive.ex1.domen;

public interface Observer<T> {
    void observe(T event);
}