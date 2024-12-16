package com.burgetjoel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class AddCustomerController{
    public Label warningLabel;
    private MainController MC;
    public TextField customerIDField;
    public TextField nameField;
    public TextField addressField;
    public ComboBox countryBox;
    public ComboBox stateBox;
    public Button addButton;
    public Button cancelButton;
    public TextField zipField;
    public TextField phoneField;
    String state;
    ObservableList<String> countries = FXCollections.observableArrayList();
    ObservableList<String> states = FXCollections.observableArrayList();
    /**
     * Generates form for adding a customer, builds lists of countries and states in comboboxes
     */
    public void initialize() {
        warningLabel.setVisible(false);
        //Adding Countries to combo box
        countries.add("Canada");
        countries.add("United Kingdom");
        countries.add("United States");
        countryBox.setItems(countries);

        /**
         * lamda used to build on action to update state dropdown based on country selection
         */
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
    }
    /**
     * Validates user input, if valid, updates both database and customer tables in MainController
     */
    public void addButtonAction(){
        int ID = Customers.generateCustomerID();
        String name = nameField.getText();
        String address = addressField.getText();
        String zip = zipField.getText();
        String phone = phoneField.getText();
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
                    System.out.println(rs.getInt(1));
                    divisionID = rs.getInt(1);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }

            Customers tempCustomer = new Customers(ID, name, address, zip, phone, divisionID);

            Customers.createCustomer(tempCustomer);
            Customers.getAllCustomers().add(tempCustomer);
            addButton.getScene().getWindow().hide();
            MC.refreshCustomerTable();
        }
    }

    public void setMainController(MainController MC){
        this.MC = MC;
    }

    public void cancelButtonAction(){
        cancelButton.getScene().getWindow().hide();
    }
}
