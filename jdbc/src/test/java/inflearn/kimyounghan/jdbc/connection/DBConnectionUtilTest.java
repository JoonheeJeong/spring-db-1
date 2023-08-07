package inflearn.kimyounghan.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

@Slf4j
class DBConnectionUtilTest {

    private final DBConnectionUtil dbConnectionUtil = DBConnectionUtil.getInstance();

    @Test
    void connection() {
        Connection conn = dbConnectionUtil.getConnection();
        Assertions.assertThat(conn).isNotNull();
    }
}