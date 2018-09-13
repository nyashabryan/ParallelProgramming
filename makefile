# Makefile
# Nyeezy

SRCDIR = src
BINDIR = bin
DOCDIR = doc
TESTDIR = test

JAVAC = javac4
JFLAGS = -g -d $(BINDIR) -cp $(BINDIR):$(JUNIT)

vpath %.java $(SRCDIR):$(TESTDIR)
vpath %.class $(BINDIR)

.SUFFIXES: .java	.class

.java.class:
	$(JAVAC)	$(JFLAGS) $<

all: Serial.class\
				Tree.class\

doc: all
	javadoc -d $(DOCDIR) $(SRCDIR)/*.java

clean:
	@rm -f $(BINDIR)/*.class
	@rm -f doc/*
