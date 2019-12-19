package group.shkd.controllers;

import group.shkd.model.Cartridge;
import group.shkd.model.RefuelingList;
import group.shkd.model.Repository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        this.oldList = this.refuelingList;
        listName.setText(refuelingList.getNum());
        fillTablesData(repository.getCartridgeDao().findAll(), this.refuelingList.getCartridges());
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
    }

    public void handleDeleteFromList(ActionEvent actionEvent) {
        Cartridge cartridge = listCartridgeTable.getSelectionModel().getSelectedItem();
        this.refuelingList.getCartridges().remove(cartridge);
        allCartridgesTable.getItems().add(cartridge);
        listCartridgeTable.getItems().remove(cartridge);
    }

    public void handleSave(ActionEvent actionEvent) {
        if (refuelingList.equals(oldList)) {
            this.refuelingList.setNum(listName.getText());
            repository.getRefuelingListDao().update(this.refuelingList);
        }
        stage.close();
    }

    public void handleCancel(ActionEvent actionEvent) {
        stage.close();
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
