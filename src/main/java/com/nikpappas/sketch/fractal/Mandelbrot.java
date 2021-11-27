package com.nikpappas.sketch.fractal;

import com.nikpappas.number.Complex;
import com.nikpappas.number.ComplexAny;
import com.nikpappas.utils.collection.Couple;
import com.nikpappas.utils.collection.Pair;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Runtime.getRuntime;
import static java.util.Arrays.asList;

public class Mandelbrot extends PApplet {
    private static final int NUM_OF_THREADS = getRuntime().availableProcessors() - 1;
    private static final int MAX_SIZE_OF_POINTS = 10000000;
    private int toDrawLimit = 60000;
    private BlockingDeque<ComplexAny> toDraw;
    private List<Thread> threads;
    private boolean nearMouse = true;
    private float offsetX = 0;
    private float offsetY = 0;
    private float rangeLimit = 2;
    private float mouseRange = 0.15f;
    public static float INIT_SCALE;
    float scale;

    private boolean printPoly = true;
    private List<MandelbrotSet> mandelbrots = asList(
            MandelbrotSet.ofPoly(-1, 1, -1, 4),
            MandelbrotSet.ofSquare(100),
            MandelbrotSet.ofPower(20, 100)
    );
    private int mandelIndex = 0;
    private Runnable findBrots;


    public static void main(String[] args) {
        System.out.printf("Running with %d threads. (+1 main) %n", NUM_OF_THREADS);
        PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());

    }

    @Override
    public void settings() {
        size(900, 800);
        INIT_SCALE = (min(width, height) * (1f - 0.2f)) / 4f;
        scale = INIT_SCALE;
        toDraw = new LinkedBlockingDeque<>();
        findBrots = () -> {
            System.out.printf("Started broting %s%n", Thread.currentThread().getName());
            while (!Thread.currentThread().isInterrupted()) {
                ComplexAny c;
                if (nearMouse) {
                    Couple<Float> mouseCoords = mouseToCoords();
                    c = Complex.of(mouseCoords._1 + INIT_SCALE * random(-mouseRange, mouseRange) / scale,
                            mouseCoords._2 + INIT_SCALE * random(-mouseRange, mouseRange) / scale);
                } else {
                    c = Complex.of(INIT_SCALE * (random(-rangeLimit, rangeLimit)) / scale - offsetX,
                            INIT_SCALE * (random(-rangeLimit, rangeLimit)) / scale + offsetY);
                }
                if (mandelbrots.get(mandelIndex).contains(c)) {
                    toDraw.addLast(c);
                    if (toDraw.size() > toDrawLimit) {
                        toDraw.removeFirst();
                    }
                }
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        threads = IntStream.range(0, NUM_OF_THREADS)
                .mapToObj(x -> Pair.of(x, findBrots))
                .map(x -> new Thread(x._2, "T" + x._1))
                .collect(Collectors.toList());
        threads.forEach(Thread::start);
    }

    @Override
    public void setup() {
        frameRate(60);
    }

    @Override
    public void draw() {
        background(55);
//        background(130);
        fill(155);
        int toDrawSize = toDraw.size();
        text(toDrawSize, 10, 10);
        Couple<Float> mouseCoords = mouseToCoords();
        text(mouseCoords._1, 140, 10);
        text(mouseCoords._2, 140, 30);
        text(threads.size(), 40, 40);

        if (printPoly) {
            String mandelString = mandelbrots.get(mandelIndex).toString();
            text(mandelString, width / 2 - mandelString.length() * 3, 20);
        }
        pushMatrix();
        translate(width / 2f, height / 2f);

//        // 20% margin
//        noStroke();
//        fill(55);
//        // padding 10%
//        circle(0, 0, scale * 4.5f);
        fill(255, 60);
        noStroke();
        for (ComplexAny x : toDraw) {
            float coordX = (float) (scale * (x.real() + offsetX));
            float coordY = (float) -(scale * (x.imaginary() - offsetY));
            if (abs(coordX) < width / 2 && abs(coordY) < height / 2) {
                circle(coordX, coordY, 1);
            }
        }


        if (toDrawSize > MAX_SIZE_OF_POINTS) {
            interrupt();
        }
        popMatrix();
    }


    @Override
    public void keyPressed(KeyEvent ke) {
        if ('=' == ke.getKey()) {
            scale *= 2;
        }
        if ('-' == ke.getKey()) {
            scale /= 2;
        }
        if (UP == ke.getKeyCode()) {
            offsetY += INIT_SCALE * 0.1 / scale;
        }
        if (DOWN == ke.getKeyCode()) {
            offsetY -= INIT_SCALE * 0.1 / scale;
        }
        if (LEFT == ke.getKeyCode()) {
            offsetX += INIT_SCALE * 0.1 / scale;
        }
        if (RIGHT == ke.getKeyCode()) {
            offsetX -= INIT_SCALE * 0.1 / scale;
        }
        if ('m' == ke.getKey()) {
            nearMouse = !nearMouse;
        }
        if ('w' == ke.getKey()) {
            addOneThread();
        }
        if ('s' == ke.getKey()) {
            removeOneThread();
        }
        if ('.' == ke.getKey()) {
            mouseRange *= 2f;
            rangeLimit *=1.2;
        }
        if (',' == ke.getKey()) {
            mouseRange /= 2f;
            rangeLimit *=0.8;
        }
        if ('a' == ke.getKey()) {
            toDrawLimit *= 0.9;
        }
        if ('d' == ke.getKey()) {
            toDrawLimit *= 1.1;
        }
        if ('0' == ke.getKey()) {
            mandelIndex = (mandelIndex + 1) % mandelbrots.size();
        }
        if ('c' == ke.getKey()) {
            toDraw.clear();
        }

    }


    void interrupt() {
        threads.stream()
                .filter(Thread::isAlive)
                .forEach(x -> {
                    x.interrupt();
                    System.out.println("interrrupting " + x.getName());
                });
    }

    void removeOneThread() {
        if (threads.size() > 1) {
            Thread t = threads.get(0);
            threads.remove(t);
            t.interrupt();
        }
    }

    void addOneThread() {
        if (threads.size() < NUM_OF_THREADS) {
            Thread t = new Thread(findBrots);
            t.start();
            threads.add(t);
        }
    }

    private Couple<Float> mouseToCoords() {
        return Couple.of((mouseX - (width / 2f)) / scale - offsetX, (height / 2f - (mouseY)) / scale + offsetY);
    }
}
