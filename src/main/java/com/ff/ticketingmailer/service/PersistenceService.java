package com.ff.ticketingmailer.service;

import com.ff.ticketingmailer.model.MaintenanceRemainder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersistenceService {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:./test";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

//    public List<MaintenanceRemainder> loadAll(){
//
//    }

    public ResultSet selectById(Connection connection, String id) throws SQLException {
        String fromDisk = "SELECT * FROM REMAINDER WHERE remainder_id=" + id;
        PreparedStatement selectPreparedStatement = connection.prepareStatement(fromDisk);
        return selectPreparedStatement.executeQuery();
    }

    public List<MaintenanceRemainder> selectAll(Connection connection) throws SQLException {
        String fromDisk = "SELECT * FROM REMAINDER";
        PreparedStatement selectPreparedStatement = connection.prepareStatement(fromDisk);
        ResultSet rs = selectPreparedStatement.executeQuery();
        List<MaintenanceRemainder> maintenanceRemainder = new ArrayList<>();
        while (rs.next()) {
            MaintenanceRemainder remainder = new MaintenanceRemainder();
            remainder.setId(rs.getString(1));
            remainder.setMarca(rs.getString(2));
            remainder.setModello(rs.getString(3));
            remainder.setManutenzione(rs.getString(4));
            remainder.setDataInstallazione(rs.getDate(5));
            remainder.setDataEmail(rs.getDate(6));
            remainder.setDataManutenzione(rs.getDate(7));
            // System.out.println("");
            maintenanceRemainder.add(remainder);
        }
        return maintenanceRemainder;
    }

//    public void printResults(Connection connection) throws SQLException {
//        ResultSet rs = selectAll(connection);
//        while (rs.next()) {
//            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//                System.out.print(rs.getMetaData().getColumnLabel(i) + ":" + rs.getObject(i) + "\t");
//            }
//            System.out.println("");
//        }
//
//
//        connection.commit();
//    }

    public void createRemainder(Connection connection) throws SQLException {
        String CreateQuery = "CREATE TABLE REMAINDER(remainder_id varchar2(100) primary key, marca varchar2(128) not null, modello  varchar2(128) not null, manutenzione  varchar2(128) not null, data_installazione date not null, data_email date, data_manutenzione date)";
        PreparedStatement createPreparedStatement = connection.prepareStatement(CreateQuery);
        createPreparedStatement.executeUpdate();
        createPreparedStatement.close();
    }

    public void insertRemainder(Connection connection, MaintenanceRemainder maintenanceRemainder) throws SQLException {
        String InsertQuery = "INSERT INTO REMAINDER" + "(remainder_id, marca,modello,manutenzione,data_installazione) values" + "(?,?,?,?,?)";
        PreparedStatement insertPreparedStatement = connection.prepareStatement(InsertQuery);
        insertPreparedStatement.setString(1, maintenanceRemainder.getId());
        insertPreparedStatement.setString(2, maintenanceRemainder.getMarca());
        insertPreparedStatement.setString(3, maintenanceRemainder.getModello());
        insertPreparedStatement.setString(4, maintenanceRemainder.getManutenzione());
        insertPreparedStatement.setDate(5, new Date(maintenanceRemainder.getDataInstallazione().getTime()));
        insertPreparedStatement.executeUpdate();
        insertPreparedStatement.close();
    }

    public void insertEmailDate(Connection connection, String id) throws SQLException {
        String InsertQuery = "UPDATE REMAINDER SET data_email=? where remainder_id=?";
        PreparedStatement insertPreparedStatement = connection.prepareStatement(InsertQuery);
        insertPreparedStatement.setDate(1, new Date(new java.util.Date().getTime()));
        insertPreparedStatement.setString(2, id);
        insertPreparedStatement.executeUpdate();
        insertPreparedStatement.close();
    }

    public void insertManutenzioneDate(Connection connection, String id) throws SQLException {
        String InsertQuery = "UPDATE REMAINDER SET data_manutenzione=? where remainder_id=?";
        PreparedStatement insertPreparedStatement = connection.prepareStatement(InsertQuery);
        insertPreparedStatement.setDate(1, new Date(new java.util.Date().getTime()));
        insertPreparedStatement.setString(2, id);
        insertPreparedStatement.executeUpdate();
        insertPreparedStatement.close();
    }

    public Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }
//
//
//
//    public static void main(String[] args) throws SQLException, FileNotFoundException {
//        FileInputStream fis = new FileInputStream("C:\\Users\\Francesco\\Downloads\\Ticketing.xlsx");
//        ExcelParseService excelParseService = new ExcelParseService();
//        List<MaintenanceRemainder> remainders = excelParseService.parse(fis);
//
//        PersistenceService persistenceService = new PersistenceService();
//        Connection connection = persistenceService.getDBConnection();
//        try {
//            persistenceService.createRemainder(connection);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        for(MaintenanceRemainder maintenanceRemainder:remainders){
//            try {
//                persistenceService.insertRemainder(connection, maintenanceRemainder);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        connection.commit();
//        persistenceService.printResults(connection);
//    }


}
