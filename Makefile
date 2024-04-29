runTests: FrontendDeveloperTests.class
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.grphics/com.sun.javafx.application=All-UNNAMED -jar ../junit5fx.jar -cp . -c FrontendDeveloperTests

FrontendDeveloperTests.class: FrontendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar FrontendDeveloperTests.java

runBDTests: BackendDeveloperTests.java Backend.java BackendInterface.java GraphADT.java GraphPlaceholder.java
	javac Backend.java BackendInterface.java GraphADT.java GraphPlaceholder.java
	javac -cp .:../junit5.jar BackendDeveloperTests.java
	java -jar ../junit5.jar -cp . -c  BackendDeveloperTests

clean:
	rm *.class
