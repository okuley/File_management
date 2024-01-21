/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author ntu-user
 */
public class DB {
    private String fileName = "jdbc:sqlite:comp20081.db";
    private int timeout = 30;
    private String dataBaseName = "COMP20081";
    private String dataBaseTableName = "Users";
    private String FileTableName="Files";
    Connection connection = null;
    private Random random = new SecureRandom();
    private String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private int iterations = 10000;
    private int keylength = 256;
    private String saltValue;
    
    /**
     * @brief constructor - generates the salt if it doesn't exists or load it from the file .salt
     */
    DB() {
        try {
            File fp = new File(".salt");
            if (!fp.exists()) {
                saltValue = this.getSaltvalue(30);
                FileWriter myWriter = new FileWriter(fp);
                myWriter.write(saltValue);
                myWriter.close();
            } else {
                Scanner myReader = new Scanner(fp);
                while (myReader.hasNextLine()) {
                    saltValue = myReader.nextLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    /**
     * @brief create a new table
     * @param tableName name of type String
     */
    public void createTable(String tableName) throws ClassNotFoundException {
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("create table if not exists " + tableName + "(id integer primary key autoincrement, name string, password string)");

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
    
    
     /**
     * @brief create a new table for files
     * @param tableName name of type String
     */
    public void createFileTable(String tableName) throws ClassNotFoundException {
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("create table if not exists " + tableName + "(id integer primary key autoincrement, owner string, file_name string,file_size integer,creation_date DATETIME DEFAULT CURRENT_TIMESTAMP,modification_date TIMESTAMP,file_path string)");

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
    

    /**
     * @brief delete table
     * @param tableName of type String
     */
    public void delTable(String tableName) throws ClassNotFoundException {
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("drop table if exists " + tableName);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * @brief add data to the database method
     * @param user name of type String
     * @param password of type String
     */
        public void addDataToDB(String user, String password) throws InvalidKeySpecException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
//            System.out.println("Adding User: " + user + ", Password: " + password);
            statement.executeUpdate("insert into " + dataBaseTableName + " (name, password) values('" + user + "','" + generateSecurePassword(password) + "')");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }
    }

        
        /**
     * @brief save file to the database method
     * @param user name of type String
     * @param file_name of type String
     * @param file_size of type int
     * @param file_path of type String
     */
        public void addDataToFileDB(String user, String file_name,int file_size,String file_path) throws InvalidKeySpecException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
//            System.out.println("Adding User: " + user + ", Password: " + password);
            statement.executeUpdate("insert into " + FileTableName + " (owner, file_name,file_size, file_path) values('" + user + "','" + file_name + "','" + file_size + "','" + file_path + "')");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }
    }

        
        
        
     /**
     * @brief delete user data from the database method
     * @param user name of type String
     */
        
       public void delDatafromDB(String user) throws InvalidKeySpecException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);          
            //System.out.println("delete from " + dataBaseTableName + " where name= "+user);
            statement.executeUpdate("delete from " + dataBaseTableName + " where name= "+ "\""+user+ "\"");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }
    } 
       
       /**
     * @brief delete file data from the database method
     * @param file_name name of type String
     */
        
       public void delFilefromDB(String file_name) throws InvalidKeySpecException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);          
            statement.executeUpdate("delete from " + FileTableName + " where file_name= "+ "\""+file_name+ "\"");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }
    } 
        
         /**
     * @param pass
     * @brief edits user data in the database method
     * @param user name of type String
     */ 
       public void editUserDataDB(String user, String pass) throws InvalidKeySpecException, ClassNotFoundException {
       
       try{
        
          Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);          
            //ResultSet rs= statement.executeQuery("select * from " + dataBaseTableName + " where name= "+ "\""+user+ "\"");
            statement.executeUpdate("update " + dataBaseTableName + " set password = " +"\""+generateSecurePassword(pass)+ "\""+ " where name= "+ "\""+user+ "\"");
         
       }
       
       catch(SQLException ex){
       Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
       }
       finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
       
       }
       

     /**
     * @brief file  data in the database method
     * @param File_name of type String
     */ 
       public void editFileDataDB(String File_name) throws InvalidKeySpecException, ClassNotFoundException {
       
       try{
        
          Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);          
            //ResultSet rs= statement.executeQuery("select * from " + dataBaseTableName + " where name= "+ "\""+user+ "\"");
            statement.executeUpdate("update " + FileTableName + " set modification_date = " +"\""+LocalDate.now()+ "\""+ " where file_name= "+ "\""+File_name+ "\"");
         
       }
       
       catch(SQLException ex){
       Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
       }
       finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
       
       }
       
       
        
        
        
    /**
     * @brief get data from the Database method
     * @retunr results as ResultSet
     */
    public ObservableList<User> getDataFromTable() throws ClassNotFoundException {
        ObservableList<User> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseTableName);
            while (rs.next()) {
                // read the result set
                result.add(new User(rs.getString("name"),rs.getString("password")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * @brief user Password get data from the Database method
     *  @param user
     * @return result 
     */
       
    
    public String getUserPassFromTable(String user) throws ClassNotFoundException {
        String result = "";
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select password from " + this.dataBaseTableName + " where name= "+ "\""+user+ "\"");
            if (rs.isBeforeFirst()) {
                 result=rs.getString("password");
                 System.out.println(result);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    
    /**
     * @brief   get user files data from the Database method
     *  @param user
     * @return result 
     */
       
    
    public ObservableList<Files>  getUserFilesFromTable(String user) throws ClassNotFoundException {
        ObservableList<Files> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.FileTableName + " where owner= "+ "\""+user+ "\"");
            while (rs.next()) {
                // read the result set
               result.add(new Files(rs.getString("owner"),rs.getString("file_name"),rs.getString("file_path"),rs.getInt("file_size"),rs.getTimestamp("creation_date")));
               //result.add(rs.getDate("creation_date"));
                //rs.getNString('owner')
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
    
    

    /**
     * @brief decode password method
     * @param user name as type String
     * @param pass plain password of type String
     * @return true if the credentials are valid, otherwise false
     */
    public boolean validateUser(String user, String pass) throws InvalidKeySpecException, ClassNotFoundException {
        Boolean flag = false;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select name, password from " + this.dataBaseTableName);
            String inPass = generateSecurePassword(pass);
            // Let's iterate through the java ResultSet
            while (rs.next()) {
                if (user.equals(rs.getString("name")) && rs.getString("password").equals(inPass)) {
                    flag = true;
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return flag;
    }

    private String getSaltvalue(int length) {
        StringBuilder finalval = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            finalval.append(characters.charAt(random.nextInt(characters.length())));
        }

        return new String(finalval);
    }

    /* Method to generate the hash value */
    private byte[] hash(char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keylength);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public String generateSecurePassword(String password) throws InvalidKeySpecException {
        String finalval = null;

        byte[] securePassword = hash(password.toCharArray(), saltValue.getBytes());

        finalval = Base64.getEncoder().encodeToString(securePassword);

        return finalval;
    }

    /**
     * @brief get table name
     * @return table name as String
     */
    public String getTableName() {
        return this.dataBaseTableName;
    }
    
    /**
     * @brief get Filestable name
     * @return table name as String
     */
    public String getFilesTableName() {
        return this.FileTableName;
    }

    /**
     * @brief print a message on screen method
     * @param message of type String
     */
    public void log(String message) {
        System.out.println(message);

    }

//    public static void main(String[] args) throws InvalidKeySpecException {
//        DB myObj = new DB();
//        myObj.log("-------- Simple Tutorial on how to make JDBC connection to SQLite DB ------------");
//        myObj.log("\n---------- Drop table ----------");
//        myObj.delTable(myObj.getTableName());
//        myObj.log("\n---------- Create table ----------");
//        myObj.createTable(myObj.getTableName());
//        myObj.log("\n---------- Adding Users ----------");
//        myObj.addDataToDB("ntu-user", "12z34");
//        myObj.addDataToDB("ntu-user2", "12yx4");
//        myObj.addDataToDB("ntu-user3", "a1234");
//        myObj.log("\n---------- get Data from the Table ----------");
//        myObj.getDataFromTable(myObj.getTableName());
//        myObj.log("\n---------- Validate users ----------");
//        String[] users = new String[]{"ntu-user", "ntu-user", "ntu-user1"};
//        String[] passwords = new String[]{"12z34", "1235", "1234"};
//        String[] messages = new String[]{"VALID user and password",
//            "VALID user and INVALID password", "INVALID user and VALID password"};
//
//        for (int i = 0; i < 3; i++) {
//            System.out.println("Testing " + messages[i]);
//            if (myObj.validateUser(users[i], passwords[i], myObj.getTableName())) {
//                myObj.log("++++++++++VALID credentials!++++++++++++");
//            } else {
//                myObj.log("----------INVALID credentials!----------");
//            }
//        }
//    }
}
