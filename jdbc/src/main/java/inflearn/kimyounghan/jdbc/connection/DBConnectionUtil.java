package inflearn.kimyounghan.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static inflearn.kimyounghan.jdbc.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {

    private static final DBConnectionUtil INSTANCE = new DBConnectionUtil();

    private final DataSource dataSource;

    private DBConnectionUtil() {
        this.dataSource = new DriverManagerDataSource(URL);
    }

    public static DBConnectionUtil getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        try {
            Connection conn = dataSource.getConnection(USERNAME, PASSWORD);
            log.info("connection: {}, class: {}", conn, conn.getClass());
            return conn;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void close(Connection conn, PreparedStatement prepStmt, ResultSet rs) {
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
