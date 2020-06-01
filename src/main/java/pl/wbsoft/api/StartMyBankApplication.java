package pl.wbsoft.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"pl.wbsoft"})
@EntityScan({"pl.wbsoft"})
@EnableJpaRepositories({"pl.wbsoft"})
public class StartMyBankApplication {

    // start everything
    public static void main(String[] args) {
        SpringApplication.run(StartMyBankApplication.class, args);
    }

    // run this only on profile 'demo', avoid run this in test
//    @Profile("demo")
//    @Bean
//    CommandLineRunner initDatabase(AccountReposiotory repository) {
//        return args -> {
//            repository.save(new Book("A Guide to the Bodhisattva Way of Life", "Santideva", new BigDecimal("15.41")));
//            repository.save(new Book("The Life-Changing Magic of Tidying Up", "Marie Kondo", new BigDecimal("9.69")));
//            repository.save(new Book("Refactoring: Improving the Design of Existing Code", "Martin Fowler", new BigDecimal("47.99")));
//        };
//    }
}
