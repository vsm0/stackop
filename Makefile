.PHONY: all build run clean

all: build run clean

build:
	mkdir -p build
	javac -d build *.java

run:
	java -cp build Main

clean:
	rm -rf build/*
