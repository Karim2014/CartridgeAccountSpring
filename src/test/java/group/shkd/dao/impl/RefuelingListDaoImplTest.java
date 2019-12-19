package group.shkd.dao.impl;

import group.shkd.dao.RefuelingListDao;
import group.shkd.model.Cartridge;
import group.shkd.model.Producer;
import group.shkd.model.RefuelingList;
import group.shkd.model.State;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

public class RefuelingListDaoImplTest {

    RefuelingListDao refuelingListDao;
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

    @Before
    public void setUp() throws Exception {
        refuelingListDao = new RefuelingListDaoImpl((DataSource) context.getBean("dataSource"));
    }

    @Test
    public void findById() {
        int searchId = 5;

        RefuelingList expected = new RefuelingList(searchId, "Список JUnit", new LinkedHashSet<>());

        RefuelingList actual = refuelingListDao.findById(searchId).orElse(null);
        Assert.assertEquals(expected, actual);

        searchId = 15;

        expected = new RefuelingList(searchId, "Список-3", new LinkedHashSet<>());
        expected.getCartridges().add(
                new Cartridge(12, new Producer(1, "CANON"), "Cartridge 703", "4", new State(1, "Заправлен в ЗиПе"), ""));
        expected.getCartridges().add(
                new Cartridge(12, new Producer(1, "CANON"), "Cartridge 703", "4", new State(1, "Заправлен в ЗиПе"), ""));
        expected.getCartridges().add(
                new Cartridge(12, new Producer(1, "CANON"), "Cartridge 703", "4", new State(1, "Заправлен в ЗиПе"), ""));
        expected.getCartridges().add(
                new Cartridge(12, new Producer(1, "CANON"), "Cartridge 703", "4", new State(1, "Заправлен в ЗиПе"), ""));



        actual = refuelingListDao.findById(searchId).orElse(null);
        Assert.assertEquals(expected, actual);

        actual = refuelingListDao.findById(2).orElse(null);
        Assert.assertNull(actual);
    }

    @Test
    public void save() {
        RefuelingList expected = RefuelingList.builder()
                .num("Список-3 JUnit Test")
                .cartridges(new LinkedHashSet<>())
                .build();

        refuelingListDao.save(expected);
        List<RefuelingList> list = refuelingListDao.findAllLight();
        RefuelingList actual = list.get(list.size()-1);
        expected.setId(actual.getId());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void update() {
        int searchId = 5;

        RefuelingList expected = new RefuelingList(searchId, "Список JUnit UPD", new LinkedHashSet<>());
        expected.getCartridges().add(
                new Cartridge(12, new Producer(1, "CANON"), "Cartridge 703", "4", new State(1, "Заправлен в ЗиПе"), ""));
        expected.getCartridges().add(
                new Cartridge(12, new Producer(1, "CANON"), "Cartridge 703", "4", new State(1, "Заправлен в ЗиПе"), ""));
        expected.getCartridges().add(
                new Cartridge(12, new Producer(1, "CANON"), "Cartridge 703", "4", new State(1, "Заправлен в ЗиПе"), ""));

        refuelingListDao.update(expected);

        RefuelingList actual = refuelingListDao.findById(searchId).orElse(new RefuelingList(searchId, "Список-N попал на Null actual", new LinkedHashSet<>()));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void delete() {
        refuelingListDao.delete(1);
        RefuelingList actual = refuelingListDao.findById(1).orElse(null);
        Assert.assertNull(actual);
    }

    @Test
    public void findAllLight() {
        RefuelingList refuelingList = new RefuelingList(1, "Список-1", new LinkedHashSet<>());
        RefuelingList refuelingList1 = new RefuelingList(5, "Список JUnit", new LinkedHashSet<>());
        RefuelingList refuelingList2 = new RefuelingList(15, "Список-3", new LinkedHashSet<>());
        RefuelingList refuelingList3 = new RefuelingList(16, "Список-4", new LinkedHashSet<>());
        RefuelingList refuelingList4 = new RefuelingList(17, "Список-5", new LinkedHashSet<>());
        RefuelingList refuelingList5 = new RefuelingList(18, "Список-3 JUnit Test", new LinkedHashSet<>());
        RefuelingList refuelingList6 = new RefuelingList(19, "Список-3 JUnit Test", new LinkedHashSet<>());

        List<RefuelingList> expected = new ArrayList<>();
        expected.add(refuelingList);
        expected.add(refuelingList1);
        expected.add(refuelingList2);
        expected.add(refuelingList3);
        expected.add(refuelingList4);
        expected.add(refuelingList5);
        expected.add(refuelingList6);

        List<RefuelingList> actual = refuelingListDao.findAllLight();
        Assert.assertEquals(expected, actual);
    }
}