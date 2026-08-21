package ua.reactive.ex1.domen;

import java.util.concurrent.CopyOnWriteArraySet;

public class ConcreteSubject implements Subject<String> {

    private final CopyOnWriteArraySet<Observer<String>> observers = new CopyOnWriteArraySet<>(); // (1)

    @Override
    public void registerObserver(Observer<String> observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer<String> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event) {                              // (2)
        observers.forEach(observer -> observer.observe(event));              // (2.1)
    }
}
