package group.shkd.controllers;

import group.shkd.model.Cartridge;
import group.shkd.model.Producer;
import group.shkd.model.Repository;
import group.shkd.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartridgeEditController extends Controller {
    private Stage stage;
    public Tab tabCartridge;
    public Button tabCartridgeButtonCancel;
    public Button tabCartridgeButtonOk;
    public ComboBox<Producer> producer;
    public TextField name;
    public TextField fullName;
    public TextField num;
    public ComboBox<State> state;
    public TextArea note;
    private Cartridge cartridge;

    private Repository repository;

    @Autowired
    public CartridgeEditController(Repository repository) {
        this.repository = repository;
    }

    @FXML
    public void initialize() {
        producer.setItems(repository.getProducts());
        state.setItems(repository.getStates());
    }

    public void showDetails(Cartridge cartridge) {
        this.cartridge = cartridge;

        producer.setValue(cartridge.getProducer());
        name.setText(cartridge.getName());
        fullName.setText(cartridge.getFullName());
        num.setText(cartridge.getNum());
        state.setValue(cartridge.getState());
        note.setText(cartridge.getNote());

        num.setEditable(num.getText().isEmpty());
    }


    public void handleCancel(ActionEvent actionEvent) {
        stage.close();
    }

    public void handleOk(ActionEvent actionEvent) {
        //TODO Проверять уникальность картриджа
        if (!correctInput()) {
            return;
        }

        cartridge.setName(name.getText());
        cartridge.setProducer(producer.getValue());
        cartridge.setNum(num.getText());
        cartridge.setNote(note.getText());
        cartridge.setState(state.getValue());

        repository.insertOrUpdate(cartridge);

        stage.close();
    }

    private boolean correctInput() {
        boolean correct = true;
        if (name.getText().isEmpty()) {
            name.setStyle("-fx-border-color: #d55358");
            correct = false;
        }
        if (producer.getValue() == null) {
            producer.setStyle("-fx-border-color: #d55358");
            correct = false;
        }
        if (num.getText().isEmpty()) {
            num.setStyle("-fx-border-color: #d55358");
            correct = false;
        }
        return correct;
    }

    public void handleTypeCB(ActionEvent actionEvent) {
        fullName.setText(producer.getValue() + " " + name.getText());
    }

    public void keyPressed(KeyEvent keyEvent) {
        //TODO Попробовать перевести на скрипты в FXML
        fullName.setText(producer.getValue() + " " + name.getText());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
