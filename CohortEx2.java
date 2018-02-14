import java.util.Scanner;
import java.lang.Math;

public class CohortEx2{
	/** Main method */
	public static void main(String[] args) {

		// Prompt the user to enter two complex numbers
		Scanner input = new Scanner(System.in);
		System.out.println("Enter real component of first complex number: ");
		double real1 = input.nextDouble();
		System.out.println("Enter imaginary component of first complex number: ");
		double img1 = input.nextDouble();
		Complex complexNumber1 = new Complex(real1, img1);
		System.out.println("Enter the operation: ");
		String operation = input.next();
		System.out.println("Enter real component of second complex number: ");
		double real2 = input.nextDouble();
		System.out.println("Enter real component of second complex number: ");
		double img2 = input.nextDouble();
		Complex complexNumber2 = new Complex(real2, img2);

		if (operation.equals("*")){
			System.out.println(complexNumber1 + " * " + complexNumber2 + " = "); 
			Complex result = complexNumber1.multiply(complexNumber2);
			System.out.print(result.toString());
		}else if(operation.equals("/")){
			System.out.println(complexNumber1 + " / " + complexNumber2 + " = "); 
			Complex result = complexNumber1.divide(complexNumber2);
			System.out.print(result.toString());
		}else if(operation.equals("+")){
			System.out.println(complexNumber1 + " + " + complexNumber2 + " = ");
			Complex result = complexNumber1.add(complexNumber2);
			System.out.print(result.toString());
		}else if(operation.equals("-")){
			System.out.println(complexNumber1 + " - " + complexNumber2 + " = ");
			Complex result = complexNumber1.subtract(complexNumber2); 
			System.out.print(result.toString());
		}else{
			System.out.println("incorrect input");
		}

		
	}


	/** Return user input as a complex number */
	public static Complex makeComplex() {
		// Create a Scanner object
		Scanner input = new Scanner(System.in);
		double[] c = new double[2];
		for (int i = 0; i < c.length; i++)
		 	c[i] = input.nextDouble(); 
		return new Complex(c[0], c[1]); 
	}
}

class Complex{
	private double real;
	private double imaginary;

	public Complex(double real, double img) {
		this.real = real;
		this.imaginary = img;
	}

	public double getReal() {
		return real;
	}

	public double getImaginary() {
		return imaginary;
	}

	public Complex add(Complex cNumb1) {
		return new Complex(real + cNumb1.getReal(), 
			imaginary + cNumb1.getImaginary()); 
	}
	public Complex subtract(Complex cNumb1) {
		return new Complex(real - cNumb1.getReal(),
			imaginary - cNumb1.getImaginary());
	}

	/** Multiply a complex number by this complex number */
	public Complex multiply(Complex cNumb1) {
		return new Complex(real * cNumb1.getReal() - imaginary * cNumb1.getImaginary(),
			imaginary * cNumb1.getReal() + real * cNumb1.getImaginary());
	}

	/** Divide a complex number by this complex number */
	public Complex divide(Complex cNumb1) {
		return new Complex((real * cNumb1.getReal() + imaginary * cNumb1.getImaginary()) /
			(Math.pow(cNumb1.getReal(), 2) + Math.pow(cNumb1.getImaginary(), 2)),
			(imaginary * cNumb1.getReal() - real * cNumb1.getImaginary()) /
			(Math.pow(cNumb1.getReal(), 2) +  Math.pow(cNumb1.getImaginary(), 2)));
	}
	@Override 
	public String toString() {
		return "(" + String.valueOf(real) + " + " + String.valueOf(imaginary) + "i)";
	}
}