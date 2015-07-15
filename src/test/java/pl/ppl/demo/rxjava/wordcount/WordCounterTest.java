package pl.ppl.demo.rxjava.wordcount;

import org.junit.Test;
import rx.Observable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class WordCounterTest {


    @Test
    public void shouldReadDataFromStream() throws IOException {
        //given
        String content = "some not very complicated content ðŋæðś’←¢€²½œęćŋęə³€€„←²€¢¢ßę";
        InputStream in = new ByteArrayInputStream(content.getBytes());

        //when
        Observable<String> result = WordCounter.readStream(in);

        //then
        assertThat(result.toBlocking().first()).isEqualTo(content);

    }


    @Test
    public void shouldSplitWords() {
        //given
        Observable<String> strings = Observable.just("some not\t very\n\r\n\r\r\n complicated content");
        String[] expected = {"some", "not", "very", "complicated", "content"};

        //when
        Observable<String> result = WordCounter.split(strings);

        //then
        assertThat(result.toBlocking().getIterator()).containsExactly(expected);
    }

    @Test
    public void shouldCountWords() {
        //given
        Observable<String> strings = Observable.from(asList("some", "not", "very", "complicated", "content", "and", "some", "other", "not", "complicated", "words", "words", "words"));
        Count[] expected = (
                new Count[]{
                        new Count("some", 1),
                        new Count("not", 1),
                        new Count("very", 1),
                        new Count("complicated", 1),
                        new Count("content", 1),
                        new Count("and", 1),
                        new Count("some", 2),
                        new Count("other", 1),
                        new Count("not", 2),
                        new Count("complicated", 2),
                        new Count("words", 1),
                        new Count("words", 2),
                        new Count("words", 3)
                });

        //when
        Observable<Count> result = WordCounter.countWords(strings);

        //then
        assertThat(result.toBlocking().getIterator()).containsExactly(expected);

    }
}