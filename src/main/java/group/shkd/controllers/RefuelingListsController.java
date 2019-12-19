package group.shkd.controllers;

import group.shkd.app.RefuelingListDetailsStage;
import group.shkd.app.RefuelingListsStage;
import group.shkd.model.RefuelingList;
import group.shkd.model.Repository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RefuelingListsController {
    private Stage stage;
    public ListView<RefuelingList> listView;
    public Button add;
    public Button edit;
    public Button delete;
    public Button close;

    @Autowired
    private Repository repository;

    @FXML
    public void initialize() {
        listView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
                handleEditRefuelingList(null);
            }
        });

        fillData(repository.getRefuelingListDao().findAllLight());
    }

    public void handleEditRefuelingList(ActionEvent actionEvent) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            showRefuelingListDetailsStage(listView.getSelectionModel().getSelectedItem());
            listView.refresh();
        }
    }

    private void showRefuelingListDetailsStage(RefuelingList refuelingList) {
        RefuelingListDetailsStage stage = new RefuelingListDetailsStage();
        stage.setRefuelingList(refuelingList);
        stage.initOwner(this.stage);
        stage.show();
    }

    private void fillData(List<RefuelingList> refuelingLists) {
        listView.setItems(FXCollections.observableList(refuelingLists));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
