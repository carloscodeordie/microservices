package com.microservices.twitter;

import com.microservices.twitter.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwitterServiceApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterServiceApplication.class);

    private final StreamRunner streamRunner;

    public TwitterServiceApplication(StreamRunner runner) {
        streamRunner = runner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("System starts...");
        streamRunner.start();
    }
}
