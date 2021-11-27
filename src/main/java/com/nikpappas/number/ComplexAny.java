package com.nikpappas.number;

public interface ComplexAny extends Comparable<ComplexAny> {
    ComplexAny square();

    double abs();

    ComplexAny add(ComplexAny c);

    ComplexAny multiply(ComplexAny c);

    double imaginary();

    double real();

    ComplexAny multiply(int i);

    default ComplexAny pow(int pow) {
        int remainder = pow % 2;
        if (pow == 1) {
            return this;
        }
        if (pow == 2) {
            return this.square();
        }
        if (remainder == 1) {
            return (this.pow(pow - 1)).multiply(this);
        }
        return (this.pow(2)).pow(pow / 2);
    }
}
