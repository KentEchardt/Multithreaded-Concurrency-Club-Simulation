JAVAC=/usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=src/MonteCarloMini
BINDIR=bin/MonteCarloMini
 
$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) -sourcepath $(SRCDIR)  $< 

CLASSES=TerrainArea.class Search.class SearchParallel.class \
		MonteCarloMinimization.class MonteCarloMinimizationParallel.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

 
default: $(CLASS_FILES)
clean:
	rm $(BINDIR)/*.class
run serial: $(CLASS_FILES)
	java -cp bin MonteCarloMini/MonteCarloMinimization $(ARGS)
run parallel: $(CLASS_FILES)
	java -cp bin MonteCarloMini/MonteCarloMinimizationParallel $(ARGS)


 