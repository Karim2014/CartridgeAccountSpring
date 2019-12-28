package group.shkd.dao.impl;

import group.shkd.dao.ProducerDao;
import group.shkd.model.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ProducerDaoImpl implements ProducerDao {

    private JdbcTemplate template;

    @Autowired
    public ProducerDaoImpl(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Producer> findById(int id) {
        return Optional.empty();
    }

    @Override
    public void save(Producer model) {

    }

    @Override
    public void update(Producer model) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Producer> findAll() {
        return template.query("SELECT * FROM producer", new BeanPropertyRowMapper<>(Producer.class));
    }
}
