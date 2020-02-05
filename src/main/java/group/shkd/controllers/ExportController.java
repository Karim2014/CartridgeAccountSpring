package group.shkd.controllers;

import group.shkd.model.RefuelingList;
import group.shkd.model.Repository;
import group.shkd.service.ExportManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;

@Service
public class ExportController extends Controller {

    public ListView<RefuelingList> listView;
    public TextField pathField;

    private String selectedDir = /*System.getProperty("user.home") + "\\Documents\\*/"D:\\Списки картриджей\\";

    @Autowired
    private Repository repository;
    @Autowired
    private ExportManager exportManager;

    public void initialize() {
        fillData(repository.getRefuelingListDao().findAllLight());
        pathField.setText(selectedDir);
    }

    public void handleClose(ActionEvent actionEvent) {
        getStage().close();
    }

    public void handleChangeName(MouseEvent actionEvent) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            pathField.setText(selectedDir + listView.getSelectionModel().getSelectedItem().toString() + ".xlsx");
        }
    }

    public void handleExport(ActionEvent actionEvent) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Создать отчет?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {
                        exportManager.exportToPdf(pathField.getText(), listView.getSelectionModel().getSelectedItem().getId());
                    } catch (JRException | SQLException e) {

                        // Create expandable Exception.
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String exceptionText = sw.toString();

                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        Label label = new Label("The exception stacktrace was:");

                        TextArea textArea = new TextArea(exceptionText);
                        textArea.setEditable(false);
                        textArea.setWrapText(true);

                        textArea.setMaxWidth(Double.MAX_VALUE);
                        textArea.setMaxHeight(Double.MAX_VALUE);
                        GridPane.setVgrow(textArea, Priority.ALWAYS);
                        GridPane.setHgrow(textArea, Priority.ALWAYS);

                        GridPane expContent = new GridPane();
                        expContent.setMaxWidth(Double.MAX_VALUE);
                        expContent.add(label, 0, 0);
                        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
                        errorAlert.getDialogPane().setExpandableContent(expContent);
                        errorAlert.showAndWait();
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void fillData(List<RefuelingList> refuelingLists) {
        listView.setItems(FXCollections.observableList(refuelingLists));
    }
}
