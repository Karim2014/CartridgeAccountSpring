package group.shkd.app;

import group.shkd.controllers.RefuelingListsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RefuelingListsStage extends Stage {
    RefuelingListsController controller;
    FXMLLoader loader;

    public RefuelingListsStage() {
        this.centerOnScreen();
        //TODO Установить размеры по умолчанию
        try {
            Scene scene = SpringStageLoader.loadScene("refuelingLists", it -> loader = it);
            this.setScene(scene);
            controller = loader.getController();
            controller.setStage(this);
            this.setTitle("Списки на заправку");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
