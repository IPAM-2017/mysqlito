package uescc.com.clientesipam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Connection;

/**
 * Created by sigfrid on 05-30-17.
 */

public class database extends SQLiteOpenHelper {



    public database(Context context) {

        super(context, "cliente", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE clientes (codigo INTEGER PRIMARY KEY AUTOINCREMENT, dui TEXT, nombre TEXT, apellido TEXT, modo TEXT, tipo TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST clientes");
        onCreate(db);
    }

    public void cerrar(){
        this.close();

    }

    public void agregar(String dui, String nombres, String apellidos,  String modo,  String tipo){
        ContentValues datos= new ContentValues();
        datos.put("dui", dui);
        datos.put("nombre", nombres);
        datos.put("apellido", apellidos);
        datos.put("modo", modo);
        datos.put("tipo", tipo);
        this.getWritableDatabase().insert("clientes", null, datos);
    }

    public Cursor getDatos(){
        String column[]={"codigo","dui","nombre","apellido","modo","tipo"};
        Cursor c=this.getReadableDatabase().query("clientes", column, null, null, null, null, null);
        return c;
    }

    public void eliminar(int id){
        ContentValues datos= new ContentValues();
        this.getWritableDatabase().delete("clientes", "codigo=" + id + "", null);
    }
}
