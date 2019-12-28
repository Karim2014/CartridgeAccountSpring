package group.shkd.dao.impl;

import group.shkd.dao.StateDao;
import group.shkd.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class StateDaoImpl implements StateDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StateDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<State> findById(int id) {
        return Optional.empty();
    }

    @Override
    public void save(State model) {

    }

    @Override
    public void update(State model) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<State> findAll() {
        return jdbcTemplate.query("SELECT * FROM states", new BeanPropertyRowMapper<>(State.class));
    }
}
