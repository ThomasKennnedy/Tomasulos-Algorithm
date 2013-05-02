all:
	javac *.java
jar:
	make all
	jar cvfm ujwal-kennedy.jar manifest *.class

clean:
	rm *.class
