package group.shkd.app;

import group.shkd.controllers.RefuelingListDetailsController;
import group.shkd.model.RefuelingList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RefuelingListDetailsStage extends Stage {
    private RefuelingListDetailsController controller;
    private FXMLLoader loader;

    RefuelingList refuelingList;

    public RefuelingListDetailsStage() {
        this.centerOnScreen();
        this.initModality(Modality.WINDOW_MODAL);
        try {
            Scene scene = SpringStageLoader.loadScene("refuelingListDetails", it -> loader = it);
            this.setScene(scene);
            controller = loader.getController();
            controller.setStage(this);
            this.setTitle("Список на заправку");
            this.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRefuelingList(RefuelingList refuelingList) {
        controller.setRefuelingList(refuelingList);
    }
}
