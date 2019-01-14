package pl.softtech.personaladdressbook.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

public class AddressBookDatabaseHelper extends SQLiteOpenHelper {

    /* Nazwa bazy danych */
    private static final String DATABASE_NAME = "AddressBook.db";

    /* Wersja bazy danych */
    private static final int DATABASE_VERSION = 1;

    /* Konstruktor tworzący bazę danych */
    public AddressBookDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        /* Zapytnie SQL tworzące tabelę w bazie danych */
        final String CREAATE_CONTACTS_TABLE =
                "CREATE TABLE " + DatabaseDescription.Contact.TABLE_NAME +
                        "(" + DatabaseDescription.Contact._ID + " integer primary key, " +
                        DatabaseDescription.Contact.COLUMN_NAME + " TEXT, " +
                        DatabaseDescription.Contact.COLUMN_PHONE + " TEXT, " +
                        DatabaseDescription.Contact.COLUMN_EMAIL + " TEXT, " +
                        DatabaseDescription.Contact.COLUMN_STREET + " TEXT, " +
                        DatabaseDescription.Contact.COLUMN_CITY + " TEXT, " +
                        DatabaseDescription.Contact.COLUMN_STATE + " TEXT, " +
                        DatabaseDescription.Contact.COLUMN_ZIP + " TEXT);";

        /* Egzekucja zapytania SQL */
        db.execSQL(CREAATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}
}
