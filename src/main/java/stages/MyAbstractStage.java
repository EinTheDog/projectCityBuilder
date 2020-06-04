package stages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

abstract class MyAbstractStage {
    protected Stage stage;

    protected MyAbstractStage(String resPath) {
        //создаем окно и корневой узел с помощью fxml
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getClassLoader().getResource("/MainMenu.fxml");
        loader.setLocation(xmlUrl);
        try {
            Parent root = loader.load();
            retainController(loader);
            //задаем сцену и окрываем окно
            stage.setScene(new Scene(root));
        } catch (IllegalStateException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR: Incorrect path to fxml file");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    protected abstract void retainController(FXMLLoader loader);

    protected void showStage() {
        stage.show();
    }

    protected Stage getInstanceStage() {
        return stage;
    }

    public void closeStage () {
        if (stage != null) stage.close();
    }

}
