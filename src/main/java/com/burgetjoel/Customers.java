package com.burgetjoel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Customers {
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    private int customerId;
    private String customerName;
    private String address;
    private String customerZip;
    private String customerPhone;
    private int divisionId;

    public Customers(int customerId, String customerName, String address, String customerZip, String customerPhone, int divisionId){
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.customerZip = customerZip;
        this.customerPhone = customerPhone;
        this.divisionId = divisionId;
    }
    /**
     * returns list of all customers
     */
    public static ObservableList<Customers> getAllCustomers(){
        ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
        String statement = "SELECT * FROM Customers";
        int custID;
        String custName;
        String custAdd;
        String custZip;
        String custPhone;
        int divId;

        try{
            JDBC.makePreparedStatement(statement, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();
            while(rs.next()){
                custID = rs.getInt(1);
                custName = rs.getString(2);
                custAdd = rs.getString(3);
                custZip = rs.getString(4);
                custPhone = rs.getString(5);
                divId = rs.getInt(10);

                Customers temp = new Customers(custID, custName, custAdd, custZip, custPhone, divId);
                allCustomers.add(temp);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return allCustomers;
    }

    /**
     * creates customer in database
     */
    public static void createCustomer(Customers customer){
        String statement = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Last_Update, Division_ID, Created_By, Last_Updated_By) VALUES('"
                + customer.getCustomerId() + "', '" + customer.getCustomerName() + "', '" + customer.getAddress() + "', '" + customer.getCustomerZip()
                + "', '" + customer.getCustomerPhone() + "', '" + LocalDateTime.now() + "', '" + LocalDateTime.now() + "', '" + customer.getDivisionId() + "', '"
                +  LoginController.getUsername() + "', '" + LoginController.getUsername() + "');";
        System.out.println(statement);

        try{
            JDBC.makePreparedStatement(statement, JDBC.getConnection());
            JDBC.getPreparedStatement().executeUpdate();
            System.out.println("Customer created successfully.");
            getAllCustomers().add(customer);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * updates customers data in database
     */
    public static void updateCustomer(Customers customer){
        String statement = "UPDATE customers SET Customer_Name = '" + customer.getCustomerName() + "', Address =" + "'" + customer.getAddress()
                + "', " + "Postal_Code = '" + customer.getCustomerZip() + "', "
                + "Phone ='" + customer.getCustomerPhone() + "', " + "Last_Update ='" + LocalDateTime.now() + "', " + "Last_Updated_By ='"
                + LoginController.getUsername() + "' " + "WHERE Customer_ID = '" + customer.getCustomerId() + "';";
        System.out.println(statement);

        try{
            JDBC.makePreparedStatement(statement, JDBC.getConnection());
            JDBC.getPreparedStatement().executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    /**
     * deletes selected customer from database
     */
    public static void deleteCustomer(int customerID){
        int count;
        String statement = "SELECT COUNT(Appointment_ID) FROM appointments WHERE Customer_ID='" + customerID + "'";
        try{
            JDBC.makePreparedStatement(statement, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();
            System.out.println(statement);
            while(rs.next()){
                count = rs.getInt(1);
                if(count > 0){
                    System.out.println("Please delete all appointments before deleting a customer profile.");
                } else {
                    statement = "DELETE FROM customers WHERE Customer_ID='" + customerID + "'";
                    JDBC.makePreparedStatement(statement, JDBC.getConnection());
                    JDBC.getPreparedStatement().executeUpdate();
                    System.out.println("Customer Deleted");
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * generates customerID based on max values in database
     */
    public static int generateCustomerID() {
        int customerID;
        try{
            String query = "SELECT MAX(Customer_ID) FROM customers;";
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                customerID = rs.getInt(1) +1;
                System.out.println(customerID);
                return customerID;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * getters/setters
     */
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerZip() {
        return customerZip;
    }

    public void setCustomerZip(String customerZip) {
        this.customerZip = customerZip;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
