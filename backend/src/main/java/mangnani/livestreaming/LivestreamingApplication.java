package mangnani.livestreaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LivestreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(LivestreamingApplication.class, args);
	}

}
