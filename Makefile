runApp: compile
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.grphics/com.sun.javafx.application=All-UNNAMED App
runTests: BackendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar BackendDeveloperTests.java
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=All-UNNAMED -jar ../junit5fx.jar -cp . -c BackendDeveloperTests
clean: 
	rm *.class
runFDTests: FrontendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar FrontendDeveloperTests.java
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=All-UNNAMED -jar ../junit5fx.jar -cp . -c FrontendDeveloperTests

compile: Backend.java Frontend.java App.java
	javac --module-path ../javafx/lib --add-modules javafx.controls Backend.java Frontend.java App.java
