package inflearn.kimyounghan.jdbc.repository;

import inflearn.kimyounghan.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.Optional;

/**
 * 스프링 예외 추상화를 통해 예외 누수 해결
 */
@Slf4j
public class MemberJdbcTemplateRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberJdbcTemplateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member (member_id, money) values (?, ?)";
        jdbcTemplate.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }

    @Override
    public Optional<Member> findById(String memberId) {
        String sql = "select money from member where member_id = ? ";
        Member member = jdbcTemplate.queryForObject(sql, rowMapper(memberId), memberId);
        return Optional.ofNullable(member);
    }

    private RowMapper<Member> rowMapper(String memberId) {
        return ((rs, rowNum) -> {
           int money = rs.getInt(1);
           return new Member(memberId, money);
        });
    }

    @Override
    public Member update(Member member) {
        String sql = "update member set money = ? where member_id = ?";
        jdbcTemplate.update(sql, member.getMoney(), member.getMemberId());
        return member;
    }

    @Override
    public void deleteById(String memberId) {
        String sql = "delete from member where member_id = ?";
        jdbcTemplate.update(sql, memberId);
    }
}
