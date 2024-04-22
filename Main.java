import java.util.Stack;
import java.util.Scanner;

public class Main
{
	private static Stack<Double> stack = new Stack<>();

	private enum Errors
	{
		SUCCESS,
		FAILURE
	};

	public static void main(String... args)
	{
		if (args.length == 1 && args[0].equals("-e"))
		{
			String examples[] = {
				"234*+",
				"34*25*+",
				"638*+4"
			};

			int i = 0;

			for (String s : examples)
			{
				System.out.printf("%d) ", ++i);

				eval(s);
			}

			System.exit(0);
		}

		System.out.println("Postfix Calculator v0.1.0");
		System.out.println("Type a valid postfix expression then hit <Enter> to solve");
		System.out.println("Type 'exit' to quit");

		var scanner = new Scanner(System.in);
		String input = "";

		while (true)
		{
			System.out.print("\n> ");

			input = scanner.nextLine();

			if (input.equals("exit"))
				break;
				
			eval(input);
		}

		scanner.close();
	}

	private static void eval(String expr)
	{
		var err = solve(expr);
		if (err == Errors.SUCCESS)
			System.out.printf("%s = %.2f\n", expr, stack.pop());
	}

	private static Errors solve(String expr)
	{
		if (invalidPostfix(expr))
		{
			System.out.println("Error: parsing error");
			System.out.printf(">>>> Invalid postfix expression: %s\n", expr);
			System.out.println(">>>> Needs to end with an operator");
			return Errors.FAILURE;
		}

		char[] chars = expr.toCharArray();

		for (char i = 0; i < chars.length; i++)
		{
			char c = chars[i];
			if (Character.isDigit(c))
				stack.push(Double.parseDouble("" + c));
			else if (c == '(')
			{
				String buf = "";
				while (true)
				{
					c = chars[++i];
					if (c == ')')
						break;
					buf += c;
				}
				stack.push(Double.parseDouble(buf));
			}
			else
			{
				try
				{
					var a1 = stack.pop();
					var a0 = stack.pop();
					var res = calculate(a0, a1, c);
					stack.push(res);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return Errors.FAILURE;
				}
			}
		}

		return Errors.SUCCESS;
	}

	private static boolean invalidPostfix(String expr)
	{
		var last = expr.length() - 1;
		var c = expr.charAt(last);
		return ("+-*/%").indexOf(c) < 0;
	}

	private static double calculate(double a0, double a1, char op)
	{
		return switch (op)
		{
			case '+' -> a0 + a1;
			case '-' -> a0 - a1;
			case '*' -> a0 * a1;
			case '/' -> a0 / a1;
			case '%' -> a0 % a1;
			default -> 60065;
		};
	}
}
