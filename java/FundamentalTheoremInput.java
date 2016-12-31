package test_classes;
import java.awt.Color; 
import java.util.Scanner;
import matrixmath.Interpolation;
import org.opensourcephysics.frames.PlotFrame;
import polyfun.Polynomial;
import riemann_sum.PolyPractice;

/**
 * This class lets you input a polynomial to test. 
 * 
 * Test class for inputting a polynomial's coefficients and plotting a derivative and integral. 
 * It is similar to DiffIntTest.java but allows user to input polynomial.  
 * 
 * This class also proves the fundamental theorem of Calculus by showing how differentiation 
 * and integration result in the original polynomial. 
 * 
 * @author Andrew M. 
 * @version 1.0
 */
public class FundamentalTheoremInput {
	public static void main(String[] args) {
		@SuppressWarnings("resource") 
		Scanner input = new Scanner(System.in); //create scanner
		System.out.println("What degree poly would you like?"); //ask for degree 
		int degree = input.nextInt(); //degree of input polynomial 
		double[] coefs = new double[degree+1]; //coefficient array 
		for (int ii = 0; ii <= degree; ii++) {
			System.out.println("Enter coefficient for x^" + ii + " term.");
			coefs[ii] = input.nextDouble(); //get each coef in the array 
		}
		Polynomial poly = new Polynomial(coefs); //make the poly
		System.out.println("F(x): ");
		poly.print(); //print the poly

		PlotFrame frame = new PlotFrame("x", "y", "Graph"); //PlotFrame to graph on

		Interpolation lag = new Interpolation(); 
		System.out.println("Derivative: "); //prints out derivative 
		Polynomial vdm_poly = lag.VDM_poly(poly); //create vdm function
		vdm_poly.print(); //print vdm function 
		System.out.println("Accumulation Function: "); 
		Polynomial riemann_poly = lag.Riemann_poly(poly); //create accumulation function poly  
		riemann_poly.print(); //prints integral (acc. function) 
		System.out.println();
		System.out.println("Integral of Derivative: "); //plots integral of derivative 
		lag.Riemann_poly(vdm_poly).print(); 
		System.out.println("Derivative of Integral: "); //plots derivative of integral 
		lag.VDM_poly(riemann_poly).print();
		System.out.println("Orig: "); //plots original function 
		poly.print(); 

		frame.setVisible(true); //show the graph 
		PolyPractice.graphPoly(frame, poly, -5, 5, 0.1, 0); //graph each function 
		PolyPractice.graphPoly(frame, vdm_poly, -5, 5, 0.1, 1);
		PolyPractice.graphPoly(frame, riemann_poly, -5, 5, 0.1, 2); 
		PolyPractice.graphPoly(frame, lag.Riemann_poly(vdm_poly), -5, 5, 0.1, 4);
		for (double ii = -5; ii < 5; ii += 1) {
			frame.setMarkerSize(3, 3); //set to the right size 
			frame.append(3, ii, PolyPractice.eval(lag.VDM_poly(riemann_poly), ii));
			frame.append(3, ii, PolyPractice.eval(lag.Riemann_poly(vdm_poly), ii));
		}
		frame.setMarkerColor(0, Color.BLUE); //nice colors for graph
		frame.setMarkerColor(1, Color.GREEN);
		frame.setMarkerColor(2, Color.ORANGE);
		frame.setMarkerColor(3, Color.RED); 
	}

}
