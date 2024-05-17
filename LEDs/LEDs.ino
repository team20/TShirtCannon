// How to set up (aka what to do if LEDs are not working):
// #1 Make sure the correct board is selected (Arduino Nano), the right comm is selected (changes per pc)
// #2 LED_PIN (check on the board to see what the LEDs are plugged into and change the constant)
// #3 What the type of LEDs they are, though we usually use Neopixels they might be different, check with electrical,
// 	  the other type is making sure that the pixel type flags are correct (whether the original LEDs are RBG, BRG, GRB, etc)
// #4 Try to change the bootloader (you can find this in the IDE under Tools -> Processor)
// #5 Is the Bitstream right?
// #6 The code itself is likely fine so try not to touch that, if anything change the inline commmands in RobotContainer
// #7 It should tell you if it isn't but just in case make sure your libraries are downloaded and updated

#include <Adafruit_NeoPixel.h>
#include <Wire.h>
// Which pin on the Arduino is connected to the NeoPixels?
// On a Trinket or Gemma we suggest changing this to 1:
#define LED_PIN 6

// How many NeoPixels are attached to the Arduino?
#define MAIN_LOOP_LED_COUNT 168
#define CANNON_LED_COUNT 10
#define SIDE_LED_COUNT 40
// Declare our NeoPixel strip object:
// Argument 1 = Number of pixels in NeoPixel strip
// Argument 2 = Arduino pin number (most are valid)
// Argument 3 = Pixel type flags, add together as needed, refer to Adafruit_NeoPixel.h
Adafruit_NeoPixel leftStrip(CANNON_LED_COUNT, 11, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel middleStrip(CANNON_LED_COUNT, 5, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel rightStrip(CANNON_LED_COUNT, 12, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel mainLoop(MAIN_LOOP_LED_COUNT, LED_PIN, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel leftTriangle(SIDE_LED_COUNT, 3, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel rightTriangle(SIDE_LED_COUNT, 4, NEO_GRB + NEO_KHZ800);

/*
 * for additional flags(in the event the LEDs don't display the color you want, you
 * probably need a different bitstream):
 * NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
 * NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
 * NEO_BRG     Pixels are wired for BRG bitstream (whatever LED Strip 2023 uses)
 * NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
 * NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
 * NEO_RGBW    Pixels are wired for RGBW bitstream (NeoPixel RGBW products)
 */

// Do not use old bootloader

/**
 * @brief Shorthand for creating colors in RGB format
 * @param r Red, 0-255
 * @param g Green, 0-255
 * @param b Blue, 0-255
 * @return The color
 */
uint32_t color(int r, int g, int b) {
	return Adafruit_NeoPixel::Color(r, g, b);
}
// frame variable, changes from loop
int colorIndex = 0;
// pattern led strips are on, read in from master/robot
int pattern = 2;
uint32_t teamColor = color(0, 255, 0);
uint32_t noteColor = color(255, 77, 0);
uint32_t offColor = color(0, 0, 0);

/// @brief An array of 6 colors in rainbow order(ROY G BV)
uint32_t RainbowColor[] = {
    color(255, 0, 0),
    color(255, 165, 0),
    color(255, 255, 0),
    color(0, 255, 0),
    color(0, 0, 255),
    color(148, 0, 211)};

void setup() {
	leftStrip.begin();
	middleStrip.begin();
	rightStrip.begin();
	mainLoop.begin();
	leftTriangle.begin();
	rightTriangle.begin();

	leftStrip.setBrightness(100);
	middleStrip.setBrightness(100);
	rightStrip.setBrightness(100);
	mainLoop.setBrightness(100);  // Set BRIGHTNESS to about 1/5 (max = 255)
	leftTriangle.setBrightness(100);
	rightTriangle.setBrightness(100);
	Serial.begin(9600);
	Wire.begin(0x20);
}

void setCannonStrip(Adafruit_NeoPixel strip, int numLEDs) {
	for (size_t i = 0; i < numLEDs; i++) {
		strip.setPixelColor(i, color(255, 255, 255));
	}
}

void loop() {
	if (Wire.peek() == 50) {
		if (Wire.available() >= 4) {
			Wire.read();
			int left = Wire.read();
			int middle = Wire.read();
			int right = Wire.read();
			setCannonStrip(leftStrip, left);
			setCannonStrip(middleStrip, middle);
			setCannonStrip(rightStrip, right);
		}
	} else if (Wire.peek() == 51) {
		if (Wire.available() >= 2) {
			Wire.read();
			switch (Wire.read()) {
				case 1:
					leftStrip.clear();
					break;
				case 2:
					middleStrip.clear();
					break;
				case 3:
					rightStrip.clear();
					break;
				default:
			}
		}
	}
	switch (pattern) {  // sets up lights to patterns
		                // note: every function returns a color based on colorIndex, the pixel index, and optional color parameters.
		                // the for loops set the pixels to have their corresponding colors based on the pattern function on the colorIndex frame
		case 0:         // reset code
			colorIndex = 0;
			pattern = -1;
		case 1:  // RainbowPartyFunTime!!
			for (int i = 0; i < MAIN_LOOP_LED_COUNT; i++) {
				mainLoop.setPixelColor(i, RainbowPartyFunTime(colorIndex, i));
			}
			for (int i = 0; i < SIDE_LED_COUNT; i++) {
				leftTriangle.setPixelColor(i, RainbowPartyFunTime(colorIndex, i));
				rightTriangle.setPixelColor(i, RainbowPartyFunTime(colorIndex, i));
			}
			delay(75);
			break;
		case 2:  // Smooth RainbowPartyFunTime
			mainLoop.rainbow((65535 / (MAIN_LOOP_LED_COUNT / 4)) * (colorIndex % (MAIN_LOOP_LED_COUNT / 4)), 5);
			leftTriangle.rainbow((65535 / (SIDE_LED_COUNT / 4)) * (colorIndex % (SIDE_LED_COUNT / 4)), 5);
			rightTriangle.rainbow((65535 / (SIDE_LED_COUNT / 4)) * (colorIndex % (SIDE_LED_COUNT / 4)), 5);
			break;
		case 5:  // Shen colors
			for (int i = 0; i < MAIN_LOOP_LED_COUNT; i++) {
				mainLoop.setPixelColor(i, TheaterLightsDouble(i + colorIndex, color(255, 255, 255), color(0, 255, 0)));
			}
			for (int i = 0; i < SIDE_LED_COUNT; i++) {
				leftTriangle.setPixelColor(i, TheaterLightsDouble(i + colorIndex, color(255, 255, 255), color(0, 255, 0)));
				rightTriangle.setPixelColor(i, TheaterLightsDouble(i + colorIndex, color(255, 255, 255), color(0, 255, 0)));
			}
			delay(100);
			break;
		default:  // display team color
			for (int i = 0; i < MAIN_LOOP_LED_COUNT; i++) {
				mainLoop.setPixelColor(i, teamColor);
			}
			for (int i = 0; i < SIDE_LED_COUNT; i++) {
				leftTriangle.setPixelColor(i, teamColor);
				rightTriangle.setPixelColor(i, teamColor);
			}
			delay(150);
			break;
	}
	leftStrip.show();
	middleStrip.show();
	rightStrip.show();
	mainLoop.show();
	leftTriangle.show();
	rightTriangle.show();
	colorIndex++;  // next frame
}

void serialEvent() {
	pattern = Serial.read();
}

/**
 * @brief Makes the LED strip display all the colors of the rainbow while the rainbow moves
 * @param c The colorIndex
 * @param i LED number
 * @return The color
 */
uint32_t RainbowPartyFunTime(int c, int i) {
	return RainbowColor[(c + i) % 6];
}

/**
 * @brief Makes individual LEDs switch back and forth between two colors
 * @param c Color index
 * @param i LED number
 * @param color1 A color
 * @param color2 The other color
 * @return The color that the LED should show
 */
uint32_t TheaterLights(int i, uint32_t color1, uint32_t color2) {  // pixel on color 1 or 2 depending on frame
	if (i % 2) {
		return color1;
	}
	return color2;
}

uint32_t TheaterLightsDouble(int i, uint32_t color1, uint32_t color2) {
	if (i % 4 < 2) {
		return color1;
	}
	return color2;
}

/**
 * @brief Makes LEDs switch back and forth between colors,
 * using color index will make the whole strip alternate colors,
 * using LED number will make even numbered LEDs one color, and odd numbered LEDs another color
 * @param x A number determining the color to use
 * @param color3 A color
 * @param color4 The other color
 * @return The color for the LED
 */
uint32_t BlinkingLights(int x, uint32_t color3, uint32_t color4) {
	if (x % 2 == 0) {
		return color3;
	}
	return color4;
}

uint32_t ProgressBar(int colorIndex, int i, int ledCount, uint32_t ledColor) {
	int total = ledCount * 2;
	int ledRange = colorIndex % ledCount;
	// First half
	if ((colorIndex % total) < ledCount) {
		if (ledRange >= i) {
			return ledColor;
		}
		return color(0, 0, 0);
	}
	if (ledRange < ledCount - i - 1) {
		return ledColor;
	}
	return color(0, 0, 0);
}
