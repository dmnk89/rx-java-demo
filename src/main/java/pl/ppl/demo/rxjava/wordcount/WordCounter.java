package pl.ppl.demo.rxjava.wordcount;

import rx.Observable;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static rx.Observable.from;
import static rx.observables.StringObservable.decode;
import static rx.observables.StringObservable.from;

public class WordCounter {

    static Observable<String> readStream(InputStream stream) {
        return decode(from(stream), "UTF-8");
    }

    static Observable<String> split(Observable<String> strings) {
        return strings
                .flatMap(s -> from(s.split("\\s+")));
    }

    static Observable<Count> countWords(Observable<String> strings) {
        return strings
                .groupBy(String::toLowerCase)
                .flatMap(WordCounter::count)
                .filter(c -> c.getCount() != 0);
    }

    private static Observable<Count> count(GroupedObservable<String, String> group) {
        return group
                .scan(
                        new Count(group.getKey(), 0),
                        (count, string) -> count.increment()
                );//.debounce(100, TimeUnit.MILLISECONDS);
    }

    public static Observable<Count> countWords(InputStream in) {
        return countWords(split(readStream(in)));
    }


}
