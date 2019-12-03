package com.parentapp.constants;

public class Endpoints {
    //Network URLS
    /*
    public static String TRACK_LOCATION_NOTIFICATION_URL = "http://192.168.1.153:80/safechild/send_location_request_notification.php";
    public static String SIGN_IN_URL = "http://192.168.1.153:80/safechild/parent_login.php";
    public static String GET_PARENT_DATA_URL = "http://192.168.1.153:80/safechild/get_parent_data.php";
    //public static String GET_PARENT_DATA_URL = "http://10.0.2.2:80/safechild/get_parent_data.php";
    public static final String GET_ALL_CHILDREN_URL = "http://192.168.1.153:80/safechild/get_all_children.php";
    public static final String GET_CHILD_EVENTS_HISTORY_URL = "http://192.168.1.153:80/safechild/child_events_history.php";
    public static final String GET_CHILD_LOCATION_URL = "http://192.168.1.153:80/safechild/get_child_location.php";

*/

    //Katya

    public static String SIGN_IN_URL = "http://155.246.76.17:3000/authenticateParent";
    public static String GET_PARENT_DATA_URL = "http://155.246.76.17:3000/parentData";
    public static String TRACK_LOCATION_NOTIFICATION_URL = "http://155.246.76.17:3000/childLocationRequestNotification";
    public static String PARENT_FCM_TOKEN_UPDATE_URL = "http://155.246.76.17:3000/parentFCMTokenUpdate";
    public static final String GET_CHILD_LOCATION_URL = "http://155.246.76.17:3000/sendLastKnownLocationToParent";
    public static final String GET_CHILD_EVENT_HISTORY_URL = "http://155.246.76.17:3000/returnAlertHistory";
    public static final String GET_ALL_CHILDREN_URL = "http://155.246.76.17:3000/allChildren";
    public static final String GET_CHILD_EVENTS_HISTORY_URL = "http://155.246.76.17:3000/returnAlertHistory";





    //public static String SIGN_IN_URL = "http://155.246.76.71:3000/authenticateParent";
    //public static String SIGN_IN_URL = "http://155.246.76.71:3000/";


    //tasks
    public static final String SEND_LOCATION_REQUEST = "send location";
    public static final String STOP_LOCATION_REQUEST = "stop location";
    public static final String GET_PARENT_DATA = "get_parent_data";
    public static final String SIGN_IN = "sign_in";
    public static final String REQUEST_LOCATION_TASK = "request_location";
    public static final String PARENT_UPDATE_FCM_TOKEN = "parent_update_fcm_token";
    public static final String GET_CHILD_LOCATION_TASK = "get_child_location";
    public static final String GET_CHILD_EVENTS_HISTORY_TASK = "get_event_history";
    public static final String GET_ALL_CHILDREN_TASK = "get_all_children";
}