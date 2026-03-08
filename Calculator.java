public class Calculator {
    public static int sum(int[] numbers) {
        int total = 0;
        for (int n : numbers) total += n;
        return total;
    }

    public static int sequentialSubtract(int[] numbers) {
        if (numbers.length == 0) return 0;
        int diff = numbers[0];
        for (int i = 1; i < numbers.length; i++) diff -= numbers[i];
        return diff;
    }

    public static int product(int[] numbers) {
        int prod = 1;
        for (int n : numbers) prod *= n;
        return prod;
    }

    public static double average(int[] numbers) {
        if (numbers.length == 0) return 0;
        return (double) sum(numbers) / numbers.length;
    }
}
