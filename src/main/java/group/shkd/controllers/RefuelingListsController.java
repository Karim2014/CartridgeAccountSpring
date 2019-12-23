package group.shkd.controllers;

import group.shkd.app.RefuelingListDetailsStage;
import group.shkd.model.RefuelingList;
import group.shkd.model.Repository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RefuelingListsController extends Controller {
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
        }
    }

    public void handleDeleteRefuelingList(ActionEvent actionEvent) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы действительно хотите удалить список?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Удаление");
            alert.setHeaderText(null);
            alert.initOwner(this.getStage());
            Optional<ButtonType> button = alert.showAndWait();
            button.ifPresent(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    repository.getRefuelingListDao().delete(listView.getSelectionModel().getSelectedItem().getId());
                }
            });
        }
    }

    public void handleCloseStage(ActionEvent actionEvent) {
        getStage().close();
    }

    private void showRefuelingListDetailsStage(RefuelingList refuelingList) {
        RefuelingListDetailsStage stage = new RefuelingListDetailsStage();
        stage.setRefuelingList(refuelingList);
        stage.initOwner(this.getStage());
        stage.showAndWait();
        fillData(repository.getRefuelingListDao().findAllLight());
    }

    private void fillData(List<RefuelingList> refuelingLists) {
        listView.setItems(FXCollections.observableList(refuelingLists));
    }
}
