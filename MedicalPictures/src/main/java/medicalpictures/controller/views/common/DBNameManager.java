/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicalpictures.controller.views.common;

/**
 * Database tables names for queries.
 *
 * @author PeerZet
 */
public class DBNameManager {

    public static DBNameManager INSTANCE = new DBNameManager();

    private DBNameManager() {
    }

    private static final String DATABASE_NAME = "MedicalPictures";
    private static final String ADMIN_TABLE_NAME = "Admin";
    private static final String DOCTOR_TABLE_NAME = "Doctor";
    private static final String PATIENT_TABLE_NAME = "Patient";
    private static final String TECHNICIAN_TABLE_NAME = "Technician";
    private static final String USERSDB_TABLE_NAME = "UsersDB";
    private static final String PICTURE_TYPE_TABLE_NAME = "PictureType";
    private static final String BODYPART_TABLE_TABLE_NAME = "BodyPart";
    private static final String PICTURE_TABLE_TABLE_NAME = "Picture";
    private static final String ACCOUNT_TYPE_TABLE_TABLE_NAME = "AccountType";
    private static final String DOCTOR_PICTURE_TABLE_TABLE_NAME = "DoctorPicture";

    public static String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static String getAdminTable() {
        return ADMIN_TABLE_NAME;
    }

    public static String getDoctorTable() {
        return DOCTOR_TABLE_NAME;
    }

    public static String getPatientTable() {
        return PATIENT_TABLE_NAME;
    }

    public static String getTechnicianTable() {
        return TECHNICIAN_TABLE_NAME;
    }

    public static String getUsersDbTable() {
        return USERSDB_TABLE_NAME;
    }

    public static String getPictureTypeTable() {
        return PICTURE_TYPE_TABLE_NAME;
    }

    public static String getBodyPartTable() {
        return BODYPART_TABLE_TABLE_NAME;
    }

    public static String getPictureTable() {
        return PICTURE_TABLE_TABLE_NAME;
    }

    public static String getAccountType() {
        return ACCOUNT_TYPE_TABLE_TABLE_NAME;
    }

    public static String getDoctorPictureTable() {
        return DOCTOR_PICTURE_TABLE_TABLE_NAME;
    }

}
