package com.example.dashrunningapp.SQLiteDb;


    import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    import com.example.dashrunningapp.exceptions.NoStoredUserException;
    import com.example.dashrunningapp.exceptions.TooManyUsersException;
    import com.example.dashrunningapp.models.UserDetails;

    import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//DBhelper class manages application sqlite database on the device. Checks if db exist and if not creates on with the appropriate tables
// Allows to check if a user exists in the database
    public class DbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "Dash.db";
        private static final int DATABASE_VERSION = 1;
        private final Context context;
        SQLiteDatabase db;

        private static final String DATABASE_PATH = "/data/data/com.example.dashrunningapp/databases/";
        private final String USER_TABLE = "User";

        //DB helper constrcutor
        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            createDb();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        public void createDb(){
            boolean dbExist = checkDbExist();   //checks if db already exists on device

            if(!dbExist){
                this.getReadableDatabase();
                copyDatabase();
            }
        }


        private boolean checkDbExist(){
            SQLiteDatabase sqLiteDatabase = null;
            //try and open the database
            try{
                String path = DATABASE_PATH + DATABASE_NAME;
                sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            } catch (Exception ex){
            }
            //if database instance exists
            if(sqLiteDatabase != null){
                sqLiteDatabase.close();
                return true;
            }
            return false;
        }

        private void copyDatabase(){
            try {
                InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                String outFileName = DATABASE_PATH + DATABASE_NAME;
                OutputStream outputStream = new FileOutputStream(outFileName);

                byte[] b = new byte[1024];
                int length;
                while ((length = inputStream.read(b)) > 0){
                    outputStream.write(b, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        private SQLiteDatabase openDatabase(){
            String path = DATABASE_PATH + DATABASE_NAME;
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
            return db;
        }

        public void close(){
            if(db != null){
                db.close();
            }
        }

        //adds users email and password into dash db user table
        public void AddUser(String email, String password){
            db = openDatabase();
            db.execSQL("CREATE TABLE IF NOT EXISTS User(email VARCHAR,password VARCHAR);");
            db.execSQL("DELETE FROM User;");
            db.execSQL("INSERT INTO User VALUES(\'"+ email +"\', \'"+ password +"\');");
            db.close();

        }
        //if a user exists already when adding another i.e. log in as somone else call this function to delete all other stored users
        public void DropUserTable(){
            db = openDatabase();
            db.execSQL("DROP TABLE IF EXISTS User;");
            db.close();
        }

        //Checks to see if user is in table and if so returns user object
        public UserDetails checkUserExist() throws NoStoredUserException, TooManyUsersException {
            UserDetails loginDetails = new UserDetails();
            String[] columns = {"email", "password"};

            db = openDatabase();
            db.execSQL("CREATE TABLE IF NOT EXISTS User(email VARCHAR,password VARCHAR);"); //if user table doesnt exist it will create one
            Cursor cursor = db.query(USER_TABLE, columns, null, null, null, null, null);    //query user table for all records and holds info in cursor

            if (cursor != null && cursor.getCount()>0) {
                cursor.moveToFirst();       //selects first record
            }
            else
                throw new NoStoredUserException();      //throw exception if none exist

            if (cursor.getCount() > 1) {
                throw new TooManyUsersException();      //throw exception when too many user, dont know which one to log in with
            }
            //Sets login detail properties to send for API verification
            loginDetails.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            loginDetails.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            cursor.close();
            db.close();
            return loginDetails;
        }

    }
