package test_classes;

import matrixmath.Matrix;

public class InvertTest 
{

	public static void main(String[] args) 
	{
		Matrix matt = new Matrix (3,3); //matt is invertible
		
		int counter = 0;
		
		for(int i=0; i<3; i++) //Filling matt
		{
			for(int j=0; j<3; j++)
			{
				counter++;
				matt.setEntry(i, j, counter);
			}
		}
		
		matt.setEntry(2, 2, 10);
		
		matt.print(); //printing matt
		
		System.out.println("\n");
		
		matt.invert().print(); //printing the inverse of matt
		
		System.out.println("\n");
		
		matt.times(matt.invert()).print(); //printing the product of matt and his inverse
		
		System.out.println("\n");
		
		
		Matrix trixie = new Matrix (4,4); //trixie is invertible
		
		for(int i=0; i<4; i++) //Filling trixie
		{
			for(int j=0; j<4; j++)
			{
				if(i+j==2)
					trixie.setEntry(i, j, i+1);
				else
					trixie.setEntry(i, j, 0);
			}
		}
		
		trixie.setEntry(3,3, 4);
		
		trixie.print(); //printing trixie
		
		System.out.println("\n");
		
		trixie.invert().print(); //printing the inverse of trixie
		
		System.out.println("\n");
		
		trixie.times(trixie.invert()).print(); //printing the product of trixie and her inverse 
		System.out.println();
		Matrix t = new Matrix(5, 5); 
		t.m = new double[][]{{-1, 1, 1, 0, 0}, {0, 0, 1, -1, -1}, {0, 6, -2.4, 0, -9}, {0, 6, -2.4, -6, 0}, {0, 0, 0, 6, -9}};
		Matrix c = new Matrix(5, 1); 
		c.m = new double[][]{{0}, {0}, {0}, {15}, {15}}; 
		t.print(); 
		System.out.println();
		t.invert().times(t).print(); 
	}
}
