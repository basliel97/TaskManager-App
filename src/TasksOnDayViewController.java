import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;
import com.mysql.cj.exceptions.ExceptionFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TasksOnDayViewController {

    private static int day;

    private static String date;

    String userId;
    String taskId;

    public static void setDay(int day) {
        TasksOnDayViewController.day = day;
    }

    public static void setDate(String date) {
        TasksOnDayViewController.date = date;
    }

    @FXML
    private TableView<TaskModel> tasksOnDay;

    @FXML
    private TableColumn<TaskModel, String> tasksOnDayLabel;

    @FXML
    private Label titleLabel;

    private DataBase db = new DataBase();
    private Connection con = db.getConnection();
    private int resultSet;
    private ResultSet resultSet1;

    ObservableList<String> taskList = FXCollections.observableArrayList();
    private ObservableList<TaskModel> tasks = FXCollections.observableArrayList();

    int userId1;

    private void getTasks(int day) {
        try {

            resultSet1 = con.createStatement()
                    .executeQuery("select userID from users where UserName = '" + LoginController.getUsername() + "';");
            while (resultSet1.next()) {

                userId1 = resultSet1.getInt("UserId");
            }
            tasksOnDay.getItems().clear();
            resultSet1 = con.createStatement().executeQuery(
                    "select * from Tasks where UserId = " + userId1 + " AND DueDate = '" + "2024-04-"+ day + "';");
            while (resultSet1.next()) {
                tasks.add(new TaskModel(
                        resultSet1.getInt("TaskID"),
                        resultSet1.getInt("UserId"),
                        resultSet1.getString("TaskName"),
                        resultSet1.getString("Description"),
                        resultSet1.getString("DueDate"),
                        resultSet1.getString("Priority"),
                        resultSet1.getString("Status"),
                        resultSet1.getString("ProjectName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws SQLException {

        try {
            titleLabel.setText("Tasks For The Day");
            tasksOnDayLabel.setCellValueFactory(new PropertyValueFactory<TaskModel, String>("TaskName"));
            tasksOnDay.setItems(tasks);
            getTasks(day);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
