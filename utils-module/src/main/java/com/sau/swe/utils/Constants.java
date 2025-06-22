package com.sau.swe.utils;


public class Constants {

    // ACCOUNT BALANCE FOR INITIAL CREATE
    public static final Long INITIAL_BALANCE = 0L;

    // CHARSET TO CREATE CUSTOM UNIQUE TRANSFER_CODE
    public static final String TRANSFER_CODE_CHARSET = "ABCDEFGHIJKLMNOPRSTUVYZXQ01234567989";

    // API URL TO CREATE ACCOUNT
    public static final String CREATE_ACCOUNT_URL = "http://localhost:8081/finance-mgmt/api/account/add-account";
    //SERVICE RUNNING PORTS
    public static final Integer AUTH_RUN_PORT = 8080;
    public static final Integer ACCOUNT_RUN_PORT = 8081;
    public static final Integer REPORT_RUN_PORT = 8082;
    public static final Integer CURRENCY_EXCHANGE_RUN_PORT = 8083;
    public static final Integer USER_RUN_PORT = 8084;
    public static final Integer DISCOVERY_SERVER_RUN_PORT = 8761;
    public static final Integer API_GATEWAY_RUN_PORT = 8086;


}
