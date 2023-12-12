package com.banana.bananawhatsapp.persistencia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.banana.bananawhatsapp.properties.PropertyValues;


public class DBConnector {
    PropertyValues props = new PropertyValues();

    public void connect() throws SQLException, IOException {

        String db_url = props.getPropValues().getProperty("db_url");
        System.out.println(db_url);
        Connection conn = DriverManager.getConnection(db_url);
        System.out.println("Conectado!");
        conn.close();
    }
}
