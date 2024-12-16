package com.burgetjoel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;


public class CreateAppointmentController {
    public Label timeWarningLabel;
    public ComboBox customerBox;
    private MainController MC;

    public TextField appointmentIDField;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public ChoiceBox<String> typeBox;
    public DatePicker startPicker;
    public DatePicker endPicker;
    public ComboBox<Integer> startHour;
    public ComboBox<Integer> endHour;
    public ComboBox<Integer> startMinute;
    public ComboBox<Integer> endMinute;
    public ComboBox contactBox;
    public Button saveButton;
    public Button cancelButton;
    /**
     * creates and sets comboboxes and fields for user input
     */
    public void initialize(){
        timeWarningLabel.setVisible(false);
        ObservableList<Integer> hours = FXCollections.observableArrayList();
        hours.addAll(8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22);
        ObservableList<Integer> minutes = FXCollections.observableArrayList();
        minutes.addAll(00, 30);
        appointmentIDField.setText(Integer.toString(Appointment.generateAppointmentID()));

        startHour.setItems(hours);
        startMinute.setItems(minutes);
        endHour.setItems(hours);
        endMinute.setItems(minutes);
        /**
         * lamda used to prevent user from selecting invalid dates
         */
        startPicker.setDayCellFactory(picker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        endPicker.setDayCellFactory(picker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        String query = "SELECT * FROM contacts";
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        try{
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                contactNames.add(rs.getString(2));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        contactBox.setItems(contactNames);

        ObservableList<String> customerNames = FXCollections.observableArrayList();
        for (Customers customer: Customers.getAllCustomers()) {
            customerNames.add(customer.getCustomerName());
        }
        customerBox.setItems(customerNames);

        ObservableList<String> appointmentTypes = FXCollections.observableArrayList();
        appointmentTypes.add("Standard");
        appointmentTypes.add("Introduction");
        typeBox.setItems(appointmentTypes);
    }

    public void setMainController(MainController MC) {
        this.MC = MC;
    }
    /**
     * calls createAppointment() from Appointment class
     */
    public void saveButtonAction(){
        if(verifySave()){
            String query = "SELECT * FROM contacts WHERE Contact_Name= '" + contactBox.getSelectionModel().getSelectedItem() + "';";
            int contactID = 0;
            try{
                JDBC.makePreparedStatement(query, JDBC.getConnection());
                ResultSet rs = JDBC.getPreparedStatement().executeQuery();
                while(rs.next()){
                    contactID = rs.getInt(1);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }

            int userID = 0;
            query = "SELECT * FROM users WHERE User_Name='" + LoginController.getUsername() + "';";
            try{
                JDBC.makePreparedStatement(query, JDBC.getConnection());
                ResultSet rs = JDBC.getPreparedStatement().executeQuery();

                while(rs.next()){
                    userID = rs.getInt(1);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }

            int id = Integer.parseInt(appointmentIDField.getText());
            String title = titleField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            String type = typeBox.getSelectionModel().getSelectedItem();
            String customerID = Integer.toString(Customers.getAllCustomers().get(customerBox.getSelectionModel().getSelectedIndex()).getCustomerId());
            LocalDateTime startDate = LocalDateTime.of(startPicker.getValue(), LocalTime.of(startHour.getValue(), startMinute.getValue()));
            LocalDateTime endDate = LocalDateTime.of(endPicker.getValue(), LocalTime.of(endHour.getValue(), endMinute.getValue()));

            // Appointment tempAppointment = new Appointment(id, title, description, location, type, convertToUtc(startDate), convertToUtc(endDate),
            //         LoginController.getUsername(), customerID, userID, contactID);

            // Appointment.createAppointment(tempAppointment);
            
            if(type == "Standard"){
                Appointment tempAppointment = new Appointment(id, title, description, location, type, convertToUtc(startDate), convertToUtc(endDate),
                    LoginController.getUsername(), customerID, userID, contactID);

                Appointment.createAppointment(tempAppointment);
                
            }
            if(type == "Introduction"){
                Appointment tempAppointment = new Appointment(id, title, description, location, type, convertToUtc(startDate), convertToUtc(endDate),
                LoginController.getUsername(), customerID, userID, contactID);

                IntroductionAppointment.createAppointment(tempAppointment);
            }
            
            MC.refreshAppointmentTables();
            saveButton.getScene().getWindow().hide();
        }
    }
    /**
     * closes form
     */
    public void cancelButtonAction(){
        cancelButton.getScene().getWindow().hide();
    }
    /**
     * converts user inputs to UTC timezone
     */
    public LocalDateTime convertToUtc(LocalDateTime time){
        return time.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
    /**
     * validates user inputs and highlights missing data or incomplete fields
     */
    public boolean verifySave(){
        if(titleField.getText().isEmpty()){
            titleField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(descriptionField.getText().isEmpty()){
            descriptionField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(locationField.getText().isEmpty()){
            locationField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(typeBox.getSelectionModel().getSelectedItem().isEmpty()){
            typeBox.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(contactBox.getSelectionModel().isEmpty()){
            contactBox.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(startPicker.getValue() == null){
            startPicker.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(endPicker.getValue() == null){
            endPicker.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(Appointment.getAllAppointments().isEmpty()){
            //if returns nothing, skip, no appointment
            return true;
        }else{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");
            LocalDateTime startDate = LocalDateTime.of(startPicker.getValue(), LocalTime.of(startHour.getValue(), startMinute.getValue()));
            LocalDateTime endDate = LocalDateTime.of(endPicker.getValue(), LocalTime.of(endHour.getValue(), endMinute.getValue()));
            if(isAvailable(startDate, endDate)){
                return true;
            }else{
                startHour.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
                startMinute.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
                endHour.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
                endMinute.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
                return false;
            }
           // testCases();
            }
    }
    /**
     * logic to check if appointments overlap
     */
    public static boolean isAvailable(LocalDateTime startTime, LocalDateTime endTime){
        for (Appointment appointment: Appointment.getAllAppointments()) {
            if(startTime.isBefore(appointment.getEnd()) && endTime.isAfter(appointment.getStart())){
                return false;
            }
        }
        return true;
    }
    public static void testCases(){
        // Test cases
        LocalDateTime newStartTime1 = LocalDateTime.of(2023, 8, 16, 9, 0);
        LocalDateTime newEndTime1 = LocalDateTime.of(2023, 8, 16, 10, 0);
        System.out.println("New appointment [9-10] is available: " + isAvailable(newStartTime1, newEndTime1));

        LocalDateTime newStartTime2 = LocalDateTime.of(2023, 8, 16, 11, 0);
        LocalDateTime newEndTime2 = LocalDateTime.of(2023, 8, 16, 14, 0);
        System.out.println("New appointment [11-2] is available: " + isAvailable(newStartTime2, newEndTime2));

        LocalDateTime newStartTime3 = LocalDateTime.of(2023, 8, 16, 9, 0);
        LocalDateTime newEndTime3 = LocalDateTime.of(2023, 8, 16, 11, 0);
        System.out.println("New appointment [9-11] is available: " + isAvailable(newStartTime3, newEndTime3));

        // More test cases
        LocalDateTime newStartTime4 = LocalDateTime.of(2023, 8, 16, 7, 0);
        LocalDateTime newEndTime4 = LocalDateTime.of(2023, 8, 16, 8, 30);
        System.out.println("New appointment [7-8:30] is available: " + isAvailable(newStartTime4, newEndTime4));

        LocalDateTime newStartTime5 = LocalDateTime.of(2023, 8, 16, 9, 30);
        LocalDateTime newEndTime5 = LocalDateTime.of(2023, 8, 16, 10, 30);
        System.out.println("New appointment [9:30-10:30] is available: " + isAvailable(newStartTime5, newEndTime5));

        LocalDateTime newStartTime6 = LocalDateTime.of(2023, 8, 16, 11, 30);
        LocalDateTime newEndTime6 = LocalDateTime.of(2023, 8, 16, 12, 30);
        System.out.println("New appointment [11:30-12:30] is available: " + isAvailable(newStartTime6, newEndTime6));
        // Additional test cases
        LocalDateTime newStartTime7 = LocalDateTime.of(2023, 8, 16, 8, 30);
        LocalDateTime newEndTime7 = LocalDateTime.of(2023, 8, 16, 10, 0);
        System.out.println("New appointment [8:30-10] is available: " + isAvailable(newStartTime7, newEndTime7));

        LocalDateTime newStartTime8 = LocalDateTime.of(2023, 8, 16, 9, 0);
        LocalDateTime newEndTime8 = LocalDateTime.of(2023, 8, 16, 11, 30);
        System.out.println("New appointment [9-11:30] is available: " + isAvailable(newStartTime8, newEndTime8));

        LocalDateTime newStartTime9 = LocalDateTime.of(2023, 8, 16, 12, 15);
        LocalDateTime newEndTime9 = LocalDateTime.of(2023, 8, 16, 12, 45);
        System.out.println("New appointment [12:15-12:45] is available: " + isAvailable(newStartTime9, newEndTime9));

        LocalDateTime newStartTime10 = LocalDateTime.of(2023, 8, 16, 12, 30);
        LocalDateTime newEndTime10 = LocalDateTime.of(2023, 8, 16, 13, 0);
        System.out.println("New appointment [12:30-1:00] is available: " + isAvailable(newStartTime10, newEndTime10));
        LocalDateTime newStartTime11 = LocalDateTime.of(2023, 8, 16, 13, 0);
        LocalDateTime newEndTime11 = LocalDateTime.of(2023, 8, 16, 14, 0);
        System.out.println("New appointment [1:00-2:00] is available: " + isAvailable(newStartTime11, newEndTime11));
    }
}
