package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Mpa;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Optional<Mpa> findMpaById(int mpaId) {
        String sql = "SELECT * FROM MPA WHERE MPA_ID = :mpaId";
        List<Mpa> mpas = jdbcOperations.query(sql, new MapSqlParameterSource(Map.of("mpaId", mpaId)), (rs, rowNum) -> Mpa.builder()
                .id(rs.getInt("MPA_ID"))
                .name(rs.getString("NAME"))
                .build());
        if (mpas.size() == 1) {
            var mpa = mpas.get(0);
            return Optional.of(mpa);
        }
        return Optional.empty();
    }

    @Override
    public List<Mpa> findAllMpa() {
        String sql = "SELECT * FROM MPA";
        return jdbcOperations.query(sql, (rs, rowNum) -> Mpa.builder()
                .id(rs.getInt("MPA_ID"))
                .name(rs.getString("NAME"))
                .build());
    }
}
