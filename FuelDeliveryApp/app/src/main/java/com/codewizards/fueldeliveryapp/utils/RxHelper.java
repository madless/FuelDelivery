//package com.codewizards.fueldeliveryapp.utils;
//
//import android.util.Log;
//
//import rx.Observable;
//import rx.Subscriber;
//import rx.observers.TestSubscriber;
//
///**
// * Created by dmikhov on 21.10.2016.
// */
//public class RxHelper {
//    static Logger logger = Logger.getLogger(RxHelper.class);
//    public static void testSchedulersTemplate(Observable.Transformer<String, String> transformer) {
//        Observable<String> obs = Observable
//                .create(subscriber -> {
//                    logger.d("Inside observable");
//                    subscriber.onNext("Hello from observable");
//                    subscriber.onCompleted();
//                })
//                .doOnNext(s -> logger.d("Before transform"))
//                .compose(transformer)
//                .doOnNext(s -> logger.d("After transform"));
//        TestSubscriber<String> subscriber = new TestSubscriber<>(new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//                logger.d("In onComplete");
//            }
//
//            @Override
//            public void onError(Throwable e) {}
//
//            @Override
//            public void onNext(String o) {
//                logger.d("In onNext");
//            }
//        });
//        obs.subscribe(subscriber);
//        subscriber.awaitTerminalEvent();
//    }
//}
