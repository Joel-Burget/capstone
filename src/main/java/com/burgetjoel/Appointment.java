package com.burgetjoel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private String startString;
    private String endString;
    private String createdBy;
    private String customerID;
    private int userID;
    private int contactID;

    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end,
                       String createdBy, String customerID, int userID, int contactID){
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createdBy = createdBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    public Appointment(int appointmentID, String title, String description, String location, String type, String startString, String endString,
                       String createdBy, String customerID, int userID, int contactID){
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startString = startString;
        this.endString = endString;
        this.createdBy = createdBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");
        LocalDateTime start = LocalDateTime.parse(startString, dtf);
        LocalDateTime end = LocalDateTime.parse(endString, dtf);

        this.setStart(start);
        this.setEnd(end);
    }
    /**
     * returns appointments from database filtered to current week
     */
    public static ObservableList<Appointment> getAllWeekAppointments(){
        ObservableList<Appointment> allAppointmentsByWeek = FXCollections.observableArrayList();
        int gotUserID = 0;
        try{
            String query = "SELECT * FROM users WHERE User_Name = '" + LoginController.getUsername() + "';";
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                gotUserID = rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        String query = "SELECT * FROM appointments WHERE User_ID = '" +  gotUserID + "' AND YEAR(Start) = YEAR(CURDATE()) AND " +
                "WEEK(Start) = WEEK(CURDATE());";
        try{
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                int tempId = rs.getInt(1);
                String tempTitle = rs.getString(2);
                String tempDescription = rs.getString(3);
                String tempLoc = rs.getString(4);
                String tempType = rs.getString(5);
                Timestamp start = rs.getTimestamp(6);
                Timestamp end = rs.getTimestamp(7);
                String tempCreatedBy = rs.getString(9);
                String tempCustomerID = rs.getString(12);
                int tempUserID = rs.getInt(13);
                int tempContactID = rs.getInt(14);

                LocalDateTime tempEnd = end.toLocalDateTime();
                LocalDateTime tempStart = start.toLocalDateTime();

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");

                Appointment tempAppointment = new Appointment(tempId, tempTitle, tempDescription,tempLoc, tempType, dtf.format(tempStart), dtf.format(tempEnd), tempCreatedBy, tempCustomerID,
                        tempUserID, tempContactID);
                allAppointmentsByWeek.add(tempAppointment);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return allAppointmentsByWeek;
    }
    /**
     * returns appointments from database filtered by current month
     */
    public static ObservableList<Appointment> getAllMonthAppointments(){
        ObservableList<Appointment> allAppointmentsByMonth = FXCollections.observableArrayList();
        int gotUserID = 0;
        try{
            String query = "SELECT * FROM users WHERE User_Name = '" + LoginController.getUsername() + "';";
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                gotUserID = rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        String query = "SELECT * FROM appointments WHERE User_ID = '" +  gotUserID + "' AND YEAR(Start) = YEAR(CURDATE()) AND " +
                "MONTH(Start) = MONTH(CURDATE());";
        try{
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                int tempId = rs.getInt(1);
                String tempTitle = rs.getString(2);
                String tempDescription = rs.getString(3);
                String tempLoc = rs.getString(4);
                String tempType = rs.getString(5);
                Timestamp start = rs.getTimestamp(6);
                Timestamp end = rs.getTimestamp(7);
                String tempCreatedBy = rs.getString(9);
                String tempCustomerID = rs.getString(12);
                int tempUserID = rs.getInt(13);
                int tempContactID = rs.getInt(14);

                LocalDateTime tempEnd = end.toLocalDateTime();
                LocalDateTime tempStart = start.toLocalDateTime();

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");

                Appointment tempAppointment = new Appointment(tempId, tempTitle, tempDescription,tempLoc, tempType, dtf.format(tempStart), dtf.format(tempEnd), tempCreatedBy, tempCustomerID,
                        tempUserID, tempContactID);
                allAppointmentsByMonth.add(tempAppointment);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return allAppointmentsByMonth;
    }

    /**
     * returns all appointments from database
     */
    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        String query = "SELECT * FROM appointments ORDER BY start ASC;";

        try{
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                int tempId = rs.getInt(1);
                String tempTitle = rs.getString(2);
                String tempDescription = rs.getString(3);
                String tempLoc = rs.getString(4);
                String tempType = rs.getString(5);
                Timestamp start = rs.getTimestamp(6);
                Timestamp end = rs.getTimestamp(7);
                String tempCreatedBy = rs.getString(9);
                String tempCustomerID = rs.getString(12);
                int tempUserID = rs.getInt(13);
                int tempContactID = rs.getInt(14);

                LocalDateTime tempEnd = end.toLocalDateTime();
                LocalDateTime tempStart = start.toLocalDateTime();

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");

                Appointment tempAppointment = new Appointment(tempId, tempTitle, tempDescription,tempLoc, tempType, dtf.format(tempStart), dtf.format(tempEnd), tempCreatedBy, tempCustomerID,
                        tempUserID, tempContactID);

                allAppointments.add(tempAppointment);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return allAppointments;
    }
    /**
     * creates appointment and adds to database, updates appointment tables in MainController
     */
    public static void createAppointment(Appointment appointment){
        String query = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By," +
                "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES ('" + appointment.getAppointmentID() + "', '" +
                appointment.getTitle() + "', '" + appointment.getDescription() + "', '" + appointment.getLocation() + "', '" + appointment.getType() + "', '" +
                appointment.getStart() + "', '" + appointment.getEnd()  + "', '" + LocalDateTime.now() + "', '" + LoginController.getUsername() +
                "', '" + LocalDateTime.now() + "', '" + LoginController.getUsername() + "', '" + appointment.getCustomerID() + "', '" +
                appointment.getUserID() + "', '" + appointment.getContactID() + "');";

        try{
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            JDBC.getPreparedStatement().executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * generates appointmentID field based on current count of appointments in database
     */
    public static int generateAppointmentID(){
        int appointmentID;
        try{
            String query = "SELECT MAX(Appointment_ID) FROM appointments;";
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                appointmentID = rs.getInt(1) +1;
                return appointmentID;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * Getters/setters for fields
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public String getStartString() {
        return startString;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getEndString() {
        return endString;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}
