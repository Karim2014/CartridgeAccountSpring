package group.shkd.dao.impl;

import group.shkd.dao.CartridgeDao;
import group.shkd.dao.impl.CartridgeDaoImpl;
import group.shkd.model.Cartridge;
import group.shkd.model.Producer;
import group.shkd.model.State;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class CartridgeDaoImplTest {

    private CartridgeDao cartridgeDao;
    private ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

    @Before
    public void setUp() throws Exception {
        cartridgeDao = new CartridgeDaoImpl((DataSource) context.getBean("testDataSource"));
    }

    @Test
    public void exists() {
        boolean actual = cartridgeDao.exists(11);
        Assert.assertTrue(actual);
        actual = cartridgeDao.exists(1);
        Assert.assertFalse(actual);
    }

    @Test
    public void findById() {
        Cartridge cartridgeActual = cartridgeDao.findById(11).orElse(null);

        Cartridge cartridgeExpected = Cartridge.builder()
                .id(11)
                .name("Cartridge 703")
                .note("")
                .num("3")
                .state(new State(4, "Установлен"))
                .producer(new Producer(1, "CANON"))
                .build();

        Assert.assertEquals(cartridgeExpected, cartridgeActual);
    }

    @Test
    public void save() {
        Cartridge cartridge = Cartridge.builder()
                .name("JUnit Test Cartridge")
                .note("JUnit 4 Test")
                .num("4")
                .producer(new Producer(2, "Gestetner"))
                .state(new State(2, "Не заправлен в ЗиПе"))
                .build();

        cartridgeDao.save(cartridge);

        List<Cartridge> tmp = cartridgeDao.findAll();
        Cartridge cartridgeActual = tmp.get(tmp.size()-1);
        cartridge.setId(cartridgeActual.getId());
        Assert.assertEquals(cartridge, cartridgeActual);
    }

    @Test
    public void update() {
        Cartridge cartridge = Cartridge.builder()
                .id(11)
                .name("Cartridge 7015")
                .note("JUnit Test")
                .num("132")
                .producer(new Producer(2, "Gestetner"))
                .state(new State(2, "Не заправлен в ЗиПе"))
                .build();

        cartridgeDao.update(cartridge);

        Cartridge cartridgeActual = cartridgeDao.findById(11).orElse(null);
        Assert.assertEquals(cartridge, cartridgeActual);
    }

    @Test
    public void delete() {
        cartridgeDao.delete(75);
        Assert.assertFalse(cartridgeDao.exists(75));
    }

    @Test
    public void findByFullName() {
        List<Cartridge> expected = new ArrayList<>();
        expected.add(new Cartridge(27, new Producer(1, "CANON"), "E16", "1", new State(2, "Не заправлен в ЗиПе"), ""));
        expected.add(new Cartridge(28, new Producer(1, "CANON"), "E16", "2", new State(2, "Не заправлен в ЗиПе"), ""));

        List<Cartridge> actual = cartridgeDao.findByFullName("E16");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findByNum() {
        List<Cartridge> expected = new ArrayList<>();
        expected.add(new Cartridge(66, new Producer(2, "Gestetner"), "SP3400", "MA951200776", new State(4, "Установлен"), ""));

        List<Cartridge> actual = cartridgeDao.findByNum("776");

        Assert.assertEquals(expected, actual);
    }
}