/*
 * File: matrix.h
 * ------------------
 * Header file for simple implementation of a matrix.
 *
 * @author Andrew M.
 * @date March, 2016
 */

#ifndef MATRIX_H
#define MATRIX_H

#include "grid.h"

/**
 * @brief The Matrix class
 * Implementation of a 2D matrix using Grid class.
 */
class Matrix {
public:
    /**
     * @brief Matrix
     * Constructor for Matrix given rows and columns.
     */
    Matrix(int rows, int cols);
    ~Matrix();
    int getRows() const { return rows; }
    int getCols() const { return cols; }

    /**
     * Grid object which forms the basis of data storage in the matrix.
     */
    Grid<double> m;

    /**
     * Matrix operations for multiplication, transpose, invert, and copy that return
     * a new matrix.
     */
    Matrix multiply(Matrix& that);
    Matrix transpose();
    Matrix inverse();
    Matrix copy();

    /**
     * Other important matrix math functions.
     */
    void plus(Matrix& that);
    void setEntry(int row, int col, double value);
    void switchRows(int firstRow, int secondRow);

    void print();
    void rowreduce();
private:
    bool hasIdentity = false;
    int rows;
    int cols;

    const int MIN_TEN_POW = -10;
    void correctZeros(bool set);

    /**
     * Functions used for row reduction.
     */
    void zerosDown(int col);
    void zerosUp(int col);

    void scalarTimesRow(double scalar, int rowNum);
    void linearCombRows(int firstRow, int secondRow, double scalar);
    void findPivot(int col, int &pivotRow);
};

#endif // MATRIX_H

