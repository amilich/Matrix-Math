package matrixmath;

import java.awt.BasicStroke;
import java.awt.Color;

import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;

import polyfun.Polynomial;

/**
 * The VDM class contains the functions to calculate and graph the derivative of a function. 
 * Given a polynomial, it can calculate the derivative at a point or plot the derivative on a 
 * PlotFrame. 
 * 
 * It creates a matrix for the polynomial that is then solved to find the slope at a point. 
 * 
 * @method slopeAtPoint
 * 	Finds the slope of the derivative at a point 
 * @method slopeFunction 
 * 	Plots the derivative of a function. 
 * 
 * @author Andrew M. 
 * @version 1.0 
 */
public class VDM {
	/**
	 * @param poly
	 * 	Polynomial used to calculate derivative at an x coordinate.  
	 * @param x
	 * 	X coordinate to calculate derivative at. 
	 * @return
	 * 	Returns the slope at a point 
	 */
	public double slopeAtPoint (Polynomial poly, double x){ 
		//splits it into (x-a)^2 Q(x) 
		int q_degree = poly.getDegree() - 2; //degree of Q(x)  
		Polynomial v_x = new Polynomial(new double[]{Math.pow(x, 2), -2*x, 1}); //create (x-a)^2
		int[] q_degs = new int[q_degree+1]; 
		int[] v_degs = new int[v_x.getDegree()+1]; //this is used to fill the VDM matrix

		for (int ii = 0; ii < q_degs.length; ii++) //degree for each term in the polynomial 
			q_degs[ii] = q_degree-ii; 
		for (int ii = 0; ii < v_degs.length; ii++) 
			v_degs[ii] = v_x.getDegree()-ii; 

		//this essentially foils out the polynomial by finding terms that result in the same degree and then adding the coefficients
		double[][] mat = new double[poly.getDegree()+1][poly.getDegree()+2]; //VDM matrix to solve for slope at point
		Matrix matrix = new Matrix(mat.length, mat[0].length); 
		for (int ii = 0; ii <= poly.getDegree(); ii++) { //poly
			for (int jj = 0; jj < v_degs.length; jj++) { //v
				for (int kk = 0; kk < q_degs.length; kk++) { //q
					if(v_degs[jj] + q_degs[kk] == ii){ //this loops through each degree and assembles the terms into the matrix
						double coef = v_x.getCoefficient(v_degs[jj]).getTerms()[0].getTermDouble(); 
						mat[ii][q_degs[kk]+2] += coef; //adds to the coefficient in the matrix when the terms add to the proper degree
					}
				}
			} 
		}
		for (int ii = 0; ii < mat.length; ii++) {
			mat[ii][mat.length] = poly.getCoefficients()[ii].getTerms()[0].getTermDouble(); //put in constants  
		}
		mat[0][0] += 1; //adding b 
		mat[1][1] += 1; //add m 
		matrix.m = mat; //make it into the matrix
		//matrix.print(); 
		matrix = matrix.rowreduce(); //rowreduce the matrix 
		return matrix.m[1][matrix.m[0].length-1]; //return the solution for m
	}

	/**
	 * Graphs the slope function (derivative) of a polynomial. 
	 * 
	 * @param poly
	 * 	The polynomial to graph the slope function of. 
	 * @param slope
	 * 	The PlotFrame to graph the function on. 
	 */
	public void slopeFunction (Polynomial poly, PlotFrame slope) {
		//PlotFrame slope = new PlotFrame("x", "m", "Slope Function"); 
		Trail trail = new Trail(); 
		trail.setStroke(new BasicStroke(3)); //connect points using a trail
		trail.color = Color.blue.brighter(); //set the color the blue 
		slope.setMarkerColor(1, Color.blue.brighter()); 
		for (double ii = -3; ii < 3; ii += 0.01) {
			double m = this.slopeAtPoint(poly, ii); //get the slope at each point
			slope.append(1, ii, m);
			trail.addPoint(ii, m); //add point to trail 
			slope.addDrawable(trail); //add trail to graph
		}
		
		slope.setVisible(true); //show graph
	}
	
	public String toString(){  
		return "This is a VDM object.";
	}
}
