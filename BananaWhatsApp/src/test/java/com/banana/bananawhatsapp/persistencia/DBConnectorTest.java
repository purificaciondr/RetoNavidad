package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.config.SpringConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
class DBConnectorTest {
    //@Autowired
    //private DBConnector dbc;

    DBConnector dbc = new DBConnector();
    @Test
    void connect_isOK() {
        try {
            dbc.connect();
            assertTrue(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}