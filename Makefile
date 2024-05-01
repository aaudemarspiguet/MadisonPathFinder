runTests: FrontendDeveloperTests.class
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.grphics/com.sun.javafx.application=All-UNNAMED -jar ../junit5fx.jar -cp . -c FrontendDeveloperTests

FrontendDeveloperTests.class: FrontendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar FrontendDeveloperTests.java

BackendDeveloperTests.class: BackendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar BackendDeveloperTests.java

runBDTests: BackendDeveloperTests.java
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=All-UNNAMED -jar ../junit5fx.jar -cp . -c BackendDeveloperTests

clean:
	rm *.class

compile: Backend.java Frontend.java App.java DijkstraGraph.class
	javac --module-path ../javafx/lib --add-modules javafx.controls Backend.java Frontend.java App.java

DijkstraGraph.class: DijkstraGraph.java
	javac -cp .:../junit5.jar DijkstraGraph.java
