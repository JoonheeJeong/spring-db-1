package inflearn.kimyounghan.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static inflearn.kimyounghan.jdbc.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("connection: {}, class: {}", conn, conn.getClass());
            return conn;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
