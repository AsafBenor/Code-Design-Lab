//Asaf Ben Or
package test;

import java.util.LinkedList;
import java.util.Stack;

public class Q3 {

    public static double calc(String expression) {
        LinkedList<String> outputQueue = new LinkedList<>(); // For holding the expression elements in postfix order
        Stack<String> operatorStack = new Stack<>(); // For operators and parentheses
        boolean isPreviousDigit = false; // Flag to handle multi-digit numbers and decimal points

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);

            if (Character.isDigit(currentChar) || currentChar == '.') { // Handle numbers and decimal points
                String number = (isPreviousDigit ? outputQueue.removeLast() : "") + currentChar;
                outputQueue.add(number);
                isPreviousDigit = true;
            } else {
                isPreviousDigit = false;
                if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/') {
                    handleOperator(currentChar, operatorStack, outputQueue);
                } else if (currentChar == '(') {
                    operatorStack.push(String.valueOf(currentChar));
                } else if (currentChar == ')') {
                    popOperatorsUntilOpeningParenthesis(operatorStack, outputQueue);
                }
            }
        }

        // Pop any remaining operators from the stack to the queue
        while (!operatorStack.isEmpty()) {
            outputQueue.add(operatorStack.pop());
        }

        // Evaluate the postfix expression
        return evaluatePostfixExpression(outputQueue);
    }

    private static void handleOperator(char operator, Stack<String> operatorStack, LinkedList<String> outputQueue) {
        while (!operatorStack.isEmpty() && hasPrecedence(operator, operatorStack.peek().charAt(0))) {
            outputQueue.add(operatorStack.pop());
        }
        operatorStack.push(String.valueOf(operator));
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    private static void popOperatorsUntilOpeningParenthesis(Stack<String> stack, LinkedList<String> queue) {
        while (!stack.peek().equals("(")) {
            queue.add(stack.pop());
        }
        stack.pop(); // Remove the '('
    }

    private static double evaluatePostfixExpression(LinkedList<String> queue) {
        Stack<Expression> stack = new Stack<>();

        for (String token : queue) {
            if (token.matches("[+\\-*/]")) {
                Number right = (Number) stack.pop();
                Number left = (Number) stack.pop();

                Expression operation = switch (token) {
                    case "+" -> new Plus(left, right);
                    case "-" -> new Minus(left, right);
                    case "*" -> new Mul(left, right);
                    case "/" -> new Div(left, right);
                    default -> throw new IllegalArgumentException("Unexpected operator: " + token);
                };

                double result = roundToThreeDecimalPlaces(operation.calculate());
                stack.push(new Number(result));
            } else { // Operand
                stack.push(new Number(Double.parseDouble(token)));
            }
        }

        return ((Number) stack.pop()).getNumericValue();
    }


    private static double roundToThreeDecimalPlaces(double value) {
        return Math.round(value * 1000) / 1000.0;
    }
}
