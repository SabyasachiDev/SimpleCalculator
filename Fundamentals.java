import java.util.Scanner;

public class Fundamentals {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many integers would you like to enter? ");
        int count = scanner.nextInt();
        int[] numbers = new int[count];
        for (int i = 0; i < count; i++) {
            System.out.print("  Enter integer " + (i + 1) + ": ");
            numbers[i] = scanner.nextInt();
        }
        
        System.out.println("\nChoose operation to perform on the " + count + " numbers:");
        System.out.println("  1) Sum");
        System.out.println("  2) Sequential subtraction");
        System.out.println("  3) Product");
        System.out.println("  4) Average");
        System.out.print("Enter choice (1-4): ");
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1: {
                System.out.println("Result (sum): " + Calculator.sum(numbers));
                break;
            }
            case 2: {
                if (count > 0) {
                    System.out.println("Result (sequential subtraction): " + Calculator.sequentialSubtract(numbers));
                } else {
                    System.out.println("No numbers to subtract.");
                }
                break;
            }
            case 3: {
                System.out.println("Result (product): " + Calculator.product(numbers));
                break;
            }
            case 4: {
                if (count > 0) {
                    System.out.println("Result (average): " + Calculator.average(numbers));
                } else {
                    System.out.println("No numbers to average.");
                }
                break;
            }
            default:
                System.out.println("Invalid operation selected.");
                break;
        }
        
        scanner.close();
    }
    
}
