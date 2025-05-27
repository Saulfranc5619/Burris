public class Calculator {

    public static double add(double a, double b) {
        return a + b;
    }

    public static double subtract(double a, double b) {
        return a - b;
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    public static double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return a / b;
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean running = true;

        while (running) {
            try {
                System.out.print("Enter the first number (or 'exit' to quit): ");
                String firstInput = scanner.next();
                if (firstInput.equalsIgnoreCase("exit")) {
                    running = false;
                    continue;
                }
                double num1 = Double.parseDouble(firstInput);

                System.out.print("Enter an operator (+, -, *, /): ");
                char operator = scanner.next().charAt(0);

                System.out.print("Enter the second number: ");
                double num2 = scanner.nextDouble();

                double result = 0;
                boolean validOperation = true;

                switch (operator) {
                    case '+':
                        result = add(num1, num2);
                        break;
                    case '-':
                        result = subtract(num1, num2);
                        break;
                    case '*':
                        result = multiply(num1, num2);
                        break;
                    case '/':
                        result = divide(num1, num2);
                        break;
                    default:
                        System.out.println("Invalid operator. Please use +, -, *, or /.");
                        validOperation = false;
                }

                if (validOperation) {
                    System.out.println("Result: " + num1 + " " + operator + " " + num2 + " = " + result);
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numeric values for numbers.");
                scanner.nextLine(); // Consume the invalid input
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println(); // Add a blank line for better readability
        }
        scanner.close();
        System.out.println("Calculator exited.");
    }
}
