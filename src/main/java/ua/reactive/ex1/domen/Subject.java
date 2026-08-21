package ua.reactive.ex1.domen;

public interface Subject<T> {
    void registerObserver(Observer<T> observer);

    void unregisterObserver(Observer<T> observer);

    void notifyObservers(T event);
}