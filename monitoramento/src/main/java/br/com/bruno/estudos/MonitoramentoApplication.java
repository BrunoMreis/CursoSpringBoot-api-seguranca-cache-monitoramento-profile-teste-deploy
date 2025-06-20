package br.com.bruno.estudos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@EnableAdminServer
public class MonitoramentoApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(MonitoramentoApplication.class, args);
    }

}
