package com.trongvan.webservices;

public class URLInteraction {
    private static final String SERVER_ADDRESS = "http://192.168.1.5";
    public static final String PROJECT_FOLDER = "/firstdemo";

    public static final String URL_GET_DATA = SERVER_ADDRESS + PROJECT_FOLDER + "/get_data.php";
    public static final String URL_INSERT_DATA =SERVER_ADDRESS + PROJECT_FOLDER +  "/insert_data.php";
    public static final String URL_UPDATE_DATA = SERVER_ADDRESS + PROJECT_FOLDER + "/update.php";
    public static final String URL_DELETE_DATA = SERVER_ADDRESS + PROJECT_FOLDER + "/delete.php";
}
