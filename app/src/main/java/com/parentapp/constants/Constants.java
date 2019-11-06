package com.parentapp.constants;

public class Constants {
    //Network URLS
    public static String TRACK_LOCATION_NOTIFICATION_URL = "http://192.168.1.153:80/safechild/send_location_request_notification.php";
    public static String SIGN_IN_URL = "http://192.168.1.153:80/safechild/parent_login.php";
    public static String GET_PARENT_DATA_URL = "http://192.168.1.153:80/safechild/get_parent_data.php";
    //public static String GET_PARENT_DATA_URL = "http://10.0.2.2:80/safechild/get_parent_data.php";

    //Katya
    //public static String SIGN_IN_URL = "http://155.246.218.202:3000/authenticateParent";
    //public static String GET_PARENT_DATA_URL = "http://155.246.218.202:3000/parentData";

    //public static String SIGN_IN_URL = "http://155.246.218.71:3000/authenticateParent";
    //public static String SIGN_IN_URL = "http://155.246.218.71:3000/";


    //tasks
    public static final String SEND_LOCATION_REQUEST = "send location";
    public static final String STOP_LOCATION_REQUEST = "stop location";
    public static final String GET_PARENT_DATA = "get_parent_data";
    public static final String SIGN_IN = "sign_in";
    public static final String GET_MAPS = "maps";

}