//Asaf Ben Or
package test;

// This class represents the division operation in an expression
public class Div extends BinaryExpression {

    // Constructor initializing with operands for division
    public Div(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    // Overridden calculate method to perform division
    @Override
    public double calculate() {
        // Checks to prevent division by zero, returns Double.MAX_VALUE as an indicator
        double denominator = operandTwo.calculate();
        if (denominator == 0) {
            System.out.println("Attempt to divide by zero");
            return Double.MAX_VALUE;
        }
        return operandOne.calculate() / denominator;
    }
}
