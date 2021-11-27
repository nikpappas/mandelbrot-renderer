package com.nikpappas.sketch.fractal;

import com.nikpappas.number.Complex;
import com.nikpappas.utils.collection.Couple;
import com.nikpappas.utils.collection.Trio;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.lang.Runtime.getRuntime;
import static java.util.stream.IntStream.range;

public class MandelbrotFileRender extends PApplet {
    private static float INIT_SCALE = 1;
    private static final int NUM_OF_THREADS = getRuntime().availableProcessors() - 1;
    private static Couple<Integer> size;
    static String fileName;
    private Future<Set<Trio<Integer>>> next;
    private Set<Trio<Integer>> current;
    static MandelbrotSet mandelbrotSet;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    float scale = INIT_SCALE;
    float offsetX = 0;
    float offsetY = 0;
    private float limitY;
    private float limitX;

    public static void main(String[] args) {
        size = Couple.of(900, 600);
        fileName = "snaps/firstrener.png";
        mandelbrotSet = MandelbrotSet.ofSquare(255);
        System.out.printf("Running with %d threads. (+1 main) %n", NUM_OF_THREADS);
        System.out.printf("Rendering a %dx%s sized image -> %s%n", size._1, size._2, fileName);
        PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());
    }

    @Override
    public void settings() {
        size(size._1, size._2);
    }

    @Override
    public void setup() {
        next = executor.submit(calculate());
    }

    @Override
    public void draw() {
        background(44);
        noStroke();
        limitX = 2 / scale;
        limitY = (size._2 / (float) size._1) * limitX;


        if (next.isDone() && !next.isCancelled()) {
            try {
                current = next.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            next.cancel(true);
        }

        if (current == null) {
            return;
        }
        current.forEach(x -> {
            if (x._3 < 0) {
                fill(255, 255, 255);
            } else {
                fill(40, 255 * (x._3 + 1) / ((float) mandelbrotSet.maxIterations), 40);
            }
            circle(x._1, x._2, 1);
        });
        save(fileName);
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        System.out.println(ke.getKey());
        if ('=' == ke.getKey()) {
            scale *= 2;
        }
        if ('-' == ke.getKey()) {
            scale /= 2;
        }
        if (UP == ke.getKeyCode()) {
            offsetY -= INIT_SCALE * 0.1 / scale;
        }
        if (DOWN == ke.getKeyCode()) {
            offsetY += INIT_SCALE * 0.1 / scale;
        }
        if (LEFT == ke.getKeyCode()) {
            offsetX -= INIT_SCALE * 0.1 / scale;
        }
        if (RIGHT == ke.getKeyCode()) {
            offsetX += INIT_SCALE * 0.1 / scale;
        }
        next = executor.submit(calculate());
    }

    Callable<Set<Trio<Integer>>> calculate() {
        return () -> range(0, size._1).boxed().flatMap(i -> {
            float x = (-limitX + 2 * limitX * i / (float) size._1) + offsetX;
            return range(0, size._2).parallel().mapToObj(j -> {
                float y = (-limitY + 2 * limitY * j / (float) size._2) + offsetY;
                return Trio.of(i, j, mandelbrotSet.iterationsToConverge(Complex.of(x, y)));
            });
        }).collect(Collectors.toSet());
    }

}
