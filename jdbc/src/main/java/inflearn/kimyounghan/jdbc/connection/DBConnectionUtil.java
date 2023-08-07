package inflearn.kimyounghan.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

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
//        this.dataSource = new DriverManagerDataSource();

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        this.dataSource = dataSource;
    }

    public static DBConnectionUtil getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        try {
//            Connection conn = dataSource.getConnection(USERNAME, PASSWORD);
            Connection conn = dataSource.getConnection();
            log.info("connection: {}, class: {}", conn, conn.getClass());
            return conn;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void close(Connection conn, PreparedStatement prepStmt, ResultSet rs) {
        JdbcUtils.closeConnection(conn);
        JdbcUtils.closeStatement(prepStmt);
        JdbcUtils.closeResultSet(rs);
    }
}
