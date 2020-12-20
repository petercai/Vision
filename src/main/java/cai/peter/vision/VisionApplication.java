package cai.peter.vision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "cai.peter.vision.persistence")
@ComponentScan({"cai.peter.vision"})
public class VisionApplication {

	public static void main(String[] args) {
		SpringApplication.run(VisionApplication.class, args);
	}

}


@Configuration
@ImportResource("classpath*:applicationContext.xml")
class XmlImportingConfiguration {
}