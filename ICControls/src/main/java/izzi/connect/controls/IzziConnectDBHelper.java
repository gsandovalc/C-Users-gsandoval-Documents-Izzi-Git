package izzi.connect.controls;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Carlos on 08/11/15.
 */
 class IzziConnectDBHelper extends SQLiteOpenHelper {


    private static int version=1;
    private static String name="IzziConnectDb";
    private static SQLiteDatabase.CursorFactory factory=null;

     IzziConnectDBHelper(Context context){
        super(context,name,factory,version);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IZZICONNECT("
            +" CLIENT_ID TEXT, "
                    +" CLIENT_SECRET TEXT, "
                    +" UID TEXT, "
                    +" SECRET TEXT, "
                    +" TOKEN TEXT, "
                    +" TOKEN_TYPE TEXT, "
                    +" NAME TEXT, "
                    +" ACCOUNT TEXT, "
                    +" PAQ TEXT, "
                    +" EXP TEXT)");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
