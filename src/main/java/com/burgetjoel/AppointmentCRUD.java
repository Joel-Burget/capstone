package com.burgetjoel;

import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;

public class AppointmentCRUD {

    public static ResultSet getAppointments(int Appointment_ID) {
        try {
            String statement = "SELECT * FROM appointments WHERE Appointment_ID='" + Appointment_ID + "'";
            JDBC.makePreparedStatement(statement, JDBC.getConnection());

            return JDBC.getPreparedStatement().executeQuery(statement);
        } catch (Exception e) {
            return null;
        }
    }

    public static void createAppointment(String title, String desc, String location, String type, LocalDateTime start,
                                         LocalDateTime end, LocalDateTime createdDate, Timestamp lastUpdate, int customerID, int userID, int contactID){
        String statement = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Last_Update, Customer_ID, User_ID," +
                "Contact_ID)" +
                " VALUES (" + title + "," +  desc + "," + location + "," + type + ",'" + start + "','" +
                end + "','" + createdDate + "','" + lastUpdate + "'," + customerID + "," + userID + "," + contactID + ");";

        System.out.println(statement);
        try {
            JDBC.makePreparedStatement(statement, JDBC.getConnection());
            JDBC.getPreparedStatement().executeUpdate(statement);
            System.out.println("Data Entered");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void deleteAppointment(int appointmentID){
        String statement = "DELETE FROM appointments WHERE Appointment_ID=" + appointmentID;

        try{
            JDBC.makePreparedStatement(statement, JDBC.getConnection());
            JDBC.getPreparedStatement().executeUpdate(statement);
            System.out.println("Appointment Deleted");
        }catch (Exception e){
            e.printStackTrace();
        }
     }

     public static void updateAppointment(int appointmentID, String title, String desc, String location, String type, LocalDateTime start,
                                          LocalDateTime end) {
         String statement = "UPDATE appointments SET Title = '" + title + "', Description = '" + desc + "', Location = '" + location
                 + "', Type = '" + type + "', Start = '" + start + "', End = '" + end + "' WHERE Appointment_ID = '" + appointmentID + "'";

         try {
             JDBC.makePreparedStatement(statement, JDBC.getConnection());
             JDBC.getPreparedStatement().executeUpdate();
             System.out.print("Appointment Updated");
         } catch (Exception e) {
             e.printStackTrace();
         }
     }


}
