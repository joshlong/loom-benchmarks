package com.example.loom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.NativeDetector;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@SpringBootApplication
public class LoomApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoomApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(@Value("${spring.threads.virtual.enabled:false}") boolean enabled) {
        return args -> System.out.println( "native? "+ NativeDetector.inNativeImage() +" virtual threads? " + enabled);
    }
}

@Controller
@ResponseBody
class CustomerHttpController {


    final CustomerRepository repository;

    CustomerHttpController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/customers")
    Collection<Customer> customers() {
        return this.repository.customers();
    }

    @GetMapping("/customers/{id}")
    Customer byId(@PathVariable Integer id) {
        return this.repository.customerById(id);
    }
}

@Repository
class CustomerRepository {

    final JdbcClient jdbc;

    final RowMapper<Customer> customerRowMapper =
            (rs, i) -> new Customer(rs.getInt("id"), rs.getString("name"));

    CustomerRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    Collection<Customer> customers() {
        return this.jdbc
                .sql("select * from customer ")
                .query(this.customerRowMapper)
                .list();
    }

    Customer customerById(Integer id) {
        return this.jdbc.sql("select * from customer where id =? ")
                .param(id)
                .query(this.customerRowMapper)
                .single();
    }
}

record Customer(Integer id, String name) {
}