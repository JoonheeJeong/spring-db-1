package inflearn.kimyounghan.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

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

    public static void close(Connection conn, PreparedStatement prepStmt, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close connection", e);
            }
        }

        if (prepStmt != null) {
            try {
                prepStmt.close();
            } catch (SQLException e) {
                log.error("close prepStmt", e);
            }
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("close resultSet", e);
            }
        }
    }
}
