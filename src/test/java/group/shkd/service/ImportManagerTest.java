package group.shkd.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import java.io.IOException;

import static org.junit.Assert.*;

public class ImportManagerTest {
    private ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

    private ImportManager importManager;

    @Before
    public void setUp() throws Exception {
        //importManager = (ImportManager) context.getBean("importManager");
    }

    @Test
    public void execute() throws IOException {
        /*Assert.notNull(importManager, "import manager");
        importManager.execute();*/
    }
}