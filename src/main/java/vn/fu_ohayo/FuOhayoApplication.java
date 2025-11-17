package vn.fu_ohayo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FuOhayoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuOhayoApplication.class, args);
	}

}
