package group.shkd.app;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher extends Application {
    private final Logger log = Logger.getLogger(Launcher.class);

    private static ClassPathXmlApplicationContext context;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        log.info("==================Initializing============");
        context = new ClassPathXmlApplicationContext("application-context.xml");
        log.info("Контекст инициализирован");
    }

    @Override
    public void start(Stage stage) {
        SpringStageLoader.loadMain(stage).show();
        log.info("Загружена главная сцена");
    }

    @Override
    public void stop() throws Exception {
        context.close();
        log.info("Контекст закрыт");
        log.info("-------------------------------------------");
    }
}
