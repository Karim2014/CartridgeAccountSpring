package group.shkd.controllers;

import group.shkd.app.RefuelingListDetailsStage;
import group.shkd.model.RefuelingList;
import group.shkd.model.Repository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
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
                    RefuelingList refuelingList = listView.getSelectionModel().getSelectedItem();
                    repository.getRefuelingListDao().delete(refuelingList.getId());
                    listView.getItems().remove(refuelingList);
                }
            });
        }
    }

    public void handleAddRefuelingList(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Новый список");
        dialog.setContentText("Введите название списка");
        dialog.setHeaderText(null);
        dialog.initOwner(getStage());
        Optional<String> text = dialog.showAndWait();
        text.ifPresent(s -> {
            repository.getRefuelingListDao().save(new RefuelingList(-1, s, null));
            fillData(repository.getRefuelingListDao().findAllLight());
        });
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
