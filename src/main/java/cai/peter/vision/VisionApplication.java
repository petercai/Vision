package cai.peter.vision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "cai.peter.vision.persistence")
@ImportResource("classpath*:applicationContext.xml")
public class VisionApplication {

	public static void main(String[] args) {
		SpringApplication.run(VisionApplication.class, args);
	}

}
