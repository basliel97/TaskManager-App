import java.sql.Connection;
import java.sql.ResultSet;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateProjectController {

    @FXML
    private JFXButton createBTN;

    @FXML
    private JFXTextField projectNameTF;

    @FXML
    private VBox vBox;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private DataBase db = new DataBase();
    private Connection con = db.getConnection();
    private int resultSet;
    private ResultSet resultSet1;
    int userId;

    @FXML
    void onCreateHandler(ActionEvent event) throws Exception {

        try {
            if (projectNameTF.getText().isEmpty()) {
                showAlert("Error", "Please fill Neccessary fields.");
                throw new Exception("Custom Exception");
            } else {
                resultSet1 = con.createStatement().executeQuery(
                        "select UserID from Users where UserName = '" + LoginController.getUsername() + "';");
                while (resultSet1.next()) {

                    userId = resultSet1.getInt("UserID");

                }

                resultSet = con.createStatement()
                        .executeUpdate("insert into project (ProjectName , UserID) values ('" + projectNameTF.getText()
                                + "'," + userId + ");");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("project Added Correctly");
                alert.showAndWait();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
