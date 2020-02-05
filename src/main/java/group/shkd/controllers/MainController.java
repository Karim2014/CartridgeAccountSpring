package group.shkd.controllers;

import group.shkd.app.CartridgeEditStage;
import group.shkd.app.ExportStage;
import group.shkd.app.RefuelingListsStage;
import group.shkd.model.Cartridge;
import group.shkd.model.Repository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainController extends Controller {

    public ComboBox<String> searchComboBox;
    public Button searchButton;
    public Button searchButtonRemove;
    private Repository repository;

    public TableView<Cartridge> tableView;
    public TableColumn<String, Cartridge> name;
    public TableColumn<String, Cartridge> num;
    public TableColumn<String, Cartridge> state;
    public TableColumn<String, Cartridge> note;

    @Autowired
    public MainController(Repository repository) {
        this.repository = repository;
    }

    @FXML
    public void initialize() {
        name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        num.setCellValueFactory(new PropertyValueFactory<>("num"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        note.setCellValueFactory(new PropertyValueFactory<>("note"));

        tableView.setRowFactory(cartridgeTableView ->{
            TableRow<Cartridge> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if ((mouseEvent.getClickCount() == 2) && (!row.isEmpty())) {
                    handleEditCartridge(null);
                }
            });
            return row;
        });

        searchComboBox.setItems(FXCollections.observableArrayList("По названию", "По номеру"));

        fillData(repository.getCartridgeDao().findAll());
    }

    private void showCartridgeEditStage(Cartridge cartridge) {
        CartridgeEditStage cartridgeEditStage = new CartridgeEditStage();
        cartridgeEditStage.showDetails(cartridge);
        cartridgeEditStage.initOwner(this.getStage());
        cartridgeEditStage.showAndWait();
    }

    public void handleAddCartridge(ActionEvent actionEvent) {
        showCartridgeEditStage(new Cartridge());
        tableView.getItems().removeAll(tableView.getItems());
        tableView.getItems().addAll(repository.getCartridgeDao().findAll());
    }

    public void handleEditCartridge(ActionEvent actionEvent) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            showCartridgeEditStage(tableView.getSelectionModel().getSelectedItem());
            tableView.refresh();
        }
    }

    public void handleDeleteCartridge(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы действительно хотите удлаить картридж?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.setTitle("Удаление");
        alert.showAndWait().ifPresent(buttonType -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                repository.getCartridgeDao().delete(tableView.getSelectionModel().getSelectedItem().getId());
                tableView.getItems().removeAll(tableView.getItems());
                tableView.getItems().addAll(repository.getCartridgeDao().findAll());
            }
        });
    }

    public void handleSearchCartridge(ActionEvent actionEvent) {

    }

    public void handleSwapCartridge(ActionEvent actionEvent) {

    }

    public void handleRemoveQuerry(ActionEvent actionEvent) {

    }

    public void handleExport(ActionEvent actionEvent) {
        ExportStage exportStage = new ExportStage();
        exportStage.initOwner(getStage());
        exportStage.show();
    }

    public void fillData(List<Cartridge> cartridges) {
        tableView.setItems(FXCollections.observableList(cartridges));
    }

    public void listsItemClick(ActionEvent actionEvent) {
        RefuelingListsStage stage = new RefuelingListsStage();
        stage.initOwner(this.getStage());
        stage.show();
    }
}
