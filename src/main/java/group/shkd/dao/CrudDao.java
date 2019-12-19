package group.shkd.dao;

import java.util.List;
import java.util.Optional;

interface CrudDao<T> {
    Optional<T> findById(int id);
    void save(T model);
    void update(T model);
    void delete(int id);

    List<T> findAll();
}
