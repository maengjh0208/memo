package com.sparta.memo.repository;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

// bean 객체로 등록
//@Component
// Repository 역할을 하는 Component
@Repository
public class MemoRepository {
    private final JdbcTemplate jdbcTemplate;

    // 해당 클래스가 bean 객체로 등록하게끔 되어는 있는데
    // 객체가 만들어지려면 아래처럼 jdbcTemplate 가 주입이 되어야함.
    // 근데 @Component 만으로는 자동적으로 주입이 안됨
    // @Autowired가 자동적으로 주입해주는 역할을 함.
    // 예전에는 생성자에 @Autowired를 꼭 달아줘야 했었는데 지금은 생성자가 하나만 있을 경우 생략이 가능함
    // 하지만 생성자 여러개 있으면 @Autowired 써줘야함.
    // 이런 주입은 생성자 주입, 필드 주입, 메서드 주입이 있는데 보통은 생성자 주입을 선호하는 편이다. 객체의 불변성을 지켜줄 수 있어서.
    @Autowired
    public MemoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Memo save(Memo memo) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, memo.getUsername());
                    preparedStatement.setString(2, memo.getContents());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        memo.setId(id);

        return memo;
    }

    public List<MemoResponseDto> findALL() {
        // DB 조회
        String sql = "SELECT * FROM memo";

        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                return new MemoResponseDto(id, username, contents);
            }
        });
    }

    public void update(Long id, MemoRequestDto requestDto) {
        // memo 내용 수정
        String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);
    }

    public Memo findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Memo memo = new Memo();
                memo.setUsername(resultSet.getString("username"));
                memo.setContents(resultSet.getString("contents"));
                return memo;
            } else {
                return null;
            }
        }, id);
    }

    public void delete(Long id) {
        // memo 삭제
        String sql = "DELETE FROM memo WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
