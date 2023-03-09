import java.math.BigInteger;
import java.util.Stack;

/**
 * This class overrides the solutions in DP with new methods that
 * manage an appropriate hash map (as implemented in our previous
 * assignment in the class HM).
 *
 * Each method will have its own hash map.
 */

public class DPHM extends DP {
    private HM<Integer,BigInteger> fibHM;
    private HM<Integer, Boolean> sumHM;

    DPHM () {
        fibHM = new HM<>(11);
        sumHM = new HM<>(11);
    }

    // fib from lecture as a template
    @Override
    BigInteger fib (int n) {
        if (fibHM.containsKey(n)) {
            return fibHM.get(n);
        }
        else {
            BigInteger r = super.fib(n);
            fibHM.put(n,r);
            return r;
        }
    }

    @Override
    boolean subsetSum (Stack<Integer> s, int sum) {
        int getInt = 0;
        int newSum = 0;
        boolean sumExists = false;

        System.out.println(sum);

        if (sumHM.containsKey(0)) {
            sumHM.clear();
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
                    sumHM.put(newSum, sumExists);
                    sumExists = subsetSum(s, newSum);
                    s.add(getInt);
                }
            } else {
                if (s.get(i) >= sum) {
                    getInt = s.get(i);
                    s.remove(i);
                    newSum = sum + getInt;
                    sumHM.put(newSum, sumExists);
                    sumExists = subsetSum(s, newSum);
                    s.add(getInt);
                }
            }
            if (sumExists) {
                break;
            }
        }

        return sumExists; // TODO
    }
}
