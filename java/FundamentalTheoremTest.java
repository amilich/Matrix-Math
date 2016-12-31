package test_classes;

import java.awt.Color;

import matrixmath.Interpolation;

import org.opensourcephysics.frames.PlotFrame;

import polyfun.Polynomial;
import riemann_sum.PolyPractice;

/**
 * Test class for the derivative, and accumulation interpolation polynomials.
 * It plots a particular function, the derivative, integral, integral of derivative, 
 * and derivative of integral on a single PlotFrame.  
 * 
 * This class essentially tests (and proves) the fundamental theorem of calculus 
 * by finding the derivative and integral of a function, and then computing the 
 * derivative of the integral and the integral of the derivative. 
 * 
 * The integral of the derivative and derivative of the integral return the original 
 * function f(x), thus proving the fundamental theorem. When using polynomials of 
 * degree > 8, the inverse matrix used to compute the derivative/integral functions 
 * is no longer computed well, so the functions are no longer exactly what they should be. 
 * 
 * @author Andrew M. 
 * @version 1.0
 */
public class FundamentalTheoremTest {
	public static void main(String[] args) {
		PlotFrame frame = new PlotFrame("x", "y", "Graph"); //make the PlotFrame

		Interpolation lag = new Interpolation(); //make the interpolation object  
		Polynomial poly = new Polynomial(new double[]{1, 0, 0, 0, 0, 0, 2}); //polynomial 

		/*ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>(); 
		for (double ii = 0; ii < 2*Math.PI; ii += Math.PI/12) {
			x.add(ii); 
			y.add(Math.sin(ii)); 
			frame.append(4, ii, Math.sin(ii));
			frame.append(4, -ii, -Math.sin(ii));
		}
		//System.out.println(x);
		//System.out.println(y);
		double[] x_a = new double[x.size()];
		double[] y_a = new double[y.size()];
		for (int ii = 0; ii < x_a.length; ii ++) {
			x_a[ii] = x.get(ii); 
			//System.out.print(x_a[ii] + ",");
		}
		for (int ii = 0; ii < y_a.length; ii ++) {
			y_a[ii] = y.get(ii); 
		}
		Polynomial f = lag.fit(x_a, y_a, 14); 
		f.print(); 
		
		frame.setVisible(true); //show the graph  
		PolyPractice.graphPoly(frame, f, -1.3*Math.PI, 1.3*Math.PI, 0.2, 0); //graph each particular function (in separate datasets)*/
		
		System.out.println("F (x): "); 
		poly.print(); //print out the polynomial 
		System.out.println("Derivative: ");
		Polynomial vdm_poly = lag.VDM_poly(poly); //make the vdm poly
		vdm_poly.print(); 
		System.out.println("Integral: "); //make the accumulation polynomial 
		Polynomial riemann_poly = lag.Riemann_poly(poly); 
		riemann_poly.print(); //print out the accumulation function 
		System.out.println(); 
		System.out.println("Integral of Derivative: "); 
		//should be original function (but without constant because derivative does not depend on  constant term)
		lag.Riemann_poly(vdm_poly).print(); //print out the integral of derivative  
		System.out.println("Derivative of Integral: "); //Should be exactly original function 
		lag.VDM_poly(riemann_poly).print(); //print out the derivative of integral 
		System.out.println("Orig: ");
		poly.print(); //print out original poly again


		frame.setVisible(true); //show the graph  
		PolyPractice.graphPoly(frame, poly, -5, 5, 0.1, 0); //graph each particular function (in separate datasets)
		PolyPractice.graphPoly(frame, vdm_poly, -5, 5, 0.1, 1);
		PolyPractice.graphPoly(frame, riemann_poly, -5, 5, 0.1, 2); 
		PolyPractice.graphPoly(frame, lag.Riemann_poly(vdm_poly), -5, 5, 0.1, 4);
		for (double ii = -5; ii < 5; ii += 1) {
			frame.setMarkerSize(3, 3); 
			frame.append(3, ii, PolyPractice.eval(lag.VDM_poly(riemann_poly), ii));
			frame.append(3, ii, PolyPractice.eval(lag.Riemann_poly(vdm_poly), ii));
		}
		frame.setMarkerColor(3, Color.RED); //set proper colors
	}
}
