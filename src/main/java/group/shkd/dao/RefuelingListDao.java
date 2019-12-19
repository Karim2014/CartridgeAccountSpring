package group.shkd.dao;

import group.shkd.model.RefuelingList;

import java.util.List;

public interface RefuelingListDao extends CrudDao<RefuelingList> {
    List<RefuelingList> findAllLight();

    @Deprecated
    @Override
    List<RefuelingList> findAll();
}
