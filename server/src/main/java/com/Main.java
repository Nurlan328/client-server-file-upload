package com;

import com.file.Server;
import com.service.FileService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class, args);
        Server.receiveFile();

        final String endpointAddress = "http://localhost:1010/services/file";
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(FileService.class); // the SEI
        factory.setAddress(endpointAddress);
    }
}
