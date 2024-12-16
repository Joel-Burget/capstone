package com.burgetjoel;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class IntroductionAppointment extends Appointment{

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

    public  IntroductionAppointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end,
                       String createdBy, String customerID, int userID, int contactID){
        super(appointmentID, title, description, location, type, location, type, createdBy, customerID, userID, contactID);
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.createdBy = createdBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        setType("introduction");
    }
        public static void createAppointment(IntroductionAppointment introductionAppointment){
            
            String query = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By," +
                "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES ('" + introductionAppointment.getAppointmentID() + "', '" +
                introductionAppointment.getTitle() + "', '" + introductionAppointment.getDescription() + "', '" + introductionAppointment.getLocation() + "', '" + introductionAppointment.getType() + "', '" +
                introductionAppointment.getStart() + "', '" + introductionAppointment.getEnd()  + "', '" + LocalDateTime.now() + "', '" + LoginController.getUsername() +
                "', '" + LocalDateTime.now() + "', '" + LoginController.getUsername() + "', '" + introductionAppointment.getCustomerID() + "', '" +
                introductionAppointment.getUserID() + "', '" + introductionAppointment.getContactID() + "');";

        try{
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            JDBC.getPreparedStatement().executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
        public void setType(String type) {
            super.setType(type);
        }
}
