package group.shkd.service;

import group.shkd.dao.RefuelingListDao;
import group.shkd.dao.impl.RefuelingListDaoImpl;
import net.sf.jasperreports.engine.JRException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

import java.io.IOException;
import java.sql.SQLException;

public class ExportManagerTest {

    RefuelingListDao refuelingListDao;
    private ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

    @Before
    public void setUp() throws Exception {
        refuelingListDao = new RefuelingListDaoImpl((DataSource) context.getBean("dataSource"));
    }

    @Test
    @Deprecated
    public void export() throws IOException {
        new ExportManager().exportToExcelViaPoi("exportTest.xlsx", refuelingListDao.findById(16).get());
    }

    @Test
    public void exportToPdf() {
        try {
            ((ExportManager) context.getBean("exportManager")).exportToPdf("", 16);
            //new ExportManager().exportToPdf("exportTest.xlsx", refuelingListDao.findById(16).get());
        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}