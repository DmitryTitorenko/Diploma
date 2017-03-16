package random;

/**
 * Created by Dmitry Titorenko on 12.03.2017.
 *
 * Algorithm that yields a sequence of pseudo-randomized numbers
 */


public class LinearCongruentialGenerator {

    //first seed
    private static long seed = System.nanoTime();
    private static final long multiplier = 25214903917L;
    private static final long addend = 11;
    private static final long mask = (1L << 48) - 1;
    private static final double DOUBLE_UNIT = 0x1.0p-53; // 1.0 / (1L << 53)

    /**
     * Generates the next pseudorandom number use Linear Congruential Generator.
     *
     * @param bits random bits
     * @return the next pseudorandom value from this random number
     * generator's sequence
     */
    private static int next(int bits) {
        seed = (seed * multiplier + addend) & mask;
        return (int) (seed >>> (48 - bits));
    }

    /**
     * Returns the next pseudorandom, uniformly distributed
     *  value between  0.0 and
     * 1.0 from this random number generator's sequence.
     *
     * @return the next pseudorandom, uniformly distributed  double
     * value between  0.0 and  1.0 from this
     * random number generator's sequence
     */
    public static double nextDouble() {
        return (((long) (next(26)) << 27) + next(27)) * DOUBLE_UNIT;
    }


    private static double nextNextGaussian;
    private static boolean haveNextNextGaussian = false;

    /**
     * Returns the next pseudorandom, Gaussian ("normally") distributed
     *  value with mean and standard
     * deviation 1.0 from this random number generator's sequence.
     *
     * @return the next pseudorandom, Gaussian ("normally") distributed
     *  double value with mean  0.0 and
     * standard deviation 1.0 from this random number
     * generator's sequence
     */
    synchronized static public double nextGaussian() {
        // See Knuth, ACP, Section 3.4.1 Algorithm C.
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        } else {
            double v1, v2, s;
            do {
                v1 = 2 * nextDouble() - 1; // between -1 and 1
                v2 = 2 * nextDouble() - 1; // between -1 and 1
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
            nextNextGaussian = v2 * multiplier;
            haveNextNextGaussian = true;
            return v1 * multiplier;
        }
    }

    public static int setRange(int min, int max) {
        double next = getRandomFrom3Sigma();

        if (next > 1) {
            setRange(min, max);
        }
        return (int) (next * ((max + min)/2)/3 )+(max + min)/2;
    }

    /**
     * Don't use value that > OR < then 3Sigma
     * @return the next pseudorandom, Gaussian ("normally") distributed
     *  double value with mean  0.0 and
     * standard deviation 1.0 from this random number
     * generator's sequence witch don't surpass 3 sigma
     */
    private static double getRandomFrom3Sigma() {
        double next = nextGaussian();
        if (next < -3 || next > 3) {
            getRandomFrom3Sigma();
        }
        return next;
    }
}
