package matrixmath;

import java.util.Random;

/**
 * Date: October, 2013
 * 
 * This class creates a matrix and contains various functions used to manipulate a matrix. 
 * The matrix is made up of a 2D array of doubles, and most matrix operations return a 
 * new matrix with modified rows or columns.  
 * 
 * @method fillMatrix 
 * 	Fills the values of one matrix with those of an input matrix. 
 * @method times 
 * 	Multiplies two matrices. 
 * @method rowreduce
 * 	Returns a rowreduced matrix. 
 * @method invert
 * 	Returns the inverse of a matrix (square matrix). 
 * @method linearCombRows
 * 	
 * @method clone
 *  Returns a cloned matrix (copy function). 
 * @method zerosDown
 *  Puts zerosDown in a particular column. 
 * @method findPivot 
 *  Finds the pivot in a particular column - the first item in the column that is not a 0. 
 * @method setEntry
 *  Set a particular entry of a matrix. 
 * @method switchRows
 *  Switch two rows in a matrix. 
 * @method scalarTimesRow
 *  Multiplies a row by a scalar. 
 * @method plus 
 *  Adds two matrices. 
 * @method print 
 * 	Prints out a matrix. 
 * @method addIdentity
 * 	Appends identity matrix (used to invert a matrix). 
 * 
 * @author Andrew M. 
 * @version 1.0
 */
public class Matrix  {
	/**
	 * This double array contains the values in the matrix. When modifying a matrix, it is 
	 * sometimes useful to set this array to a double array created outside of the matrix. 
	 */
	public double[][] m; //the array that holds the matrix values
	int rows; //number of rows 
	int cols; //number of columns
	boolean hasIdentity = false; 
	double count = 0; 

	/**
	 * Constructor for matrix - initializes with # of rows and columns. 
	 * 
	 * @param i
	 * 	The number of rows. 
	 * @param j
	 * 	The number of columns. 
	 */
	public Matrix(int i, int j){
		m = new double[i][j]; //make the 2D array 
		rows = i; //set the number of rows 
		cols = j; //set the number of columns 
	}

	/**
	 * This constructor allows for random generation of matrices with random integers or floats. 
	 * 
	 * @param i
	 * 	Rows in the matrix. 
	 * @param j
	 * 	Number of columns. 
	 * @param random
	 * 	Whether the matrix should be randomly generated. 
	 * @param floats
	 * 	This boolean determines whether floating point values are used or whether the matrix will be filled with integers. 
	 */
	public Matrix(int i, int j, boolean random, boolean floats){
		Random rand = new Random(); 
		m = new double[i][j]; //make the 2D array 
		rows = i; //set the number of rows 
		cols = j; //set the number of columns 
		if(random){ //will randomly generate a matrix
			for (int ii = 0; ii < m.length; ii++) {
				for (int jj = 0; jj < m[0].length; jj++) {
					m[ii][jj] = rand.nextInt(25); //reasonable limit for each number in the matrix  
					if(floats){
						m[ii][jj] *= rand.nextFloat(); //turns the integers into floating point numbers
					}
					if(rand.nextBoolean()){
						m[ii][jj] *= -1; //makes some of the numbers negative 
					}
				}
			}
		}
	}

	/**
	 * Fills a matrix with values given by another matrix. 
	 * 
	 * @param that
	 * 	The matrix values used to fill these. 
	 * @return 
	 */
	public void fillMatrix(Matrix that){ //useful for cloning a matrix - fills the values of one matrix with those of another. 
		double[][] mat = new double[that.m.length][that.m[0].length]; 
		for (int ii = 0; ii < mat.length; ii++) {
			for (int jj = 0; jj < mat[0].length; jj++) {
				mat[ii][jj] = that.m[ii][jj]; //put each value in 
			}
		}
		this.m = mat; //equate the 2D arrays in each matrix 
	}

	/**
	 * Finds the inverse of a matrix. The invert method appends the identity matrix to a given matrix, 
	 * rowreduces the original matrix, and then returns a matrix created from the right side of the 
	 * augmented matrix. 
	 * 
	 * @return
	 * 	Returns a new, inverted matrix (same dimensions as one from the user). 
	 */
	public Matrix invert(){
		Matrix withID = this.addIdentity(this.clone()); //appends identity matrix
		Matrix red = withID.rowreduce(); //rowreduce that matrix

		Matrix inverted = new Matrix(this.rows, this.cols); //inverse is original size
		for (int ii = 0; ii < this.rows; ii++) { //fill inverse matrix from the right side of the augmented matrix 
			for (int jj = this.cols; jj < withID.cols; jj++) {
				inverted.setEntry(ii, jj-this.cols, red.m[ii][jj]); //exchange values from augmented matrix into inverted matrix
			}
		}
		return inverted;
	}

	/**
	 * Adds an identity matrix to a given matrix (used for inverting). 
	 * 
	 * @param that
	 * 	The matrix on which to append the identity matrix.  
	 * @return
	 * 	New augmented matrix with identity matrix. 
	 */
	public Matrix addIdentity(Matrix that){
		Matrix withID = null; 
		//need a square matrix to invert
		if(this.rows > that.cols){
			System.out.println("not invertible");
			return null; 
		}
		else if(that.cols > that.rows){
			System.out.println("not invertible");
			return null; 
		}
		else{
			//have a square matrix
			withID = new Matrix(that.rows, 2*that.cols);
		}
		//fill the augmented matrix with the values from the original matrix 
		for (int ii = 0; ii < that.rows; ii++) {
			for (int jj = 0; jj < that.cols; jj++) {
				withID.setEntry(ii, jj, that.m[ii][jj]);
			}
		}
		//put in identity matrix 
		for (int ii = that.cols; ii < withID.cols; ii++) {
			for (int jj = 0; jj < that.rows; jj++) {
				if(ii-that.cols == jj){ //if on diagonal, put a 1 
					withID.setEntry(jj, ii, 1);
				}
				else{ //otherwise, put a 0
					withID.setEntry(jj, ii, 0); 
				}
			}
		}
		withID.hasIdentity = true; //this matrix has the identity 
		return withID; //return augmented matrix 
	}

	/**
	 * Copies a matrix; returns the same matrix (useful for seeing what happens to a matrix
	 * while keeping an original copy).  
	 */
	public Matrix clone(){
		Matrix cloned = new Matrix(this.rows, this.cols); 
		cloned.fillMatrix(this); 
		return cloned; 
	}

	/**
	 * Adds a multiple of one row to another row. Returns a new matrix 
	 * with the same values but with the multiple of one row added to 
	 * another (as specified by the parameters). 
	 * 
	 * @param scalar
	 * 	The scalar used to multiply the first row. 
	 * @param firstrow
	 * 	The row that is multiplied and then added. 
	 * @param secondrow
	 * 	The modified row. 
	 * @return
	 * 	A matrix with the modified row. 
	 */
	public Matrix linearCombRows(double scalar, int firstrow, int secondrow){
		Matrix replaced = new Matrix(this.rows, this.cols); //make the matrix to be returned 
		replaced.fillMatrix(this); 

		for (int ii = 0; ii < replaced.cols; ii++) {
			replaced.m[secondrow][ii] += scalar*replaced.m[firstrow][ii]; 
		}
		return replaced; //returns new matrix (DOES NOT modify the original matrix) 
	}

	/**
	 * Adds two matrices. To add two matrices, you need to add the corresponding members from each matrix. 
	 * The two matrices must thus have the same dimensions (rows and columns) for addition to be possible. 
	 * 
	 * @param that
	 * 	The second matrix to be added. 
	 * @return
	 * 	New matrix containing result of added matrices. 
	 */
	public Matrix plus (Matrix that){
		if (rows == that.rows && cols == that.cols){ //make sure rows agree 
			Matrix added = new Matrix(rows, cols); //make added matrix 
			for (int ii = 0; ii < added.m.length; ii++) { //add corresponding values 
				for (int jj = 0; jj < added.m[0].length; jj++) {
					added.m[ii][jj] = this.m[ii][jj] + that.m[ii][jj]; 
				}
			}
			return added; //return new matrix 
		}
		return null; //if does not agree in size 
	}

	/**
	 * Prints out a matrix. 
	 * Goes through each row and column of the matrix and print out the value; space each member
	 * of the matrix with a "|" to separate each value. 
	 */
	public void print(){
		for (int ii = 0; ii < m.length; ii++) { //loop through matrix
			System.out.print("| ");
			for (int jj = 0; jj < m[0].length; jj++) {
				double precision = 10000; //round to a precision of 4 decimal places (easy to change)
				double val = Math.round(m[ii][jj]*precision); //round by multiplying by 10,000, rounding to nearest int, and then dividing by 10,000
				val /= precision; 
				System.out.print(val);
				System.out.print(" | "); //separates each entry
			}
			System.out.println();
		}
	}

	/**
	 * Returns a rowreduced matrix. 
	 * 
	 * @return
	 * 	A matrix that has been fully rowreduced. 
	 */
	public Matrix rowreduce(){ 
		Matrix reduced = new Matrix(this.rows, this.cols); //creates new matrix that will contain rowreduced values
		reduced.fillMatrix(this); 
		double max = 0; 
		if(this.hasIdentity) //if the matrix has the identity, you can only rowreduce half of the columns (for augmented matrix) 
			max = this.cols/2; 
		else { //otherwise, you can rowreduce as much as possible (limtied by either rows or cols) and leave the rest not reduced 
			if(this.rows > this.cols) //more rows than cols 
				max = this.cols;
			else //more cols than rows
				max = this.rows; 
		}

		for (int ii = 0; ii < max; ii++) { //go through the maximum number of columns 
			reduced = reduced.zerosDown(reduced, ii); //put zeros down in the column you are on 
			
			if(reduced.m[ii][ii] != 1){ //if the value at this position not 1, you need to make it 1 (on diagonal of matrix)
				double multFactor = 1/(reduced.m[ii][ii]); //multiply by reciprocal 
				if(Math.abs(multFactor) < Math.pow(10, 10) && Math.abs(multFactor) > Math.pow(10, -10)){ //this makes sure you are not dividing by a positive infinity type number
					reduced = reduced.scalarTimesRow(multFactor, ii); //if it's smaller than 10^-10, round to 0
				}
			}
		}

		return reduced; //return rowreduced matrix
	}

	/**
	 * Given a matrix and a column, this method goes through the column and puts zeros in every row except the pivot. 
	 * The matrix class rowreduces by performing zerosDown on every column. 
	 * 
	 * ZerosDown is the most important part of the rowreduce as the program cycles through each column and puts zeros 
	 * in every position except the diagonal (should be 1's).  
	 * 
	 * @param that
	 *	The matrix used (being rowreduced). 
	 * @param col
	 * 	The column to put zeros down. 
	 * @return
	 * 	Returns a new matrix with zeros in that column. 
	 */
	public Matrix zerosDown(Matrix that, int col){ 
		Matrix zeroDown = new Matrix(this.rows, this.cols); //new matrix with zerosDown performed 
		zeroDown.fillMatrix(that); //fill with old values 
		double[] pivot = findPivot(col); //find the pivot in the column you're using 
		if(pivot[0] >= col) //if the pivot is not in the right place, 
			zeroDown = zeroDown.switchRows((int)pivot[0], col); //swap pivot to right plaec
		int switch_pos = 0; 
		double multFactor = 0; 

		//this rounds down values that are less than a tiny number (10^-15)
		for (int jj = 0; jj < that.m.length; jj++) {
			for (int kk = 0; kk < that.m[0].length; kk++) {
				if(Math.abs(that.m[jj][kk]) < Math.pow(10, -15) && that.m[jj][kk] != 0){
					that.m[jj][kk] = 0; //set it to 0
				}
			}
		}
		for (int ii = zeroDown.rows-1; ii >= 0; ii--) { //go through each row from the bottom to start making each value a zero 
			if(zeroDown.m[ii][col] != 0 && ii != pivot[0]){ //you do not want a zero where the pivot is (or if there is already a 0)
				for (int jj = zeroDown.rows-1; jj >= 0; jj--) {
					if(Math.abs(zeroDown.m[jj][col]) >= Math.pow(10, -8) && jj != ii){ //determines whether the value is too large or small 
						multFactor = -1*(zeroDown.m[ii][col])/(zeroDown.m[jj][col]); //finds proper value to multiply by  
						switch_pos = jj; //row # used for linearcombrow 
						break; 
					}
				}
				if(Math.abs(multFactor) < Math.pow(10, 8) && Math.abs(multFactor) > Math.pow(10, -8)){ //make sure you're not dividing/multiplying by a number too large or small
					zeroDown = zeroDown.linearCombRows(multFactor, switch_pos, ii); 
				}
			}
		}
		return zeroDown; 
	}

	/**
	 * The pivot is the first number in a column that is not a zero. 
	 * 
	 * @param col
	 * @return
	 */
	public double[] findPivot(int col){ 
		if(this.m[col][col] != 0){ //it is often simpler just to use a pivot on the diagonal 
			return new double[] {col, this.m[col][col]}; 
		}
		for (int ii = 0; ii < this.rows; ii++) { //otherwise, find the first # in the column that's not a 0
			if(this.m[ii][col] != 0){
				return new double [] {ii, this.m[ii][col]}; 
			}
		}
		return new double[] {0, 0}; //no pivot in a column 
	}

	/**
	 * Multiplies a particular row by a scalar. 
	 * 
	 * @param scalar
	 * 	The scalar to multiply the particular row by. 
	 * @param rownumber
	 * 	The row number to multiply. 
	 * @return
	 * 	New, multiplied matrix. 
	 */
	public Matrix scalarTimesRow(double scalar, int rownumber){
		Matrix times = new Matrix(this.rows, this.cols); //new matrix with row multiplied 
		times.fillMatrix(this); //fill data in new matrix with old one 
		for (int ii = 0; ii < this.cols; ii++) {
			times.m[rownumber][ii] *= scalar; //multiply each value by scalar 
		}
		return times; //return new matrix
	}

	/**
	 * Switches two rows in a matrix; returns a new matrix with the switched rows.  
	 * 
	 * @param firstRow
	 * 	First row used to switch. 
	 * @param secondRow
	 * 	Second row used to switch. 
	 * @return
	 * 	New matrix with switched rows. 
	 */
	public Matrix switchRows(int firstRow, int secondRow){
		Matrix switched = new Matrix(this.rows, this.cols);
		switched.fillMatrix(this); //fill the values of the new matrix with the old one (but then switch the rows) 
		double[] row_one = new double[this.cols]; //holds contents of each row while they're being switched  
		double[] row_two = new double[this.cols]; 

		row_one = this.m[firstRow]; //make sure you don't lose the row 
		row_two = this.m[secondRow]; 

		switched.m[firstRow] = row_two; //switch the rows 
		switched.m[secondRow] = row_one; 
		return switched; 
	}

	/**
	 * Multiplies two matrices. To multiply two matrices, you need to multiply each member of a row of the first matrix 
	 * with the corresponding member of the column of the second matrix. Each of these individual products for each member 
	 * of the row is then summed, and this sum is part of the product matrix. 
	 * 
	 * The multiplied matrix has dimensions of the # of rows in the first matrix and the # of columns in the second matrix. 
	 * 
	 * @param that
	 * 	The matrix used to multiply this one by (remember not commutative) 
	 * @return
	 * 	New matrix with multiplied values 
	 */
	public Matrix times(Matrix that){
		Matrix mult = new Matrix(rows, that.cols); 

		for (int ii = 0; ii < this.rows; ii++) { 
			for (int jj = 0; jj < that.cols; jj++) { //ii, jj loop through new matrix (when multiplying, new matrix has x dimension of the 1st matrix and then y dimension of the 2nd matrix's y dimension)
				for (int kk = 0; kk < that.rows; kk++) { //loop through each row of this matrix 
					mult.m[ii][jj] += this.m[ii][kk]*that.m[kk][jj]; //add to get the final product
				}
			}
		}
		return mult; //return multiplied matrix 
	}

	/**
	 * Sets an entry in a matrix with value d. 
	 * 
	 * @param i
	 * 	Row of new entry 
	 * @param j
	 * 	Column of new entry
	 * @param d
	 * 	The value to put in the matrix. 
	 */
	public void setEntry(int i, int j, double d) {
		m[i][j] = d; //set a particular entry
	}
}
