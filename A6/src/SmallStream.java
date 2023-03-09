import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A simplified version of the Java library's stream interface
 */

public interface SmallStream<E> extends Iterable<E> {
    int size();

    <U> SmallStream<U> map(Function<? super E, ? extends U> fun);

    <U> U reduceR(U identity, BiFunction<U, E, U> accumulator);

    boolean allMatch(Predicate<? super E> predicate);

    boolean anyMatch(Predicate<? super E> predicate);

    SmallStream<E> filter(Predicate<? super E> predicate);

    Optional<E> min(Comparator<? super E> comparator);
}
