package ua.reactive.ex1.domen;

public class ConcreteObserverB implements Observer<String> {

    @Override
    public void observe(String event) {
        System.out.println("Observer B: " + event);
    }
}
