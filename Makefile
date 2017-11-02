JAVAC=javac
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -f *.class schedule.txt

%.class : %.java
	$(JAVAC) $(sources)
