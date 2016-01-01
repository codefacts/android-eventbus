import codefacts.*;

/**
 * Created by sohan on 1/1/2016.
 */
public class ReqSubscriber implements Subscriber {
    public void onReq(ReqEvent reqEvent) {
        reqEvent.progress(1, 2);
        reqEvent.progress(3);
        reqEvent.progress(4);
        reqEvent.fail(new NullPointerException("ok"));
//        System.out.println("On Request1 " + reqEvent);
//        throw new RuntimeException("From Req");
    }

    public void onNotify(Notification event) {
//        System.out.println("Notified: " + event);
//        throw new RuntimeException("From Notifited");
    }

//    public void onReq2(ReqEvent reqEvent) {
//        reqEvent.progress(1, 2);
//        reqEvent.progress(3);
//        reqEvent.progress(4);
//        reqEvent.reply(new Message() {
//            private String message = "good";
//
//            @Override
//            public void onReply(Message event) {
//
//            }
//
//            @Override
//            public void onFail(Throwable e) {
//
//            }
//
//            @Override
//            public String toString() {
//                return "$classname{" +
//                        "message='" + message + '\'' +
//                        '}';
//            }
//        });
//        System.out.println("On Request2 " + reqEvent);
//    }
//
//    public void onReq3(ReqEvent reqEvent) {
//        reqEvent.reply(new Message() {
//            String name = "That's good";
//
//            @Override
//            public String toString() {
//                return "$classname{" +
//                        "name='" + name + '\'' +
//                        '}';
//            }
//
//            @Override
//            public void onReply(Message event) {
//
//            }
//
//            @Override
//            public void onFail(Throwable e) {
//
//            }
//        });
//        System.out.println("On Request3 " + reqEvent);
//    }

    public static void main(String... args) {
        EventBus.getDefault().subscribe(new ReqSubscriber());
        EventBus.getDefault().send(new ReqEvent(1));

        EventBus.getDefault().publish(new Notification());
//        EventBus.getDefault().send(new ReqEvent(1));

        long t1 = System.nanoTime();
        for (int i = 0; i < 10000000; i++) {
            EventBus.getDefault().send(new ReqEvent(1));

            EventBus.getDefault().publish(new Notification());
        }
        long t2 = System.nanoTime();
        System.out.println("TIME: " + (t2 - t1) / 1000000);
    }
}
