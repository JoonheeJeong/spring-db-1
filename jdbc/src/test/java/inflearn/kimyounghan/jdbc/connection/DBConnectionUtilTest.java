package inflearn.kimyounghan.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;

@Slf4j
class DBConnectionUtilTest {

    private DBConnectionUtil dbConnectionUtil;

    @Test
    void getConnectionDriverManagerDataSource() {
        dbConnectionUtil = DBConnectionUtil.getInstance(DriverManagerDataSource.class);

        assertConnectionSuccess(dbConnectionUtil);
    }

    @Test
    void getConnectionHikariDataSource() {
        dbConnectionUtil = DBConnectionUtil.getInstance(HikariDataSource.class);

        assertConnectionSuccess(dbConnectionUtil);
    }

    void assertConnectionSuccess(DBConnectionUtil dbConnectionUtil) {
        Connection conn = dbConnectionUtil.getConnection();
        Assertions.assertThat(conn).isNotNull();
    }
}