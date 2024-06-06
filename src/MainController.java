import java.awt.Color;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {

    private LocalDate currentDate;
   public static void setMonth(int month) {
        MainController.month = month;
    }

static int month;

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXButton calanderBTN;

    @FXML
    private JFXButton myDayBTN;

    @FXML
    private JFXButton nextBTN;

    @FXML
    private JFXButton tasksBTN;

    @FXML
    private JFXButton userBTN;

    @FXML
    private JFXButton projectBTN;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private DataBase db = new DataBase();
    private Connection con = db.getConnection();
    private ResultSet resultSet;

    @FXML
    void onProjectHandler(ActionEvent event) throws IOException {

        borderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("CreateTask.fxml")));

    }

    @FXML
    void onLogOutHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log OUT ?");
        alert.setHeaderText(null);
        alert.setContentText("Are You Sure You Want To Logout");
        alert.showAndWait();
        if (alert.getResult().getButtonData().isCancelButton()) {

        } else {

            root = (Parent) FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private JFXButton logoutBTN;

    @FXML
    void onCalanderHandler(ActionEvent event) {

        currentDate = LocalDate.now();

        GridPane calendarGrid = new GridPane();
        calendarGrid.setPadding(new Insets(10));
        calendarGrid.setHgap(0);
        calendarGrid.setVgap(0);

        // Weekday labels
        String[] weekdays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
        for (int i = 0; i < weekdays.length; i++) {
            Label weekdayLabel = new Label(weekdays[i]);
            calendarGrid.add(weekdayLabel, i, 0);
        }

        updateCalendarGrid(calendarGrid);
        Label monthYearLabel = new Label(currentDate.getMonth().toString() + " " + currentDate.getYear());
        monthYearLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        JFXButton prevMonthButton = new JFXButton("Previous Month");

        prevMonthButton.setStyle(" -fx-text-fill: white; -fx-background-color:  #114232;");
        prevMonthButton.setMaxHeight(50);
        prevMonthButton.setPrefHeight(50);
        prevMonthButton.setOnAction(e -> {
            currentDate = currentDate.minusMonths(1);
            updateMonthLabel(monthYearLabel);
            updateCalendarGrid(calendarGrid);
        });

        JFXButton nextMonthButton = new JFXButton("Next Month");
        nextMonthButton.setStyle(" -fx-text-fill: white; -fx-background-color:  #114232;");
        nextMonthButton.setMaxHeight(50);
        nextMonthButton.setPrefHeight(50);
        nextMonthButton.setOnAction(e -> {
            currentDate = currentDate.plusMonths(1);
            updateMonthLabel(monthYearLabel);
            updateCalendarGrid(calendarGrid);
        });

        HBox buttonBox = new HBox(prevMonthButton, monthYearLabel, nextMonthButton);
        buttonBox.setSpacing(225);

        borderPane.setCenter(new HBox(new VBox(buttonBox, calendarGrid)));
        // Scene scene = new Scene(new HBox(new VBox(buttonBox, calendarGrid)), 800,
        // 600);
        // primaryStage.setTitle("Full Size Calendar View");
        // primaryStage.setScene(scene);
        // primaryStage.show();

    }

    @FXML
    void onMyDayHandler(ActionEvent event) throws IOException {

        borderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("MyDayView.fxml")));

    }

    @FXML
    void onNextHandler(ActionEvent event) {

    }

    @FXML
    void onTasksHandler(ActionEvent event) throws IOException {

        borderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("AllTasks.fxml")));

    }

    private void updateCalendarGrid(GridPane calendarGrid) {
        calendarGrid.getChildren().clear();

        int daysInMonth = currentDate.lengthOfMonth();
        int currentDayOfMonth = currentDate.getDayOfMonth();

        int dayOfWeek = currentDate.withDayOfMonth(1).getDayOfWeek().getValue() % 7; // Start with Monday
        int row = 1; // Start with the second row

        // Add day labels at the top of the calendar grid
        String[] dayNames = { "             Mon ", "            Tue", "            Wed", "            Thu",
                "            Fri", "           Sat", "           Sun" };
        for (int i = 0; i < dayNames.length; i++) {
            Label dayLabel1 = new Label(dayNames[i]);
            dayLabel1.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
            dayLabel1.setMaxWidth(110);
            dayLabel1.setPrefWidth(110);
            dayLabel1.setStyle("-fx-text-fill: black;");
            calendarGrid.add(dayLabel1, i, 0);
        }
        JFXButton[] dayButton = new JFXButton[32];
        for (int i = 1; i <= daysInMonth; i++) {
            dayButton[i] = new JFXButton(i + "");
            dayButton[i].setMaxWidth(110);
            dayButton[i].setPrefWidth(110);
            dayButton[i].setMaxHeight(100);
            dayButton[i].setPrefHeight(100);
            dayButton[i].setStyle("-fx-font-size: 30;");
            dayButton[i].setStyle("-fx-text-fill: white;");
            dayButton[i].setStyle("-fx-border-color: white; -fx-border-width: 1px;");
            if (i == currentDayOfMonth) {
                dayButton[i].setStyle(" -fx-text-fill: white; -fx-background-color:  #114232;");

                // Change background color of current day
            }

            int day = i; // Capture the day value for the lambda expression
            dayButton[i].setOnAction(event -> {
                try {
                    TasksOnDayViewController.setDay(day);
                    System.out.println(month);

                    borderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("TasksonDayView.fxml")));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            calendarGrid.add(dayButton[i], dayOfWeek, row);

            dayOfWeek = (dayOfWeek + 1) % 7;
            if (dayOfWeek == 0) {
                row++; // Move to the next row after completing a row of 7 days
            }
        }

    }

    public void initialize() throws IOException {

        userBTN.setText(LoginController.getUsername());
        borderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("MyDayView.fxml")));
            

    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void onUserHandler(ActionEvent event) throws IOException {
        borderPane.setCenter((Parent) FXMLLoader.load(getClass().getResource("ProfileView.fxml")));

    }

    private void updateMonthLabel(Label monthYearLabel) {
        monthYearLabel.setText(currentDate.getMonth().toString() + " " + currentDate.getYear());
    }

}
