/*
 * File: matrix.cpp
 * -----------------
 * Implementation of various matrix methods.
 * Inv and rowreduce. 
 *
 * @author Andrew M.
 * @date March, 2016
 */

#include "matrix.h"
#include "ssutil.h"
#include "math.h"

using namespace std;

/**
 * @brief Matrix::Matrix
 * Constructor for matrix.
 * @param rows
 * Rows in matrix.
 * @param cols
 * Columns in matrix.
 */
Matrix::Matrix(int rows, int cols) {
    this->rows = rows;
    this->cols = cols;
    this->m.resize(rows, cols);
}

/**
 * @brief Matrix::~Matrix
 * Destructor. No dynamically allocated memory stored.
 */
Matrix::~Matrix() {
}

/**
 * @brief Matrix::linearCombRows
 * Perfom linear combination of rows.
 * @param firstRow
 * First row, which will be multiplied by scalar and added to second.
 * @param secondRow
 * Second row, which will be changed.
 * @param scalar
 * Scalar used for operation.
 */
void Matrix::linearCombRows(int firstRow, int secondRow, double scalar) {
    for (int ii = 0; ii < cols; ii ++) {
        m[secondRow][ii] += scalar * m[firstRow][ii];
    }
}

/**
 * @brief Matrix::copy
 * @return
 * Copy of existing matrix.
 */
Matrix Matrix::copy() {
    Matrix copied(rows, cols);
    for (int ii = 0; ii < rows; ii ++) {
        for (int jj = 0; jj < cols; jj ++) {
            copied.setEntry(ii, jj, m[ii][jj]);
        }
    }
    return copied;
}

/**
 * @brief Matrix::inverse
 * @return
 * Inverse of matrix.
 */
Matrix Matrix::inverse() {
    Matrix withIdentity(rows, cols + rows);
    for (int ii = 0; ii < rows; ii ++) {
        for (int jj = 0; jj < cols; jj ++) {
            withIdentity.setEntry(ii, jj, m[ii][jj]);
        }
    }
    for (int ii = 0; ii < rows; ii ++) {
        for (int jj = cols; jj < cols + rows; jj ++) {
            if (ii == jj - rows) {
                withIdentity.setEntry(ii, jj, 1);
            } else {
                withIdentity.setEntry(ii, jj, 0);
            }
        }
    }
    withIdentity.rowreduce();

    Matrix inverted(rows, cols);
    for (int ii = 0; ii < rows; ii ++) {
        for (int jj = 0; jj < cols; jj ++) {
            inverted.setEntry(ii, jj, withIdentity.m[ii][jj + rows]);
        }
    }

    return inverted;
}

/**
 * @brief Matrix::transpose
 * Find the transpose of a matrix.
 * @return
 * Transpose matrix.
 */
Matrix Matrix::transpose() {
    Matrix transposeM(cols, rows);
    for (int ii = 0; ii < rows; ii ++) {
        for (int jj = 0; jj < cols; jj ++) {
            transposeM.m[jj][ii] = this->m[ii][jj];
        }
    }
    return transposeM;
}

/**
 * @brief Matrix::correctZeros
 * Sets tiny values to zero.
 */
void Matrix::correctZeros(bool set) {
    if (!set) return;
    for (int ii = 0; ii < rows; ii ++) {
        for (int jj = 0; jj < cols; jj ++) {
            double val = m[ii][jj];
            if (val < 0) {
                if (val > -1 * pow(10, MIN_TEN_POW)) {
                    m[ii][jj] = 0;
                }
            } else if (val > 0) {
                if (val < pow(10, MIN_TEN_POW)) {
                    m[ii][jj] = 0;
                }
            }
        }
    }
}

/**
 * @brief Matrix::zerosUp
 * Ensures entries above pivot are zero in column.
 * @param col
 * Column to zero up.
 */
void Matrix::zerosUp(int col) {
    int pivotRow = -1;
    findPivot(col, pivotRow);

    for (int ii = col - 1; ii >= 0; ii --) {
        if (m[ii][col] != 0) {
            double multFactor = -1 * m[ii][col] / m[col][col];
            linearCombRows(col, ii, multFactor);
        }
    }
}

/**
 * @brief Matrix::zerosDown
 * Create zeroes in a column during row reduction below pivot.
 * @param col
 * Column to perform zeros down.
 */
void Matrix::zerosDown(int col) {
    int pivotRow = -1;
    findPivot(col, pivotRow);
    if (pivotRow != col) {
        switchRows(pivotRow, col);
    }

    for (int ii = col + 1; ii < rows; ii ++) {
        if (m[ii][col] != 0) {
            double multFactor = -1 * m[ii][col] / m[col][col];
            linearCombRows(col, ii, multFactor);
        }
    }
}

/**
 * @brief Matrix::switchRows
 * Switch two matrix rows.
 * @param firstRow
 * First row to swap.
 * @param secondRow
 * Second row.
 */
void Matrix::switchRows(int firstRow, int secondRow) {
    for (int ii = 0; ii < cols; ii ++) {
        double value = m[firstRow][ii];
        m[firstRow][ii] = m[secondRow][ii];
        m[secondRow][ii] = value;
    }
}

/**
 * @brief Matrix::rowreduce
 * Rowreduce a matrix.
 */
void Matrix::rowreduce() {
    int max = 0;
    if(rows > cols) {
        max = cols;
    } else {
        max = rows;
    }
    for (int ii = 0; ii < max; ii++) {
        zerosDown(ii);

        if(m[ii][ii] != 1 && m[ii][ii] != 0){
            double multFactor = 1 / (m[ii][ii]);
            scalarTimesRow(multFactor, ii);
        }
    }

    for (int ii = 0; ii < max; ii++) {
        zerosUp(ii);

        if(m[ii][ii] != 1 && m[ii][ii] != 0){
            double multFactor = 1 / (m[ii][ii]);
            scalarTimesRow(multFactor, ii);
        }
    }
}

/**
 * @brief Matrix::findPivot
 * Find the pivot in a particular column.
 * @param col
 * Column to search for pivot.
 * @param pivotRow
 * Row to set contianing pivot.
 * @param pivotValue
 * The value of the pivot.
 */
void Matrix::findPivot(int col, int &pivotRow) {
    if (m[col][col] != 0) {
        pivotRow = col;
    } else {
        for (int ii = 0; ii < rows; ii++) {
            if (m[ii][col] != 0){
                pivotRow = ii;
                return;
            }
        }
        pivotRow = 0;
    }
}

/**
 * @brief Matrix::scalarTimesRow
 * Multiply a row by a scalar.
 * @param scalar
 * The scaler to multiply by.
 * @param rowNum
 * The row to muptiply.
 */
void Matrix::scalarTimesRow(double scalar, int rowNum) {
    for (int ii = 0; ii < cols; ii ++) {
        this->m[rowNum][ii] *= scalar;
    }
}

/**
 * @brief Matrix::print
 * Print out the matrix.
 */
void Matrix::print() {
    for (int ii = 0; ii < rows; ii ++) {
        for (int jj = 0; jj < cols; jj ++) {
            cout << this->m[ii][jj] << " | ";
        }
        cout << endl;
    }
    cout << endl;
}

/**
 * @brief Matrix::setEntry
 * Set a particular entry in a matrix.
 * @param row
 * Row to set
 * @param col
 * Column to set
 * @param value
 * New double value for grid
 */
void Matrix::setEntry(int row, int col, double value) {
    if (!m.inBounds(row, col)) return;
    m[row][col] = value;
}

/**
 * @brief Matrix::multiply
 * Multiplies two matrices.
 * @param that
 * Matrix to multiply with
 * @return
 * New matrix.
 */
Matrix Matrix::multiply(Matrix& that) {
    Matrix mult(rows, that.cols);

    for (int ii = 0; ii < rows; ii++) {
        for (int jj = 0; jj < that.cols; jj ++) {
            for (int kk = 0; kk < that.rows; kk ++) {
                double newValue = this->m[ii][kk] * that.m[kk][jj];
                mult.m[ii][jj] += newValue;
            }
        }
    }
    mult.correctZeros(false);

    return mult;
}
