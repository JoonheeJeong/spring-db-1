package inflearn.kimyounghan.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static inflearn.kimyounghan.jdbc.connection.ConnectionConst.*;

@Slf4j
@RequiredArgsConstructor
public class DBConnectionUtil {

    private static final DBConnectionUtil INSTANCE = new DBConnectionUtil();

    private static DataSource dataSource;

    public static DBConnectionUtil getInstance() {
        return INSTANCE;
    }

    public static DBConnectionUtil getInstance(Class<? extends DataSource> datasourceClass) {
        if (datasourceClass == DriverManagerDataSource.class) {
            dataSource = getDriverManagerDataSource();
        } else if (datasourceClass == HikariDataSource.class) {
            dataSource = getHikariDataSource();
        } else {
            throw new IllegalStateException("적절한 DataSource를 찾을 수 없습니다.");
        }
        return INSTANCE;
    }

    public Connection getConnection() {
        try {
            Connection conn = DataSourceUtils.getConnection(dataSource);
            log.info("connection: {}, class: {}", conn, conn.getClass());
            return conn;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void close(Connection conn, PreparedStatement prepStmt, ResultSet rs) {
        DataSourceUtils.releaseConnection(conn, dataSource);
        JdbcUtils.closeStatement(prepStmt);
        JdbcUtils.closeResultSet(rs);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDateSource(DataSource dataSource) {
        DBConnectionUtil.dataSource = dataSource;
    }

    private static DriverManagerDataSource getDriverManagerDataSource() {
        return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    private static HikariDataSource getHikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USERNAME);
        hikariDataSource.setPassword(PASSWORD);
        return hikariDataSource;
    }
}
