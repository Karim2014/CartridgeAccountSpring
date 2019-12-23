package group.shkd.controllers;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public abstract class Controller {

    @Getter
    @Setter
    Stage stage;

}
