package ua.reactive.ex1.domen;

public class ConcreteObserverA implements Observer<String> {

    @Override
    public void observe(String event) {
        System.out.println("Observer A: " + event);
    }
}