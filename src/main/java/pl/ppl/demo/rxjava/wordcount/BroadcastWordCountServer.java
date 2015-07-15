package pl.ppl.demo.rxjava.wordcount;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static pl.ppl.demo.rxjava.wordcount.WordCounter.countWords;

public class BroadcastWordCountServer extends WebSocketServer {

    private PublishSubject<Count> subject = PublishSubject.create();
    private Map<WebSocket, Subscription> webSocketSubscriptionMap = new HashMap<>();



    public BroadcastWordCountServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Subscription subscription = subject.subscribe(count -> {
            conn.send(count.toString());
        });
        webSocketSubscriptionMap.put(conn, subscription);

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        webSocketSubscriptionMap.remove(conn).unsubscribe();
    }

    @Override
    public void onMessage(WebSocket conn, String message) {}

    @Override
    public void onError(WebSocket conn, Exception ex) {
        webSocketSubscriptionMap.remove(conn).unsubscribe();
        ex.printStackTrace();
    }


    public Observer<? super Count> observable() {
        return subject;
    }


    public static void main(String[] args) throws UnknownHostException {

        BroadcastWordCountServer server = new BroadcastWordCountServer(new InetSocketAddress(9000));
        server.start();

        countWords(System.in)
                .subscribeOn(Schedulers.io())
                .doOnNext(System.out::println)
                .doOnCompleted(() -> stop(server))
                .subscribe(server.observable());



    }

    private static void stop(BroadcastWordCountServer server) {
        try {
            server.stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
