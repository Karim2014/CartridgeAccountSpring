package group.shkd.dao.impl;

import group.shkd.dao.RefuelingListDao;
import group.shkd.model.Cartridge;
import group.shkd.model.Producer;
import group.shkd.model.RefuelingList;
import group.shkd.model.State;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
public class RefuelingListDaoImpl implements RefuelingListDao {
    private static final Logger log = Logger.getLogger(RefuelingListDaoImpl.class);
    private JdbcTemplate template;

    private final String SQL_INSERT_REFUELING_LIST = "INSERT INTO refuelingList (num) VALUES (?)";

    private final String SQL_UPDATE_REFUELING_LIST = "UPDATE refuelingList SET num=? WHERE id=?";

    private final String SQL_INSERT_MEDIATOR = "INSERT INTO refuelingList_cartridge (refuelingList, cartridge) VALUES (?,?)";

    private final String SQL_SELECT_ALL =
            "SELECT r.id as r_id, r.num as r_num,\n" +
            "c.id as c_id, c.name as c_name, c.num as c_num, c.note,\n" +
            "p.title as prod_title, p.id as prod_id,\n" +
            "s.[title] as state_title, s.id as state_id\n" +
            "FROM [refuelingList] as r\n" +
            "LEFT JOIN [refuelingList_cartridge] as rc ON r.id = rc.refuelingList\n" +
            "LEFT JOIN cartridge as c ON rc.cartridge=c.id\n" +
            "LEFT JOIN producer as p ON c.producer=p.id\n" +
            "LEFT JOIN states as s ON c.[state]=s.id\n";

    private final String SQL_SELECT_BY_ID = SQL_SELECT_ALL + "WHERE r.id=? ";

    private Map<Integer, RefuelingList> refuelingListMap = new HashMap<>();

    private final RowMapper<RefuelingList> rowMapper = (resultSet, i) -> {
        Integer id = resultSet.getInt("r_id");

        if (!refuelingListMap.containsKey(id)) {
            RefuelingList refuelingList = RefuelingList.builder()
                    .id(id)
                    .num(resultSet.getString("r_num"))
                    .cartridges(new LinkedHashSet<>())
                    .build();

            refuelingListMap.put(id, refuelingList);
        }

        int c_id = resultSet.getInt("c_id");
        String producerTitle  = resultSet.getString("prod_title");
        String name = resultSet.getString("c_name");

        if (c_id != 0 && producerTitle != null && name != null) {
            Cartridge cartridge = new Cartridge(
                    c_id,
                    new Producer(
                            resultSet.getInt("prod_id"),
                            producerTitle
                    ),
                    name,
                    resultSet.getString("c_num"),
                    new State(
                            resultSet.getInt("state_id"),
                            resultSet.getString("state_title")),
                    resultSet.getString("note"));
            refuelingListMap.get(id).getCartridges().add(cartridge);
        }

        return refuelingListMap.get(id);
    };

    @Autowired
    public RefuelingListDaoImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<RefuelingList> findById(int id) {
        List<RefuelingList> refuelingLists = template.query(SQL_SELECT_BY_ID, rowMapper, id);
        if (!refuelingLists.isEmpty()) {
            return Optional.of(refuelingLists.get(0));
        }
        return Optional.empty();
    }

    @Override
    public void save(RefuelingList model) {
        template.update(SQL_INSERT_REFUELING_LIST, model.getNum());
    }

    @Override
    public void update(RefuelingList model) {
        //Обновление таблицы RefuelingList
        log.info("Обновление списка " + model.getNum());
        template.update(SQL_UPDATE_REFUELING_LIST, model.getNum(), model.getId());
        //обновление промежуточной таблицы
        // сперва удаляем старые записи
        log.info("Удаление старых зависимостей");
        template.update("DELETE FROM refuelingList_cartridge WHERE refuelingList=?", model.getId());
        //findById(model.getId()
        List<Cartridge> cartridges = new ArrayList<>(model.getCartridges());
        template.batchUpdate(SQL_INSERT_MEDIATOR, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, model.getId());
                preparedStatement.setInt(2, cartridges.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return cartridges.size();
            }
        });
    }
    @Override
    public void delete(int id) {
        template.update("DELETE FROM refuelingList WHERE id=?", id);
        template.update("DELETE FROM refuelingList_cartridge WHERE refuelingList=?", id);
    }

    @Override
    public List<RefuelingList> findAllLight() {
        return template.query("SELECT * FROM refuelingList ORDER BY id DESC", (resultSet, i) -> RefuelingList.builder()
                .id(resultSet.getInt("id"))
                .num(resultSet.getString("num"))
                .cartridges(new LinkedHashSet<>())
                .build());
    }

    @Override
    public List<RefuelingList> findAll() {
        return template.query(SQL_SELECT_ALL, rowMapper);
    }
}
