package org.example.DataBaseCreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Creator {
    public static  void main(String[] args){
        String url = "jdbc:sqlite::eeegdata.db";

        String sql = "CREATE TABLE IF NOT EXISTS eeg_data (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " username TEXT NOT NULL,\n"
                + " electrode_number INTEGER NOT NULL,\n"
                + " base64_graph TEXT NOT NULL\n"
                + ");";
        try (Connection conn  = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            System.out.println("Tabela utworzona! ");
        } catch (SQLException e ){
            System.out.println(e.getMessage());
        }

    }
}
