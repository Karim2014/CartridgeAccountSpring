package group.shkd.controllers;

import group.shkd.model.Cartridge;
import group.shkd.model.RefuelingList;
import group.shkd.model.Repository;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static javafx.scene.control.Alert.*;

@Component
public class RefuelingListDetailsController {
    public TableView<Cartridge> allCartridgesTable;
    public ComboBox<String> searchComboBox;
    public TableColumn<String, Cartridge> allCartridgesName;
    public TableColumn<String, Cartridge> allCartridgesNum;
    public Button search;
    public TextField listName;
    public TextField searchTextField;
    public TableView<Cartridge> listCartridgeTable;
    public TableColumn<String, Cartridge> listCartridgeName;
    public TableColumn<String, Cartridge> listCartridgeNum;
    public Button save;

    private Stage stage;
    private RefuelingList refuelingList;
    private RefuelingList oldList; // Необходимо для того чтобы проверять старую версию с новой для оптимизации сохранения

    @Autowired
    Repository repository;

    public void initialize() {
        searchComboBox.setItems(FXCollections.observableArrayList("По названию", "По номеру"));

        allCartridgesName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        allCartridgesNum.setCellValueFactory(new PropertyValueFactory<>("num"));

        listCartridgeName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        listCartridgeNum.setCellValueFactory(new PropertyValueFactory<>("num"));

        allCartridgesTable.setRowFactory(cartridgeTableView -> {
            TableRow<Cartridge> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty() && mouseEvent.getButton() == MouseButton.PRIMARY) {
                    handleAddToList(null);
                }
            });
            return row;
        });

        listCartridgeTable.setRowFactory(cartridgeTableView -> {
            TableRow<Cartridge> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty() && mouseEvent.getButton() == MouseButton.PRIMARY) {
                    handleDeleteFromList(null);
                }
            });
            return row;
        });
    }

    public void setRefuelingList(RefuelingList refuelingList) {
        this.refuelingList = repository.getRefuelingListDao().findById(refuelingList.getId()).orElse(refuelingList);
        this.oldList = new RefuelingList(this.refuelingList);
        listName.setText(refuelingList.getNum());
        fillTablesData(repository.getCartridgeDao().findAll(), this.refuelingList.getCartridges());
        checkLists();
    }

    private void fillTablesData(List<Cartridge> cartridges, Set<Cartridge> refuelingListCartridges) {
        cartridges.removeAll(refuelingList.getCartridges());
        allCartridgesTable.setItems(FXCollections.observableList(cartridges));

        listCartridgeTable.setItems(FXCollections.observableList(new ArrayList<>(refuelingListCartridges)));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void handleAddToList(ActionEvent actionEvent) {
        Cartridge cartridge = allCartridgesTable.getSelectionModel().getSelectedItem();
        this.refuelingList.getCartridges().add(cartridge);
        listCartridgeTable.getItems().add(cartridge);
        allCartridgesTable.getItems().remove(cartridge);
        checkLists();
    }

    public void handleDeleteFromList(ActionEvent actionEvent) {
        Cartridge cartridge = listCartridgeTable.getSelectionModel().getSelectedItem();
        this.refuelingList.getCartridges().remove(cartridge);
        allCartridgesTable.getItems().add(cartridge);
        listCartridgeTable.getItems().remove(cartridge);
        checkLists();
    }

    private void checkLists() {
        System.out.println(oldList.getCartridges());
        System.out.println(refuelingList.getCartridges());
        save.setDisable(oldList.equals(this.refuelingList));
    }

    public void handleSave(ActionEvent actionEvent) {
        Alert alert = new Alert(AlertType.INFORMATION, "Сохранение списка");
        alert.setTitle("Сохранение");
        alert.setHeaderText(null);
        alert.initOwner(stage);
        alert.getButtonTypes().clear();
        new Thread(() -> {
            Platform.runLater(alert::show);
            repository.getRefuelingListDao().update(this.refuelingList);
            Platform.runLater(() -> {
                alert.close();
                stage.close();
            });
        }).start();
    }

    public void handleCancel(ActionEvent actionEvent) {
        if (!refuelingList.equals(oldList)) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Закрыть без сохранения?", ButtonType.NO, ButtonType.YES);
            alert.setHeaderText(null);
            alert.initOwner(stage);

            ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setDefaultButton(false);
            ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setDefaultButton(true);

            Optional<ButtonType> buttonType = alert.showAndWait();

            buttonType.ifPresent(buttonType1 -> {
                if (buttonType1 == ButtonType.YES) {
                    stage.close();
                }
            });
        } else
            stage.close();
    }

    public void handleChangeNum(KeyEvent actionEvent) {
        this.refuelingList.setNum(listName.getText());
        checkLists();
    }

    public void handleSearch(ActionEvent actionEvent) {
        List<Cartridge> cartridges = new ArrayList<>();
        String query = searchTextField.getText();
        if (searchComboBox.getValue().equals("По названию")) {
            cartridges = repository.getCartridgeDao().findByFullName(query);
        } else
        if (searchComboBox.getValue().equals("По номеру")) {
            cartridges = repository.getCartridgeDao().findByNum(query);
        }
        fillTablesData(cartridges, this.refuelingList.getCartridges());
    }

    public void handleDeleteQuery(ActionEvent actionEvent) {
        searchTextField.setText("");
        handleSearch(actionEvent);
    }
}
