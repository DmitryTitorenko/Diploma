package random;

/**
 * Created by Dmitry Titorenko on 12.03.2017.
 *
 * Algorithm that yields a sequence of pseudo-randomized numbers
 */


public class LinearCongruentialGenerator {

    // IllegalArgumentException messages
    private static final String BadBound = "bound must be positive";
    private long seed = System.nanoTime();
    private static final long multiplier = 25214903917L;
    private static final long addend = 11;
    private static final long mask = (1L << 48) - 1;

    /**
     * Generates the next pseudorandom number use Linear Congruential Generator.
     *
     * @param bits random bits
     * @return the next pseudorandom value from this random number
     * generator's sequence
     */
    private int next(int bits) {
        seed = (seed * multiplier + addend) & mask;
        return (int) (seed >>> (48 - bits));
    }

    /**
     * Returns a pseudorandom, uniformly distributed {@code int} value
     * between 0 (inclusive) and the specified value (exclusive).
     *
     * @param bound the upper bound (exclusive).  Must be positive.
     * @return the next pseudorandom, uniformly distributed
     * value between zero (inclusive) and  bound (exclusive)
     * from this random number generator's sequence
     * @throws IllegalArgumentException if bound is not positive
     */
    public int nextInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException(BadBound);
        int r = next(31);
        int m = bound - 1;
        if ((bound & m) == 0)  // i.e., bound is a power of 2
            r = (int) ((bound * (long) r) >> 31);
        else {
            r = r % bound;
        }
        return r;
    }
}
