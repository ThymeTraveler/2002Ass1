
#Makefile for Ass 1
#Rayhaan Omar
#Sometime in Aug

JAVAC=/usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin
DOC=doc

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<
CLASSES= medianFilter.class medianFilterParallel.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)
SOURCES= medianFilter.java medianFilterParallel.java
SOURCE_FILES= $(SOURCES:%.java=$(SRCDIR)/%.java)

default: $(CLASS_FILES)

clean: 	
	rm $(BINDIR)/*.class
runseq: $(CLASS_FILES)
	java -cp $(BINDIR) medianFilter
	@echo  $("sampleInput100.txt 3 output.txt")

doc: $(SOURCE_FILES)
	javadoc -version -author -d $(DOC) $(SOURCE_FILES)
