package inflearn.kimyounghan.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static inflearn.kimyounghan.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class SpringExceptionTranslatorTest {

    private final DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

    @Test
    void badGrammarException_withRawErrorCode() {
        final String sql = "select bad grammar";

        Connection con;
        PreparedStatement stmt;
        try {
            con = dataSource.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.executeQuery();
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            log.info("errorCode: {}", errorCode, e);
            assertThat(errorCode).isEqualTo(42122);
        }
    }

    @Test
    void badGrammarException_withSpringExceptionTranslator() {
        final String sql = "select bad grammar";

        Connection con;
        PreparedStatement stmt;
        try {
            con = dataSource.getConnection();
            stmt = con.prepareStatement(sql);
            stmt.executeQuery();
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            log.info("errorCode: {}", errorCode, e);

            SQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            DataAccessException resultEx = exTranslator.translate("select", sql, e);
            log.info("resultEx", resultEx);
            assertThat(resultEx).isInstanceOf(BadSqlGrammarException.class);
        }
    }
}
