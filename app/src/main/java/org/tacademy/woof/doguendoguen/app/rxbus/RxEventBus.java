package org.tacademy.woof.doguendoguen.app.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Tak on 2017. 8. 12..
 */

public class RxEventBus {
    private static RxEventBus instance;
    private PublishSubject<Object> bus = PublishSubject.create();

    private RxEventBus() {
    }

    public static RxEventBus getInstance() {
        if(instance == null)
            instance = new RxEventBus();

        return instance;
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public void send(Object o) {
        bus.onNext(o);
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
