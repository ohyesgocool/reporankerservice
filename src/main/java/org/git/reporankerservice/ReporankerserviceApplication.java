package org.git.reporankerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ReporankerserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReporankerserviceApplication.class, args);
    }

}
