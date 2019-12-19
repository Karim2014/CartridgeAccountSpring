package group.shkd.model;

import group.shkd.dao.CartridgeDao;
import group.shkd.dao.ProducerDao;
import group.shkd.dao.RefuelingListDao;
import group.shkd.dao.StateDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Repository {
    private CartridgeDao cartridgeDao;
    private ProducerDao producerDao;
    private StateDao stateDao;
    @Autowired
    private RefuelingListDao refuelingListDao;

    @Autowired
    public Repository(CartridgeDao cartridgeDao, ProducerDao producerDao, StateDao stateDao) {
        this.cartridgeDao = cartridgeDao;
        this.producerDao = producerDao;
        this.stateDao = stateDao;
    }

    public ObservableList<Producer> getProducts() {
        return FXCollections.observableList(producerDao.findAll());
    }

    public ObservableList<State> getStates() {
        return FXCollections.observableList(stateDao.findAll());
    }

    public void insertOrUpdate(Cartridge cartridge) {
        if (cartridgeDao.exists(cartridge.getId())) {
            cartridgeDao.update(cartridge);
        } else {
            cartridgeDao.save(cartridge);
        }
    }

    public CartridgeDao getCartridgeDao() {
        return cartridgeDao;
    }

    public RefuelingListDao getRefuelingListDao() {
        return refuelingListDao;
    }
}
