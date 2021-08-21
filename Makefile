
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
CLASSES= BinaryTreeNode.class BTQueueNode.class BTQueue.class BinaryTree.class BinarySearchTree.class AVLTree.class AccessAVLApp.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)
SOURCES= AccessArrayApp.java 
SOURCE_FILES= $(SOURCES:%.java=$(SRCDIR)/%.java)

default: $(CLASS_FILES)

clean: 	
	rm $(BINDIR)/*.class
runarray: $(CLASS_FILES)
	java -cp $(BINDIR) AccessArrayApp

runbst: $(CLASS_FILES)
	java -cp $(BINDIR) AccessBSTApp

runavl:  $(CLASS_FILES)
	java -cp $(BINDIR) AccessAVLApp

doc: $(SOURCE_FILES)
	javadoc -version -author -d $(DOC) $(SOURCE_FILES)
