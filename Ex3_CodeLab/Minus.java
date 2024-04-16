//Asaf Ben Or
package test;

// Implements subtraction operation within an expression
public class Minus extends BinaryExpression {

    // Constructor to initialize subtraction operands
    public Minus(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    // Overridden calculate method to perform subtraction
    @Override
    public double calculate() {
        // Performs the subtraction of the second operand from the first
        return operandOne.calculate() - operandTwo.calculate();
    }
}
