package matrixmath;
import polyfun.Polynomial;
import riemann_sum.SimpsonsRule;

/**
 * The Interpolation class contains methods to, given x and y coordinates, interpolate a polynomial 
 * function through the points. 
 * 
 * It also contains methods to calculate the derivative and integral functions using the VDM and Riemann 
 * Sum classes. 
 * 
 * @method fit
 * 	Finds the equation of an interpolated polynomial through a set of points. 
 * @method Riemann_poly 
 * 	Calculates and interpolates Riemann sum function. 
 * @method VDM_poly 
 * 	Calculates and interpolates VDM function. 
 * 
 * @author Andrew
 * @version 1.0
 */
public class Interpolation {
	/**
	 * Fits a polynomial to given set of x and y coordinates. 
	 * 
	 * @param x
	 * 	X coordinates of points to interpolate
	 * @param y
	 * 	Y coordinates of points to interpolate 
	 * @param degree
	 * 	Degree of polynomial to fit
	 * @return
	 * 	Returns fitted polynomial 
	 */
	public Polynomial fit(double[] x, double[] y, int degree){
		Matrix points = new Matrix(degree+1, degree+2); //sets up matrix to rowreduce to get coefficients
		Matrix use_inv = new Matrix(degree+1, degree+1); //sets up matrix to invert to get coefs (alternate method) 
		Matrix b = new Matrix(degree+1, 1);
		for (int ii = 0; ii < points.rows; ii++) {
			for (int jj = 0; jj < degree+1; jj++) {
				points.setEntry(ii, jj, Math.pow(x[ii], degree-jj)); //calculate value of function at a point 
				use_inv.setEntry(ii, jj, Math.pow(x[ii], degree-jj)); //put it into the matrix
			}
		}
		for (int ii = 0; ii < points.rows; ii++) { //makes column vector to multiply
			double y_val = y[ii]; 
			points.setEntry(ii, points.cols-1, y_val); 
			b.setEntry(ii, 0, y_val);
		}	
		//Matrix red = points.rowreduce(); //rowreduce the points 
		Matrix inv = use_inv.invert(); //get the inverse 
		Matrix soln = inv.times(b); //multiply to get solution
		//inv.print(); 
		
		double[] new_coefs = new double[soln.rows]; 
		for (int ii = 0; ii < soln.rows; ii ++) { //find solution set
			//new_coefs[new_coefs.length-1-ii] = red.m[ii][degree+1]; 
			new_coefs[new_coefs.length-1-ii] = soln.m[ii][0]; 
		}
		for (int ii = 0; ii < new_coefs.length; ii++) {
			double val = Math.round(new_coefs[ii]*1E6); //does rounding
			new_coefs[ii] = val/1E6; 
		}
		
		Polynomial fitted = new Polynomial(new_coefs); //create the actual polynomial
		return fitted; 
	}

	/**
	 * Finds the accumulation function polynomial for a given poly.
	 * 
	 * @param poly
	 * 	The polynomial used to fit an accumulation function (integral). 
	 * @return
	 * 	Returns that polynomial. 
	 */
	public Polynomial Riemann_poly(Polynomial poly){
		SimpsonsRule rule = new SimpsonsRule(); //create the Riemann sum 
		double[] x = new double[poly.getDegree()+2]; 
		double[] y = new double[poly.getDegree()+2]; 
		for (double ii = 1; ii < x.length+1; ii ++) { //put in x and y points 
			x[(int)ii-1] = ii; //set x coordinate
			y[(int)ii-1] = rule.rs(poly, 0, ii, 100); //calculates riemann sums at various points to interpolate for function 
		} 
		return this.fit(x, y, poly.getDegree()+1); //returns fitted poly 
	}

	/**
	 * Finds the VDM polynomial for a given function. 
	 * 
	 * @param poly
	 * 	The polynomial used to fit the VDM (the derivative). 
	 * @return
	 * 	Returns the VDM function 
	 */
	public Polynomial VDM_poly(Polynomial poly){
		VDM vdm = new VDM(); //creates VDM
		double[] x = new double[poly.getDegree()]; //make x point array 
		double[] y = new double[poly.getDegree()]; //make array of y coords 
		for (int ii = 1; ii < x.length+1; ii++) {
			x[ii-1] = ii; //set x coordinate
			y[ii-1] = vdm.slopeAtPoint(poly, ii);  //create point set with y values from derivative function
		} 

		return this.fit(x, y, poly.getDegree()-1); //returns fitted polynomial 
	}
}
