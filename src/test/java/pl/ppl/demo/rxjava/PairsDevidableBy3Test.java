package pl.ppl.demo.rxjava;


import org.junit.Test;
import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;


public class PairsDevidableBy3Test {

    @Test
    public void shouldEmitsWhenPairIs1and2() {
        //given
        int left = 1;
        int right = 2;
        Observable<Integer> first = Observable.just(left);
        Observable<Integer> second = Observable.just(right);

        //when
        Observable<Pair> result = emitPairIfDividableBy3(first, second);

        //then
        assertThat(result.toBlocking().first()).isEqualTo(new Pair(left, right));
    }

    @Test
    public void shouldEmitsWhenPairIs2and4() {
        //given
        int left = 2;
        int right = 4;
        Observable<Integer> first = Observable.just(left);
        Observable<Integer> second = Observable.just(right);

        //when
        Observable<Pair> result = emitPairIfDividableBy3(first, second);

        //then
        assertThat(result.toBlocking().first()).isEqualTo(new Pair(left, right));
    }

    @Test
    public void shouldNotEmitsWhenPairIsNotDividableby3() {
        //given
        int left = 2;
        int right = 5;
        Observable<Integer> first = Observable.just(left);
        Observable<Integer> second = Observable.just(right);

        //when
        Observable<Pair> result = emitPairIfDividableBy3(first, second);

        //then
        assertThat(result.toBlocking().getIterator()).isEmpty();
    }

    @Test
    public void testWithStreamOfData() {

        //given
        Observable<Integer> first = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Observable<Integer> second = Observable.just(1, 1, 1, 1, 1, 1, 1, 1, 1);
        Pair[] expected = new Pair[]{
                new Pair(2,1),
                new Pair(5,1),
                new Pair(8,1)
        };


        //when
        Observable<Pair> result = emitPairIfDividableBy3(first, second);

        //then
        assertThat(result.toBlocking().getIterator()).containsExactly(expected);

    }

    private Observable<Pair> emitPairIfDividableBy3(Observable<Integer> first, Observable<Integer> second) {
        return Observable
                .zip(first, second, Pair::new)
                .filter(Pair::isDividableBy3);
    }


    private static class Pair {
        final int left;
        final int right;

        private Pair(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (left != pair.left) return false;
            return right == pair.right;

        }

        @Override
        public int hashCode() {
            int result = left;
            result = 31 * result + right;
            return result;
        }

        public Boolean isDividableBy3() {
            return (left + right) % 3 == 0;
        }
    }
}