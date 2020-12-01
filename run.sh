#!/bin/zsh

javac -cp src/ -d target -g src/Debugger.java
javac -cp src/ -d target -g src/AudioPlayer.java
javac -cp src/ -d target -g src/Band.java
javac -cp src/ -d target -g src/Main.java
javac -cp src/ -d target -g src/Test1.java
javac -cp src/ -d target -g src/Test2.java
javac -cp src/ -d target -g src/Test3.java

java -cp target/ Debugger