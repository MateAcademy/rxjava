package ua.reactive.ex1;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ua.reactive.ex1.domen.ConcreteObserverA;
import ua.reactive.ex1.domen.ConcreteObserverB;
import ua.reactive.ex1.domen.ConcreteSubject;
import ua.reactive.ex1.domen.Observer;
import ua.reactive.ex1.domen.Subject;

import static org.mockito.Mockito.times;

class ObserverPatternTest {

    @Test
    void observersHandleEventsFromSubject() {
        // дано
        Subject<String> subject = new ConcreteSubject();
        Observer<String> observerA = Mockito.spy(new ConcreteObserverA());
        Observer<String> observerB = Mockito.spy(new ConcreteObserverB());

        // если
        subject.notifyObservers("No listeners");

        subject.registerObserver(observerA);
        subject.notifyObservers("Message for A");

        subject.registerObserver(observerB);
        subject.notifyObservers("Message for A & B");

        subject.unregisterObserver(observerA);
        subject.notifyObservers("Message for B");

        subject.unregisterObserver(observerB);
        subject.notifyObservers("No listeners");

        // тогда
        Mockito.verify(observerA, times(1)).observe("Message for A");
        Mockito.verify(observerA, times(1)).observe("Message for A & B");
        Mockito.verifyNoMoreInteractions(observerA);

        Mockito.verify(observerB, times(1)).observe("Message for A & B");
        Mockito.verify(observerB, times(1)).observe("Message for B");
        Mockito.verifyNoMoreInteractions(observerB);
    }
}
