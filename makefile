# Makefile
# Nyeezy

SRCDIR = src
BINDIR = bin
DOCDIR = doc
TESTDIR = test

JAVAC = javac
JFLAGS = -g -d $(BINDIR) -cp $(BINDIR)

vpath %.java $(SRCDIR):$(TESTDIR)
vpath %.class $(BINDIR)

.SUFFIXES: .java	.class

.java.class:
	$(JAVAC)	$(JFLAGS) $<

all: Tree.class\
	Serial.class\
	ForkedApp.class\
	ThreadedApp.class

runSerialApp:
	java -cp $(BINDIR) Serial

runThreadedApp:
	java -cp $(BINDIR) ThreadedApp

runForkedApp:
	java -cp $(BINDIR) ForkedApp

doc: all
	javadoc -d $(DOCDIR) $(SRCDIR)/*.java

clean:
	@rm -f $(BINDIR)/*.class
	@rm -f doc/*
