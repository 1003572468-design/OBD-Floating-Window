package com.ileja.aicar.obd.data;

/* JADX INFO: loaded from: classes.dex */
public class AIObdCmd {
    public static final String OBD_AUTO_HOLD_CMD = "AT506=1\r\n";
    public static final String OBD_COMMAND_END = "\r\n";
    public static final String OBD_ERROR_CLEAR_CMD = "AT401\r\n";
    public static final String OBD_ERROR_READ_CMD = "AT400\r\n";
    public static final String OBD_FUEL_CMD = "AT047\r\n";
    public static final String OBD_HS_CMD = "ATHBT\r\n";
    public static final String OBD_LEJA_CARDISPLACEMENT = "AT+DISPLACEMENT=";
    public static final String OBD_LEJA_GET_VERSION = "AT+VERSION?\r\n";
    public static final String OBD_LEJA_MOCK_UPGRADE = "AT+MOCKVERSION";
    public static final String OBD_LEJA_UPGRAGE_ROM_SIZE = "AT+CKLEN=";
    public static final String OBD_LEJA_UPGRAGE_SET_VERSION = "AT+UPV=";
    public static final String OBD_LEJA_UPGRAGE_START = "AT+UPGRADE\r\n";
    public static final String OBD_RT_OFF_CMD = "ATROFF\r\n";
    public static final String OBD_RT_ON_CMD = "ATRON\r\n";
    public static final String OBD_SET_ACCTIME_PRE = "ATACCT";
}