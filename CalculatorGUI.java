import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class CalculatorGUI extends JFrame implements ActionListener {

    private JTextField displayField;
    private double num1 = 0; // Initialize to avoid issues if operator is pressed first
    private String operator = "";
    private boolean operatorPressed = false; // True if an operator was the last key pressed, expecting next number
    private double lastOperand = 0;   // Stores the second number of the last operation for repeated equals
    private String lastOperator = ""; // Stores the last operator used for repeated equals

    private DecimalFormat df; // For formatting display output

    public CalculatorGUI() {
        // Set up the frame
        setTitle("Swing Calculator (Refined)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500); // Adjusted size for better button layout
        setLayout(new BorderLayout());

        // Create the display field
        displayField = new JTextField("0");
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(JTextField.RIGHT); // Align text to the right
        displayField.setFont(new Font("Arial", Font.BOLD, 24)); // Set a larger font
        add(displayField, BorderLayout.NORTH);

        // Initialize DecimalFormat
        df = new DecimalFormat("#.##########"); // Max 10 decimal places, no trailing zeros for whole numbers

        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5)); // 5 rows, 4 columns, with 5px gaps

        // Define button labels in the desired order for a standard calculator layout
        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C" // Clear button
        };

        // Create and add buttons to the panel
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 18)); // Set button font
            // Special handling for the clear button if we want it to span or be different
            if (label.equals("C")) {
                button.setForeground(Color.RED);
            }
            button.addActionListener(this); // Add action listener
            buttonPanel.add(button);
        }
        
        // If "C" needs to span columns, a more complex layout like GridBagLayout might be needed
        // or place "C" in a separate panel. For now, it's part of the 5x4 grid.
        // To make "C" span, we can adjust the grid or use a different panel for the last row.
        // For simplicity with GridLayout, "C" will take one cell.
        // If we want a 4x4 grid for numbers/ops and 'C' separate, we'd do something like:
        // JPanel mainButtonGrid = new JPanel(new GridLayout(4,4,5,5)); // for 7-9,/,4-6,*,1-3,-,0,.,=,+
        // ... add those buttons to mainButtonGrid ...
        // JPanel clearButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // for C
        // clearButtonPanel.add(new JButton("C"));
        // buttonPanel.setLayout(new BorderLayout());
        // buttonPanel.add(mainButtonGrid, BorderLayout.CENTER);
        // buttonPanel.add(clearButtonPanel, BorderLayout.SOUTH);
        // But the current approach with 5x4 is simpler for now.

        add(buttonPanel, BorderLayout.CENTER);

        // Make the frame visible
        setLocationRelativeTo(null); // Center the frame on screen
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String currentDisplay = displayField.getText();

        // 5. Error State Reset: If an error is displayed, pressing a digit or decimal point starts a new number.
        if (currentDisplay.startsWith("Error")) {
            if (command.matches("[0-9]") || command.equals(".")) {
                displayField.setText(command.equals(".") ? "0." : command);
                operatorPressed = false; // Allow further digits/decimal
                num1 = 0; // Reset calculation state
                operator = "";
                lastOperator = "";
                lastOperand = 0;
                return; // Handled error reset, exit early
            } else if (!command.equals("C")) { // Only C should work if in error state, besides new number
                return;
            }
        }

        if (command.matches("[0-9]")) { // Digit buttons
            if (operatorPressed || currentDisplay.equals("0")) {
                displayField.setText(command);
                operatorPressed = false; // After a digit is pressed, no longer expecting an operator next
            } else {
                displayField.setText(currentDisplay + command);
            }
        } else if (command.equals(".")) { // Decimal point button
            if (operatorPressed || currentDisplay.equals("0")) { // If operator was just pressed, start new number with "0."
                displayField.setText("0.");
                operatorPressed = false;
            } else if (!currentDisplay.contains(".")) { // Append only if no decimal point yet
                displayField.setText(currentDisplay + ".");
            }
        } else if (command.equals("C")) { // Clear button
            displayField.setText("0");
            num1 = 0;
            operator = "";
            operatorPressed = false;
            lastOperand = 0;
            lastOperator = "";
        } else if (command.equals("=")) { // Equals button
            if (!operator.isEmpty()) { // Standard calculation: num1 operator num2
                try {
                    double num2 = Double.parseDouble(currentDisplay);
                    lastOperand = num2; // Store for potential repeated equals
                    // lastOperator is already set when an operator button was pressed
                    
                    num1 = performCalculation(num1, num2, operator);
                    displayField.setText(df.format(num1));
                    // operator = ""; // Don't reset operator: allows 5 + 2 = (7) + = (9) behavior IF lastOperator is used right
                                    // For now, to make repeated equals distinct, we clear operator
                                    // and rely on lastOperator and lastOperand
                    operator = ""; // Explicitly clear current operator for distinct repeated equals.
                    operatorPressed = true; // Expecting a new number or a new operator for a new calculation
                } catch (NumberFormatException ex) {
                    displayField.setText("Error: Invalid num");
                    resetErrorState();
                } catch (IllegalArgumentException ex) {
                    displayField.setText("Error: Div by 0");
                    resetErrorState();
                }
            } else if (!lastOperator.isEmpty()) { // Repeated equals: num1 lastOperator lastOperand
                try {
                    num1 = performCalculation(num1, lastOperand, lastOperator);
                    displayField.setText(df.format(num1));
                    operatorPressed = true; // Still ready for new number or operator
                } catch (IllegalArgumentException ex) { // e.g. if lastOp was / and lastOperand was 0 due to complex chain
                    displayField.setText("Error: Div by 0");
                    resetErrorState();
                }
            }
            // If both operator and lastOperator are empty, equals does nothing.
        } else { // Operator buttons (+, -, *, /)
            // If an operator is pressed after an error message, it should be ignored unless it's 'C' (handled above)
            if (currentDisplay.startsWith("Error")) return;

            try {
                double currentNumber = Double.parseDouble(currentDisplay);
                if (!operator.isEmpty() && !operatorPressed) {
                    // Scenario: 5 * 2 - (user presses - after typing 2, effectively 5*2 then -)
                    // Perform the pending calculation
                    num1 = performCalculation(num1, currentNumber, operator);
                    displayField.setText(df.format(num1)); // Display intermediate result
                } else if (operatorPressed) {
                    // Scenario: 5 * - (user presses - after *). The last operator (-) overrides.
                    // num1 is already set from the number before the first operator.
                    // No calculation needed here, just update the operator.
                }
                else { // No pending operator, or display was just cleared/result shown
                    num1 = currentNumber;
                }
                operator = command;
                lastOperator = operator; // Store for repeated equals
                operatorPressed = true; // Expecting the second number next
            } catch (NumberFormatException ex) {
                displayField.setText("Error: Invalid num");
                resetErrorState();
            } catch (IllegalArgumentException ex) { // From performCalculation if it's somehow triggered here
                 displayField.setText("Error: Div by 0");
                 resetErrorState();
            }
        }
    }

    private double performCalculation(double n1, double n2, String op) {
        switch (op) {
            case "+": return Calculator.add(n1, n2);
            case "-": return Calculator.subtract(n1, n2);
            case "*": return Calculator.multiply(n1, n2);
            case "/": return Calculator.divide(n1, n2); // Can throw IllegalArgumentException
            default: return n2; // Should not happen with valid operators
        }
    }

    private void resetErrorState() {
        operator = "";
        lastOperator = "";
        operatorPressed = false;
        // num1 might hold a value that led to error, but it's fine, 
        // next valid number input will overwrite or use it if appropriate.
                }
            }
        }
    }

    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CalculatorGUI();
            }
        });
    }
}
