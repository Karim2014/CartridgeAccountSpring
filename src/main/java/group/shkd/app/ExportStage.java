package group.shkd.app;

import group.shkd.controllers.ExportController;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ExportStage extends Stage {

    FXMLLoader loader;
    ExportController controller;

    public ExportStage() {
        setTitle("Отчет");
        try {
            initModality(Modality.WINDOW_MODAL);
            setScene(SpringStageLoader.loadScene("export", it -> loader = it));
            controller = loader.getController();
            controller.setStage(this);
            setMinHeight(386.0);
            setMinWidth(372.0);
            setMaxHeight(getMinHeight() * 2.5);
            setMaxWidth(getMinWidth() * 1.5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
