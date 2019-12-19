package group.shkd.dao;

import group.shkd.model.Cartridge;

import java.util.List;
import java.util.Optional;

public interface CartridgeDao extends CrudDao<Cartridge> {
    List<Cartridge> findByFullName(String fullName);
    List<Cartridge> findByNum(String num);
    List<Cartridge> findAll(String otherParams);

    boolean exists(int id);
}
