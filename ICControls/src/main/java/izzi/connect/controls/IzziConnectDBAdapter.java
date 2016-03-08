package izzi.connect.controls;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Carlos on 08/11/15.
 */
 class IzziConnectDBAdapter {
     static final String C_TABLA = "IZZICONNECT" ;

    /**
     * Definimos constantes con el nombre de las columnas de la tabla
     */
    public static final String C_CLIENT_ID  = "CLIENT_ID";
    public static final String C_CLIENT_SECRET = "CLIENT_SECRET";
    public static final String C_UID = "UID";
    public static final String C_SECRET = "SECRET";
    public static final String C_TOKEN = "TOKEN";
    public static final String C_TOKEN_REFRESH = "TOKEN_REFRESH";
    public static final String C_RESPONSE = "RESPONSE";

    private Context contexto;
    private IzziConnectDBHelper dbHelper;
    private SQLiteDatabase db;

    private String[] columnas = new String[]{ C_CLIENT_ID,C_CLIENT_SECRET,C_UID,C_SECRET,C_TOKEN,C_TOKEN_REFRESH,C_RESPONSE} ;

    public IzziConnectDBAdapter(Context context){

        this.contexto=context;
    }

    public IzziConnectDBAdapter open() throws SQLException{
        dbHelper= new IzziConnectDBHelper(contexto);
        db=dbHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        dbHelper.close();
    }
    public Cursor getCursor() throws SQLException
    {
        Cursor c = db.query( true, C_TABLA, columnas, null, null, null, null, null, null);

        return c;
    }

    public IzziConnectResponse getRecord() throws SQLException
    {
        Cursor c = db.rawQuery("SELECT * FROM "+C_TABLA,null);
        IzziConnectResponse response=null;
        //Nos movemos al primer registro de la consult
        if (c != null) {
            c.moveToFirst();
            response=new IzziConnectResponse();
            response.setToken_type(c.getString(0));
        }
        return response;
    }
}
