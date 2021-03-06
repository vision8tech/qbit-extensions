package io.advantageous.qbit.example.errors;

import io.advantageous.boon.core.Sys;
import io.advantageous.qbit.annotation.RequestMapping;
import io.advantageous.qbit.reactive.Callback;
import io.advantageous.qbit.reactive.CallbackBuilder;
import io.advantageous.qbit.service.ServiceBundle;
import io.advantageous.qbit.service.ServiceBundleBuilder;

import static io.advantageous.boon.core.IO.puts;

/**
 * Created by rick on 6/8/15.
 */
public class ErrorHandlingServiceBundle {

    @RequestMapping("/my")
    public static class MyService {

        public boolean methodThatThrowsError() {
            puts("Method that throws error");
            throw new IllegalStateException("ERROR");
        }


        public void methodThatThrowsError2(Callback<Boolean> callback) {
            puts("Method that throws error");
            throw new IllegalStateException("ERROR");
        }

        public boolean regularMethod() {
            puts("Regular method");
            return true;
        }
    }


    public static interface IMyService {
        void methodThatThrowsError(Callback<Boolean> callback);
        void regularMethod(Callback<Boolean> callback);
        void methodThatThrowsError2(Callback<Boolean> callback);

    }


    public static void main(final String... args) {

        final ServiceBundle serviceBundle = ServiceBundleBuilder
                .serviceBundleBuilder()
                .build().addService(new MyService());

        serviceBundle.startServiceBundle();

        IMyService myService = serviceBundle.createLocalProxy(IMyService.class, "myservice");

        myService.regularMethod(
                CallbackBuilder.callbackBuilder().setCallback(Boolean.class,
                        aBoolean -> {
                            puts("Custom callback handler aaa", aBoolean);
                        }
                )
                        .setOnError(throwable ->
                                {
                                    puts("Custom error handler aaa", throwable);
                                }
                        ).build()
        );


        myService.methodThatThrowsError(
                CallbackBuilder.callbackBuilder().setCallback(Boolean.class,
                        aBoolean -> puts("Custom callback handler ccc", aBoolean))
                        .setOnError(throwable ->
                                {
                                    puts("Custom error handler ccc", throwable);
                                }
                        ).build()
        );

        serviceBundle.flushSends();

        Sys.sleep(2000);

//
//        client.methodThatThrowsError(new Callback<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) {
//
//                //Never called
//                puts("Called back 1.. callback is here", aBoolean);
//            }
//        });
//        puts("Called back 1");
//
//        /* Since the method throws an exception
//         the callback is never called.
//         */
//
//        //No way to catch it. No callback.
//
//        client.methodThatThrowsError2(new Callback<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) {
//                //Never called
//                puts("Called back 2 callback 2", aBoolean);
//            }
//        });
//
//        puts("Called back 2");
//
//
//        /* The default error handling will just output
//           errors to the log.
//           For many services where you don't expect exception
//           or services where there is nothing you can do with exceptions
//           if they do occur that is exactly what you want.
//           If all you are going to do is log it, then
//           we handle that already.
//           However, sometimes you can recover.
//           Sometimes, you want to log additional context, so...
//         */
//
//        /* Now to actually handle the error. */
//        client.methodThatThrowsError(new Callback<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) {
//
//                //Never called
//                puts("Called back 1.. callback is here", aBoolean);
//            }
//
//            @Override
//            public void onError(Throwable error) {
//                //THIS GETS CALLED!
//                puts("This gets called!", error);
//            }
//
//        });
//
//
//        /* Now to actually handle the error. */
//        client.methodThatThrowsError2(new Callback<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) {
//                 //NEVER CALLED
//                puts("Called back 1.. callback is here", aBoolean);
//            }
//
//            @Override
//            public void onError(Throwable error) {
//                //THIS GETS CALLED!
//                puts("This gets called!", error);
//            }
//
//            /**
//             * You can also handle timeouts btw.
//             */
//            @Override
//            public void onTimeout() {
//                puts("This does not but can you imagine when it would");
//            }
//
//        });
//
//        /* The moral of the story is that if you want to handle
//         * the error, you must register a callback.
//         *
//         * In order to register a callback, you must have a method
//         * that has a callback as the first param or a method
//         * that returns a value.
//         */
//
//        /*
//         * So far we have been using inner classes.
//         * Let show an example on how to do the same with lambda.
//         */
//
//        final Reactor reactor = new Reactor(Timer.timer(), 10_000, TimeUnit.MILLISECONDS);
//        final CallbackBuilder callbackBuilder = CallbackBuilder.callbackBuilder(reactor);
//
//
//        callbackBuilder.setCallback(Boolean.class,
//                aBoolean ->
//                {
//                    puts("Return from method", aBoolean);
//                });
//
//        callbackBuilder.setOnError(throwable
//                -> {
//            puts("I customized the way I am handling errors");
//        });
//
//
//        client.regularMethod(callbackBuilder.build());
//        client.methodThatThrowsError(callbackBuilder.build());
//        client.methodThatThrowsError2(callbackBuilder.build());
//
//        /** A reactor can handle multiple callbacks and timeouts, and errors. */
//
//        for (int index =0; index<10; index++) {
//            Sys.sleep(10);
//            reactor.process();
//        }
//
//        /** You use a reactor inside of a services @QueueCallback handler for limit, empty and idle. */
//
//        Sys.sleep(1000);
//
//
//        /**
//         * I just added this.
//         * You can now use callback builder without reactor.
//         * Use this when you want to use things outside of a service.
//         */
//
//        final CallbackBuilder callbackBuilder2 = CallbackBuilder.callbackBuilder();
//
//
//        callbackBuilder2.setCallback(Boolean.class,
//                aBoolean ->
//                {
//                    puts("Return from method 2", aBoolean);
//                });
//
//        callbackBuilder2.setOnError(throwable
//                -> {
//            puts("I customized the way I am handling errors 2");
//        });
//
//        client.regularMethod(callbackBuilder2.build());
//        client.methodThatThrowsError(callbackBuilder2.build());
//        client.methodThatThrowsError2(callbackBuilder2.build());

    }

}
