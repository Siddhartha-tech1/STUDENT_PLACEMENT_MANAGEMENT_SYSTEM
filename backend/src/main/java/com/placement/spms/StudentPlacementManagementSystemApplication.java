package com.placement.spms;

import com.placement.spms.util.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentPlacementManagementSystemApplication {
    public static void main(String[] args) {
        DotenvLoader.load();
        SpringApplication.run(StudentPlacementManagementSystemApplication.class, args);
    }
}
