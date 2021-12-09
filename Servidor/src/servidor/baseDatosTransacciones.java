package servidor;

import java.sql.*;

public class baseDatosTransacciones {

    public static void insertTransaction(String transactions,Boolean validity){
        // Aqui se pone la ruta de la base de datos
        String jdbcUrl = "jdbc:sqlite:src/servidor/transactions.db";
        Integer b = 0;
        if (validity){
            b = 1;
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = String.format("INSERT INTO transactions (transactionId,transactions,validity) VALUES (NULL,'%s',%d)",transactions,b);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error insert to SQLite database");
            e.printStackTrace();
        }
    }

    public static void main ( String args[] ) {
        String jdbcUrl = "jdbc:sqlite:src/servidor/transactions.db";
        //insertTransaction("transaccionValida", true);
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "SELECT * FROM transactions";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                Integer transactionId = result.getInt("transactionId");
                String transactions = result.getString("transactions");
                Boolean validity = result.getBoolean("validity");
                System.out.println(transactionId + " | " + transactions + " | " + validity);
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite database");
            e.printStackTrace();
        }
    }
}
