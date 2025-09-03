package co.com.pragma.crediya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);

        /*PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("123321");
        System.out.println("Hashed password: " + hashedPassword);*/
    }
}
