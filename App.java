import java.io.IOException;

import javafx.application.Application;

public class App {
  public static void main(String[] args) {
    System.out.println("v0.1");
    Backend backend = new Backend(new DijkstraGraph<String, Double>());
    Frontend.setBackend(backend);

    try {
      backend.loadGraphData("src/campus.dot");
      Application.launch(Frontend.class, args);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    
  }
}
