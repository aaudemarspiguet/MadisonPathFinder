runTests: compile BackendDeveloperTests.java
	javac -cp .:../junit5.jar BackendDeveloperTests.java
	java -jar ../junit5.jar -cp . -c  BackendDeveloperTests

runApp: compile
	java App

clean: 
	rm *.class

runFDTests: compile FrontendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar FrontendDeveloperTests.java
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.grphics/com.sun.javafx.application=All-UNNAMED -jar ../junit5fx.jar -cp . -c FrontendDeveloperTests

compile: Backend.java BackendInterface.java GraphADT.java GraphPlaceholder.java BaseGraph.java DijkstraGraph.java Frontend.java FrontendInterface.java HashtableMap.java MapADT.java  App.java
	javac Backend.java BackendInterface.java GraphADT.java GraphPlaceholder.java BaseGraph.java
	javac Frontend.java FrontendInterface.java HashtableMap.java MapADT.java App.java
	javac -cp .:../junit5.jar DijkstraGraph.java
