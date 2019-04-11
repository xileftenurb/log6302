JAR_CLASSES = binTkLinesCl.class binTkLinesMain.class csvTkLinesCl.class \
	escTkLinesCl.class escTkLinesInt.class escTkLinesMain.class sepTkLinesCl.class \
	tkLinesCl.class tkLinesMain.class

all: buzzParseMain

buzzParseMain:
	javac -Xlint -Xdiags:verbose src/javascriptParseMain.java -d out

javadoc:
	javadoc *.java

jar:
	jar cf tkLines.jar $(JAR_CLASSES)

clean:
	rm -f *~ *.class
	(cd test; make clean)
