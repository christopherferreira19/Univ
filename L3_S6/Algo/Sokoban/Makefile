SRC =$(wildcard src/*.java)
SRC+=$(wildcard src/*/*.java)
SRC+=$(wildcard src/*/*/*.java)

CLS =$(patsubst src/%.java,src/%.class,$(SRC))

.PHONY: all
all: $(CLS)

$(CLS): %.class: %.java
	javac -cp lib/Dessin.jar:src -d obj $<

.PHONY: run
run: $(CLS)
	java -cp lib/Dessin.jar:rsc:obj Dessin.Run SokobanAuto Terrains/terrain7
