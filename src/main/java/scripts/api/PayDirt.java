package scripts.api;

/**
 * Purpose of class: Used for controlling the workers pay-dirt sack, cannot instantiate class.
 *
 * Author: Polymorphic
 * Date: September 4th, 2021
 * Time: 3:48 PM TORONTO
 */

public class PayDirt {

    private static int currentPayDirtSackCount = -1;
    private static int futurePayDirtSackCount = -1;

    // cannot instantiate private constructor
    private PayDirt() {}

    // reset future pay-dirt
    public static void resetFuturePayDirtSackCount() {
        setFuturePayDirtSackCount(-1);
    }

    public static int getCurrentPayDirtSackCount() {
        return currentPayDirtSackCount;
    }

    public static void setCurrentPayDirtSackCount(int currentPayDirtSackCount) {
        PayDirt.currentPayDirtSackCount = currentPayDirtSackCount;
    }

    public static int getFuturePayDirtSackCount() {
        return futurePayDirtSackCount;
    }

    public static void setFuturePayDirtSackCount(int futurePayDirtSackCount) {
        PayDirt.futurePayDirtSackCount = futurePayDirtSackCount;
    }
}
