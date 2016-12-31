/*
 * File: polynomial.cpp
 * --------------------
 * Implementation of polynomial class.
 */

#include "polynomial.h"
#include "ssutil.h"
#include "math.h"

using namespace std;

/**
 * @brief Polynomial::addTerm
 * Add a particular degree term to the polynomial.
 * @param degree
 * Degree of new term.
 * @param coefficient
 * Coefficient of new term.
 */
void Polynomial::addTerm(int degree, double coefficient) {
    while (degree >= this->size()) {
        this->add(0);
    }
    this->set(degree, coefficient);
}

/**
 * @brief Polynomial::print
 * Print out the polynomial with x terms.
 */
void Polynomial::print() {
    cout << "P(x) = ";
    for (int ii = 0; ii < size(); ii ++) {
        cout << this->get(ii);
        if (ii < size() - 1) {
            cout << " * x^" << (size() - ii - 1);
            cout << " + ";
        }
    }
    cout << endl;
}

/**
 * @brief Polynomial::evaluate
 * Evaluate the polynomial at an x value.
 * @param x
 * X value.
 * @return
 * Y value.
 */
double Polynomial::evaluate(double x) {
    double y = 0;

    for (int ii = 0; ii < this->size(); ii ++) {
        y += this->get(ii) * pow(x, size() - ii - 1);
    }
    return y;
}

/**
 * @brief Polynomial::scaleCoefficients
 * Scale vector to increase in size when adding more coefficients.
 */
void Polynomial::scaleCoefficients() {
    int curSize = this->size();
    for (int ii = 0; ii < curSize; ii ++) {
        this->add(ZERO);
    }
}

/**
 * @brief Polynomial::printR2
 * Print polynomial r squared. See https://en.wikipedia.org/wiki/Coefficient_of_determination.
 */
void Polynomial::printR2(const Vector<double>& xValues, const Vector<double>& yValues) {
    double sstot = 0;
    double ssres = 0;
    double mean = average(yValues);
    for (int ii = 0; ii < xValues.size(); ii ++) {
         ssres += pow(yValues[ii] - evaluate(xValues[ii]), 2);
         sstot += pow(yValues[ii] - mean, 2);
    }
    double r2 = 1 - ssres/sstot;
    cout << "The r^2 value of the polynomial is " << realToString(r2) << endl;
}

/**
 * @brief ExponentialFunction::ExponentialFunction
 * Create exponential function.
 */
ExponentialFunction::ExponentialFunction(double a, double b) {
    this->a = a;
    this->b = b;
}

/**
 * @brief ExponentialFunction::~ExponentialFunction
 * Exponential function destructor.
 */
ExponentialFunction::~ExponentialFunction() { }

/**
 * @brief ExponentialFunction::printR2
 * Print exponential r squared.
 */
void ExponentialFunction::printR2(const Vector<double> &xValues, const Vector<double> &yValues) {
    double sstot = 0;
    double ssres = 0;
    double mean = average(yValues);
    for (int ii = 0; ii < xValues.size(); ii ++) {
         ssres += pow(yValues[ii] - evaluate(xValues[ii]), 2);
         sstot += pow(yValues[ii] - mean, 2);
    }
    double r2 = 1 - ssres/sstot;
    cout << "The r^2 value of the exponential is " << realToString(r2) << endl;
}
