//Asaf Ben Or
package test;

// This class represents a multiplication operation in an expression
public class Mul extends BinaryExpression {
    
    // Constructor for multiplication, accepting two expressions as operands
    public Mul(Expression firstOperand, Expression secondOperand) {
        super(firstOperand, secondOperand);
    }

    // Overridden calculate method to perform multiplication
    @Override
    public double calculate() {
        // Multiplies the results of calculating the left and right expressions
        return operandOne.calculate() * operandTwo.calculate();
    }
}
