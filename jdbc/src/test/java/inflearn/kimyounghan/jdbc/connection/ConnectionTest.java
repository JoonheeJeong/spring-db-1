package inflearn.kimyounghan.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static inflearn.kimyounghan.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    @Test
    void useDriverManager() throws SQLException {
        Connection conn0 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection conn1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        logTwoConnections(conn0, conn1);
    }

    @Test
    void useDriverManagerDataSource() throws SQLException {
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    void useConnectionPoolDataSource() throws SQLException, InterruptedException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool-1");

        useDataSource(dataSource);
        Thread.sleep(1000);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection conn0 = dataSource.getConnection();
        Connection conn1 = dataSource.getConnection();
        logTwoConnections(conn0, conn1);
    }

    private void logTwoConnections(Connection conn0, Connection conn1) {
        log.info("connection: {}, class: {}", conn0, conn0.getClass());
        log.info("connection: {}, class: {}", conn1, conn1.getClass());
    }
}
