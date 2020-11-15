#!/bin/zsh

javac -cp src/ -d target -g src/Debugger.java
javac -cp src/ -d target -g src/AudioPlayer.java
javac -cp src/ -d target -g src/InstrumentPiano.java
javac -cp src/ -d target -g src/InstrumentGuitar.java
javac -cp src/ -d target -g src/InstrumentMain.java

java -cp target/ Debugger