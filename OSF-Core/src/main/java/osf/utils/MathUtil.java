package osf.utils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Random;

public class MathUtil {
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static int rand(int min, int max) {
        max += 1;
        return min + (new Random()).nextInt(max - min);
    }

    public static Double roundTwoPoints(double d) {

        DecimalFormat twoPointForm = new DecimalFormat("#.##");
        return Double.valueOf(twoPointForm.format(d));
    }

    public static String formatDouble(double d) {
        String formatted;
        if (d % 1 == 0) //Whole number
            formatted = (int) Math.floor(d) + "";
        else
            formatted = d + "";

        return formatted;

    }

    public static String formatToCurrency(double d) {
        NumberFormat dollarFormat = NumberFormat.getCurrencyInstance();
        return dollarFormat.format(d);
    }

    public static Float roundTwoPoints(float f) {
        DecimalFormat twoPointForm = new DecimalFormat("#.##");
        return Float.valueOf(twoPointForm.format(f));
    }

    public static int booleanAsInt(boolean value) {
        if (value)
            return 1;
        else
            return 0;
    }

    public static boolean intAsBoolean(int value) {
        return value == 1;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static int limitValue(int number, int minCap, int maxCap) {
        if (number <= minCap)
            return minCap;
        if (number >= maxCap)
            return maxCap;
        return number;
    }

    public static int capValue(int number, int maxCap) {
        if (number >= maxCap)
            return maxCap;
        else
            return number;
    }

    public static double limitValue(double number, double minCap, double maxCap) {
        if (number <= minCap)
            return minCap;
        if (number >= maxCap)
            return maxCap;
        return number;
    }

    public static double capValue(double number, double maxCap) {
        if (number >= maxCap)
            return maxCap;
        else
            return number;
    }
}
