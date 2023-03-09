import java.math.BigInteger;
import java.util.Stack;

/**
 * This class contains the recursive solutions for the
 * dynamic programming problems
 *
 * These solutions will only work for small problems as they
 * involve many repeated subcomputations.
 */

public class DP {
    // fib from lecture repeated here as an template
    BigInteger fib (int n) {
        if (n < 2) return BigInteger.valueOf(n);
        else return fib(n-1).add(fib(n-2));
    }

    /**
     * Take a stack of numbers, a desired sum 'sum', and
     * return T/F depending on whether it is possible to use some
     * of the numbers in the stack to produce the desired sum.
     */
    boolean subsetSum (Stack<Integer> s, int sum) {
        int getInt = 0;
        int newSum = 0;
        boolean sumExists = false;

        if (s.contains(sum)) {
            return true;
        } else if (s.isEmpty() && sum == 0) {
            return true;
        } else if (s.isEmpty()) {
            return false;
        }

        for (int i = 0; i < s.size(); i++) {
            if (sum > 0) {
                if (s.get(i) <= sum) {
                    getInt = s.get(i);
                    s.remove(i);
                    newSum = sum - getInt;
                    sumExists = subsetSum(s, newSum);
                    s.add(getInt);
                }
            } else {
                if (s.get(i) >= sum) {
                    getInt = s.get(i);
                    s.remove(i);
                    newSum = sum + getInt;
                    sumExists = subsetSum(s, newSum);
                    s.add(getInt);
                }
            }
        }

        return sumExists; // TODO
    }

}
