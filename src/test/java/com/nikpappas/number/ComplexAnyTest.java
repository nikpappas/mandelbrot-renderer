package com.nikpappas.number;

import com.nikpappas.utils.collection.Triplet;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplexAnyTest {

    @TestFactory
    public Stream<DynamicTest> testFact() {
        return DynamicTest
                .stream(Stream.of(
                                Triplet.of(Complex.of(2, 0), 2, Complex.of(4, 0)),
                                Triplet.of(Complex.of(2, 0), 3, Complex.of(8, 0)),
                                Triplet.of(Complex.of(2, 0), 4, Complex.of(16, 0)),
                                Triplet.of(Complex.of(2, 0), 5, Complex.of(32, 0)),
                                Triplet.of(Complex.of(2, 0), 6, Complex.of(64, 0)),
                                Triplet.of(Complex.of(2, 0), 7, Complex.of(128, 0)),
                                Triplet.of(Complex.of(2, 0), 8, Complex.of(256, 0)),
                                Triplet.of(Complex.of(0, 2), 2, Complex.of(-4, 0)),
                                Triplet.of(Complex.of(0, 2), 3, Complex.of(-0.0, -8)),
                                Triplet.of(Complex.of(0, 2), 4, Complex.of(16, -0.0)),
                                Triplet.of(Complex.of(0, 2), 5, Complex.of(0, 32)),
                                Triplet.of(Complex.of(0, 2), 6, Complex.of(-64, 0)),
                                Triplet.of(Complex.of(0, 2), 7, Complex.of(-0.0, -128)),
                                Triplet.of(Complex.of(0, 2), 8, Complex.of(256, -0.0))
                        ),
                        (p) -> format("%s^%d = %s->", p._1, p._2, p._3),
                        (p) -> {
                            ComplexAny actual = p._1.pow(p._2);
                            assertEquals(p._3, actual);

                        }
                );
    }


}