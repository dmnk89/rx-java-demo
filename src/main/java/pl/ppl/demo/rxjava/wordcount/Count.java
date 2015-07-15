package pl.ppl.demo.rxjava.wordcount;

import java.util.Objects;

public class Count {
    private final String word;
    private final int count;

    public Count(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public Count increment() {
        return new Count(word, count + 1);
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "{\"" + word + "\":" + count + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Count count1 = (Count) o;
        return count == count1.count &&
                Objects.equals(word, count1.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, count);
    }
}
