//Asaf Ben Or
package test;

// Represents a constant numeric value in an arithmetic expression
public class Number implements Expression {
    private double numericValue; // More descriptive variable name

    // Constructor to initialize the numeric value
    public Number(double numericValue) {
        this.numericValue = numericValue;
    }

    // Calculates and returns the numeric value itself
    @Override
    public double calculate() {
        return this.numericValue;
    }
    
    // Getter method to access the numeric value
    public double getNumericValue() { 
        return this.numericValue; 
    }
}
