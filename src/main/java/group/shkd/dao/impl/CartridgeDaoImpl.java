package group.shkd.dao.impl;

import group.shkd.dao.CartridgeDao;
import group.shkd.model.Cartridge;
import group.shkd.model.Producer;
import group.shkd.model.State;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class CartridgeDaoImpl implements CartridgeDao {
    private final Logger log = Logger.getLogger(CartridgeDaoImpl.class);
    private JdbcTemplate template;

    private final String SQL_FIND_ALL =
            "SELECT c.*, p.title as producer_title, s.title as state_title " +
            "FROM cartridge as c " +
            "LEFT JOIN producer as p ON c.producer = p.id " +
            "LEFT JOIN states as s ON c.state = s.id ";

    private final String ORDER_BY_NAME = "ORDER BY c.full_name ";

    private final String SQL_FIND_BY_ID =
            SQL_FIND_ALL + "WHERE c.id = ? ";

    private final String SQL_UPDATE =
            "UPDATE cartridge " +
                    "SET name=?, producer=?, state=?, note=? " +
                    "WHERE id=?";

    private final String SQL_INSERT =
            "INSERT INTO cartridge (name, producer, num, state, note) VALUES (?,?,?,?,?)";

    private final String SQL_DELETE = "DELETE FROM cartridge WHERE id = ?";

    private RowMapper<Cartridge> rowMapper = (resultSet, i) -> new Cartridge(
            resultSet.getInt("id"),
            new Producer(
                    resultSet.getInt("producer"),
                    resultSet.getString("producer_title")),
            resultSet.getString("name"),
            resultSet.getString("num"),
            new State(
                    resultSet.getInt("state"),
                    resultSet.getString("state_title")),
            resultSet.getString("note")
    );

    @Autowired
    public CartridgeDaoImpl(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Cartridge> findByFullName(String fullName) {
        if (!fullName.isEmpty()) {
            fullName = String.format("%s%s%s", "%", fullName, "%");
            return template.query(SQL_FIND_ALL + "WHERE c.full_name LIKE ?" + ORDER_BY_NAME, rowMapper, fullName);
        }
        return findAll();
    }

    @Override
    public List<Cartridge> findByNum(String num) {
        if (!num.isEmpty()) {
            num = String.format("%s%s%s", "%", num, "%");
            return template.query(SQL_FIND_ALL + "WHERE c.num LIKE ? " + ORDER_BY_NAME, rowMapper, num);
        }
        return findAll();
    }

    @Override
    public boolean exists(int id) {
        return findById(id).isPresent();
    }

    @Override
    public Optional<Cartridge> findById(int id) {
        log.info("Поиск картриджа с id=" + id);
        List<Cartridge> cartridges = template.query(SQL_FIND_BY_ID, rowMapper, id);

        if (!cartridges.isEmpty()) {
            return Optional.of(cartridges.get(0));
        }
        return Optional.empty();
    }

    @Override
    public void save(Cartridge model) {
        log.info(String.format("Вставка картриджа %s", model.toString()));
        template.update(SQL_INSERT, model.getName(), model.getProducer().getId(), model.getNum(), model.getState().getId(),
                model.getNote());
    }

    @Override
    public void update(Cartridge model) {
        log.info(String.format("Обновление картриджа с id=%s (%s)", model.getId(), model.toString()));
        template.update(SQL_UPDATE, model.getName(), model.getProducer().getId(), model.getState().getId(),
                model.getNote(), model.getId());
    }

    @Override
    public void delete(int id) {
        log.info(String.format("Удаление картриджа с id=%d", id));
        template.update(SQL_DELETE, id);
    }

    @Override
    public List<Cartridge> findAll() {
        log.info("Выборка всех картриджей");
        return findAll(ORDER_BY_NAME);
    }

    @Override
    public List<Cartridge> findAll(String otherParams) {
        log.info("Выборка всех картриджей с другими параметрами");
        return template.query(SQL_FIND_ALL + otherParams, rowMapper);
    }
}
