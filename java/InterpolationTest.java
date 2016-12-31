package test_classes;

import polyfun.Polynomial;
import matrixmath.Interpolation;

/**
 * This class tests interpolation by creating the derivative and accumulation function polynomials. 
 * To do this, it calculates the derivative and Riemann sum at various points and interpolates a 
 * function.  
 * 
 * @author Andrew M. 
 */
public class InterpolationTest {
	public static void main(String[] args) {
		Interpolation lag = new Interpolation(); //make interpolation object 
		Polynomial poly = new Polynomial(new double[]{1, 2, 1, 4}); //polynomial to integrate/differentiate 
		System.out.println("Original function f(x): ");
		poly.print(); //print poly 
		lag.VDM_poly(poly).print(); //make acc poly 
		System.out.println("Accumulation function (Integral): ");
		Polynomial a = lag.Riemann_poly(poly); //print acc function 
		a.print(); //print out the function 
	}
}
