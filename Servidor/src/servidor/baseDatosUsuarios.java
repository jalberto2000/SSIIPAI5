package servidor;

import java.sql.*;

public class baseDatosUsuarios {
    public static String getKey(String user){
        // Aqui se pone la ruta de la base de datos
        String jdbcUrl = "jdbc:sqlite:src/servidor/users.db";
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = String.format("SELECT key FROM users WHERE user = '%s'",user);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            String keyres = result.getString("key");
            return keyres;
        } catch (SQLException e) {
            System.out.println("Error insert to SQLite database");
            e.printStackTrace();
        }
        return null;
    }

    public static void insertUser(String user,String key){
        // Aqui se pone la ruta de la base de datos
        String jdbcUrl = "jdbc:sqlite:src/servidor/users.db";
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = String.format("INSERT INTO users (userId,user,key) VALUES (NULL,'%s','%s')",user,key);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error insert to SQLite database");
            e.printStackTrace();
        }
    }
    public static void main ( String args[] ) {
        String jdbcUrl = "jdbc:sqlite:src/servidor/users.db";
        //insertUser("Jesse","test");
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()){
                Integer userId = result.getInt("userId");
                String user = result.getString("user");
                String key = result.getString("key");
                System.out.println(userId + " | " + user + " | " + key);
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite database");
            e.printStackTrace();
        }

    }
}
