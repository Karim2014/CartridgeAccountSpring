package group.shkd.app;

import group.shkd.controllers.Controller;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public final class SpringStageLoader implements ApplicationContextAware {
    public interface Callback {
        void call(FXMLLoader controller);
    }
    private static final Logger log = Logger.getLogger(SpringStageLoader.class);
    private static ApplicationContext staticContext;

    private static final String FXML_DIR = "/view/fxml/";
    private static final String MAIN_STAGE = "main";
    private static final String FXML_EXT = ".fxml";

    private static Parent load(String fxmlName, Callback callable) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SpringStageLoader.class.getResource(FXML_DIR + fxmlName + FXML_EXT));
        loader.setClassLoader(SpringStageLoader.class.getClassLoader());
        loader.setControllerFactory(staticContext::getBean);
        if (callable != null) {
            callable.call(loader);
        }
        return loader.load(SpringStageLoader.class.getResourceAsStream(FXML_DIR + fxmlName + FXML_EXT));
    }

    public static Stage loadMain(Stage stage) {
        try {
            stage.setOnHidden(windowEvent -> Platform.exit());
            stage.setTitle("Учет картриджей ШКД");
            stage.setScene(new Scene(load(MAIN_STAGE, null)));
        } catch (IOException e) {
            logError(MAIN_STAGE, e);
        }
        return stage;
    }

    public static Scene loadScene(String fxmlName, Callback callback) throws IOException {
        try {
            return new Scene(load(fxmlName, callback));
        } catch (Exception e) {
            logError(fxmlName, e);
            return new Scene(new Label("Ошибка загрузки сцены"));
        }
    }

    private static void logError(String fxmlName, Exception e) {
        log.error(String.format("Ошибка загузки сцены (%s)", fxmlName), e);
        showAlert(fxmlName, e);
        e.printStackTrace();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        staticContext = applicationContext;
    }

    private static void showAlert(String fxmlName, Exception t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(String.format("Произошла непредвиденная ошибка загрузки сцены (%s)", fxmlName));
        alert.setContentText("Детали ошибки: " + t.getMessage());
        alert.showAndWait();
    }
}
