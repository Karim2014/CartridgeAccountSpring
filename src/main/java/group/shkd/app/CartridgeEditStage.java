package group.shkd.app;

import group.shkd.controllers.CartridgeEditController;
import group.shkd.controllers.MainController;
import group.shkd.model.Cartridge;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class CartridgeEditStage extends Stage {
    private static final Logger log = Logger.getLogger(CartridgeEditStage.class);

    private FXMLLoader loader;
    private CartridgeEditController controller;

    public CartridgeEditStage() {
        this.initModality(Modality.WINDOW_MODAL);
        this.centerOnScreen();
        try {
            Scene scene = SpringStageLoader.loadScene("cartridgeDetails", it -> loader = it);
            this.setScene(scene);
            controller = loader.getController();
            this.setTitle("Картридж");
            controller.setStage(this);
        } catch (Exception e) {
            log.error("CartridgeEditStage", e);
            e.printStackTrace();
        }
    }

    public void showDetails(Cartridge cartridge) {
        controller.showDetails(cartridge);
    }
}
