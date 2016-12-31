/*
 * File: polynomial.h
 * -------------------
 * Header file for implementation of polynomial.
 * Also contains exponential function class.
 */

#ifndef POLYNOMIAL_H
#define POLYNOMIAL_H

#include "vector.h"
#include "math.h"

using namespace std;

/**
 * @brief The Polynomial class
 * Implementation of a polynomial as a vector of doubles, which represent coefficients. The index of
 * each coefficient is the degree of the term at that location in the vector.
 */
class Polynomial : private Vector<double> {
public:
    /**
     * Create a polynomial with an empty vector of coefficients.
     */
    int degree() { return this->size(); }
    void addTerm(int degree, double coefficient);
    void clear() { this->clear(); }
    void printR2(const Vector<double>& xValues, const Vector<double>& yValues);
    /**
     * @brief evaluate
     * Evaluate the polynomial.
     */
    double evaluate(double x);
    /**
     * @brief print
     * Prints the polynomial.
     */
    void print();
private:
    void scaleCoefficients();
};

/**
 * @brief The ExponentialFunction class
 * Exponential function: y(x) = A*e^(Bx)
 */
class ExponentialFunction {
public:
    /**
     * Initialize exponential approximation function.
     */
    ExponentialFunction(double a, double b);
    ~ExponentialFunction();
    double a;
    double b;
    double evaluate(double x) { return a * exp(b * x); }
    void printR2(const Vector<double>& xValues, const Vector<double>& yValues);
    void print() {
        cout << "Y(x) = " << a << " * exp(" << b << " * x)" << endl;
    }
};

#endif // POLYNOMIAL_H

