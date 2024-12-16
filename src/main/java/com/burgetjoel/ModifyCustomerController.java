package com.burgetjoel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ModifyCustomerController {
    public Label warningLabel;
    private MainController MC;
    public TextField nameField;
    public TextField addressField;
    public TextField zipField;
    public ComboBox<String> countryBox;
    public ComboBox<String> stateBox;
    public Button modifyButton;
    public Button cancelButton;
    public TextField phoneField;
    public TextField customerIDField;
    String state;
    ObservableList<String> countries = FXCollections.observableArrayList();
    ObservableList<String> states = FXCollections.observableArrayList();
    /**
     * populates from with data from selected user
     */
    public void initialize(){
        warningLabel.setVisible(false);
        customerIDField.setText(Integer.toString(MainController.getSelectedCustomer().getCustomerId()));
        nameField.setText(MainController.getSelectedCustomer().getCustomerName());
        addressField.setText(MainController.getSelectedCustomer().getAddress());
        zipField.setText(MainController.getSelectedCustomer().getCustomerZip());
        phoneField.setText(MainController.getSelectedCustomer().getCustomerPhone());

        //Adding Countries to combo box
        countries.add("Canada");
        countries.add("United Kingdom");
        countries.add("United States");
        countryBox.setItems(countries);

        //generate state combo box based on country selected
        countryBox.setOnAction((e) -> {
            stateBox.getItems().clear();
            if(countryBox.getSelectionModel().getSelectedItem().equals("Canada")){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/burgetjoel/canadianProvinces.txt"));
                    while((state = reader.readLine()) != null){
                        states.add(state);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                stateBox.setItems(states);
            }

            if(countryBox.getSelectionModel().getSelectedItem().equals("United Kingdom")){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/burgetjoel/britishIsles.txt"));
                    while((state = reader.readLine()) != null){
                        states.add(state);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                stateBox.setItems(states);
            }

            if(countryBox.getSelectionModel().getSelectedItem().equals("United States")){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/burgetjoel/usStates.txt"));
                    while((state = reader.readLine()) != null){
                        states.add(state);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                stateBox.setItems(states);
            }
        });

        try{
            String query = "SELECT * FROM first_level_divisions WHERE Division_ID='" + MainController.getSelectedCustomer().getDivisionId()
                    + "';";
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                if(rs.getInt(7) == 1){
                    countryBox.getSelectionModel().select(2);
                    stateBox.getSelectionModel().select(rs.getString(2));
                }
                if(rs.getInt(7) == 2){
                    countryBox.getSelectionModel().select(1);
                    stateBox.getSelectionModel().select(rs.getString(2));
                }
                if(rs.getInt(7) == 3){
                    countryBox.getSelectionModel().select(0);
                    stateBox.getSelectionModel().select(rs.getString(2));
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * validates user input and calls updateCustomer from Customer class
     */
    public void modifyButtonAction(ActionEvent actionEvent) {
        int divisionID = 0;


        if(nameField.getText().equals("")){
            nameField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            warningLabel.setText("Please complete all fields");
            warningLabel.setVisible(true);

        }else if(addressField.getText().equals("")){
            addressField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            warningLabel.setText("Please complete all fields");
            warningLabel.setVisible(true);
        }else if(zipField.getText().equals("")){
            zipField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            warningLabel.setText("Please complete all fields");
        }else if(phoneField.getText().equals("")){
            phoneField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            warningLabel.setText("Please complete all fields");
        }else if(countryBox.getSelectionModel().getSelectedItem() == null){
            countryBox.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            warningLabel.setText("Please complete all fields");
        }else if(stateBox.getSelectionModel().getSelectedItem() == null){
            stateBox.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            warningLabel.setText("Please complete all fields");
        } else {
            try {
                String query = "SELECT Division_ID FROM first_level_divisions WHERE Division = '"
                        + stateBox.getSelectionModel().getSelectedItem() + "';";
                JDBC.makePreparedStatement(query, JDBC.getConnection());
                ResultSet rs = JDBC.getPreparedStatement().executeQuery();

                while(rs.next()){
                    divisionID = rs.getInt(1);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }

            try {
                String query = "UPDATE customers SET Customer_Name = '" + nameField.getText() + "' , Address = '" + addressField.getText()
                        + "', Postal_Code = '" + zipField.getText() + "', Phone = '" + phoneField.getText() + "', Last_Update='" + LocalDateTime.now() + "', Last_Updated_By='" +
                        LoginController.getUsername() + "', Division_ID='" + divisionID + "' WHERE Customer_ID='" + customerIDField.getText() + "';";
                JDBC.makePreparedStatement(query, JDBC.getConnection());
                JDBC.getPreparedStatement().executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
            }
            modifyButton.getScene().getWindow().hide();
            MC.refreshCustomerTable();
        }

    }
    /**
     * closes form
     */
    public void cancelButtonAction(ActionEvent actionEvent) {
        cancelButton.getScene().getWindow().hide();
    }

    public void setMainController(MainController MC){
        this.MC = MC;
    }
}
