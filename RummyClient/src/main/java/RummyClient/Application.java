package RummyClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
//		SpringApplication.run(Application.class, args);

//		MainController controller = new MainController();
//		controller.joinLobby("192.168.0.104", "nautik");

		try {
			UIManager.setLookAndFeel( new FlatSolarizedDarkIJTheme() );
		} catch (UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}

		//run menu window
		try{
			SwingUtilities.invokeLater(() -> new MenuWindow());
		}catch (Exception e){
			System.out.println(e.getMessage());
		}

	}

}
