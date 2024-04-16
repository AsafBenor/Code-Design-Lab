package test;

public class MainTrain1 {

	public static void main(String[] args) {
		int score = 100;

		if (Q3.calc("1/100000") != 0) {
			System.out.println("You should round the result by 3 decimal places (-10)"); // -10
			score -= 10;
		}
		if (Q3.calc("10/2*5") != 25) {
			System.out.println("Check your operator stack order (-10)"); // -10
			score -= 10;
		}
		if (Q3.calc("((((1)-((1))+1))/((1)+1)*2)") != 1) {
			System.out.println("Too many parentheses for you? ;) ((-10))"); // -10
			score -= 10;
		}
		if (Q3.calc("2.00*2.0/(2.00*2)/2.000000*(2.00000/2*(2.0/2.000)*2.0000)+0.00001*100000") != 2) {
			System.out.println("You should expect for small decimal numbers as well (-10.0)"); // -10
			score -= 10;
		}
		if (Q3.calc("(2+5-(3.235-4.435+1.2)/2.3712)*((1))*((2+(1)-2)*3)+12/6*0.01-(10.51*2)") != 0) {
			System.out.println("You didn't beat the final boss..  (-40)"); // -40
			score -= 40;
		}

		System.out.println("Your score is: " + score);
		if (score == 100) {
			System.out.println(
					"Congrats! You have perfect score.\nYou can happily submit the exercise and get back to watching netflix :)");
		}

	}

}