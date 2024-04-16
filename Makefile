runTests: BackendDeveloperTests.java Backend.java BackendInterface.java GraphADT.java GraphPlaceholder.java
	javac Backend.java BackendInterface.java GraphADT.java GraphPlaceholder.java
	javac -cp .:../junit5.jar BackendDeveloperTests.java
	java -jar ../junit5.jar -cp . -c  BackendDeveloperTests
clean:
	rm *.class
