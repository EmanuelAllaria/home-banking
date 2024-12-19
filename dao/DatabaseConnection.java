package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
    private static final String URL = "jdbc:h2:C:\\Repo\\home-banking\\BASEdeDATOSYAC";

    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al establecer la conexión a la base de datos.", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        if (this.connection == null) {
            throw new IllegalStateException("La conexión a la base de datos no está inicializada.");
        }
        return connection;
    }
}
