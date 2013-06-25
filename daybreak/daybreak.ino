/*
  Based on Melody by Tom Igoe 
 
 Plays a melody 
 
 circuit:
 * speaker on digital pin 8

This example code is in the public domain.
 
 http://arduino.cc/en/Tutorial/Tone
 
 */
 #include "pitches.h"

// notes in the melody:
int melody[] = {
//  NOTE_C4, NOTE_G3,NOTE_G3, NOTE_A3, NOTE_G3,0, NOTE_B3, NOTE_C4};
 NOTE_A4, NOTE_C5, NOTE_A4, NOTE_C5, NOTE_E5
, NOTE_A4, NOTE_C5, NOTE_A4, NOTE_C5};
  

// note durations: 4 = quarter note, 8 = eighth note, etc.:
int noteDurations[] = {
//  4, 8, 8, 4,4,4,4,4 };
//12, 10, 16, 20,12 };
4, 5, 4, 3, 3
,4, 5, 4, 3};

int noteDelay[] = {
  
};

void setup() {
  // iterate over the notes of the melody:
  int size = sizeof(melody)/sizeof(melody[0]);
  for (int thisNote = 0;  thisNote< size; thisNote++) {

    // to calculate the note duration, take one second 
    // divided by the note type.
    //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
    int noteDuration = 1000/noteDurations[thisNote];
    tone(8, melody[thisNote],noteDuration);

    // to distinguish the notes, set a minimum time between them.
    // the note's duration + 30% seems to work well:
    int pauseBetweenNotes = noteDuration * 1.30;
    delay(pauseBetweenNotes);
    // stop the tone playing:
    noTone(8);
  }
}

void loop() {
  // no need to repeat the melody.
}
