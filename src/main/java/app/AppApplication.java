package app;

import app.controllers.ControllerInterface;
import app.controllers.LoginController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication implements CommandLineRunner{
    @Autowired   
    LoginController Controller;
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        try {
            Controller.session();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
