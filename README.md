# mandelbrot-renderer

A window to the depths of Mandelbrot sets


![Screenshot of a poly Mandelbrot](https://raw.githubusercontent.com/nikpappas/mandelbrot-renderer/main/snaps/sample.jpg)
## Build

### Prerequisites

A java 11 sdk

### Instructions

To build and test, run `./gradlew clean test`

## Run

`./gradlew run`

## GUI

The gui is mostly based on keypresses but the main mouse feature is to draw complex numbers that are included in the set
and are close to the mouse.

### Keys

#### Navigation

You navigate through the complex space using the arrow keys.

#### Scale

You zoom in/out using `=`,`-` keys

#### Mouse Scale

You expand and retract the mouse range that is rendered by pressing `,`,`.`

#### Threads
The app by default runs as many threads as many cores your system has minus one (to accomodate the actual rendering
window)
But you can decrease, increase the number of threads at will by using the `w`/`s` keys.

#### Modes
* Different equasions can be used for Mandelplotting and you can swap between those with the `0`  key.
Notice that the equasion used is render at the top of the window.
* Mouse mode on/off is with the `m` key.