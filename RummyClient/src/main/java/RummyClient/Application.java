package RummyClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		MainController controller = new MainController();
		controller.joinLobby("192.168.0.104", "nautik");

	}

}
