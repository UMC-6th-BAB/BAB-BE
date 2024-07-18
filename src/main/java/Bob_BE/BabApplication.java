package Bob_BE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BabApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabApplication.class, args);
	}

}
