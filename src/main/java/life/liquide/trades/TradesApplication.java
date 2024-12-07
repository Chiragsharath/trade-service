package life.liquide.trades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"life.liquide"})
public class TradesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradesApplication.class, args);
	}

}
