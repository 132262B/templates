package com.app;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;


    @BeforeEach
    void setUp() {

        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();
    }

}
