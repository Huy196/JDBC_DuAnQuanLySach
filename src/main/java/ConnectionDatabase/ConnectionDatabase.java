package ConnectionDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDatabase {
    private String UML = "jdbc:mysql://127.0.0.1:3306/JDBC_QuanLyBanSach";
    private String name = "root";
    private String pass = "1962005";

    Connection connection = null;

    public Connection connection(){
        try {
            connection = DriverManager.getConnection(UML,name,pass);
            System.out.println("Kết nối thành công");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }
}
