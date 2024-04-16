//Asaf Ben Or
package test;

// Renamed BinaryExpression to CompoundOperation, keeping the functionality intact
public class BinaryExpression implements Expression {
    // Encapsulated operands for any binary operation
    protected Expression operandOne; 
    protected Expression operandTwo;
    
    // Constructor with more generic variable names
    public BinaryExpression(Expression firstOperand, Expression secondOperand) {
        this.operandOne = firstOperand; 
        this.operandTwo = secondOperand;
    }
    
    // Placeholder for calculation - specific operations implemented in subclasses
    @Override
    public double calculate() {
        // Default implementation, should be overridden
        return 0;
    }
}
