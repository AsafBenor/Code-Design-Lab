//Asaf Ben Or
package test;

// Represents an addition operation within an expression
public class Plus extends BinaryExpression {
    
    // Constructor to initialize with two expressions to be added
    public Plus(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    // Overridden calculate method to perform addition
    @Override
    public double calculate() {
        // Calculates the sum of the two operands
        return operandOne.calculate() + operandTwo.calculate();
    }
}
