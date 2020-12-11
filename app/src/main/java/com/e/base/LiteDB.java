package com.e.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class LiteDB extends SQLiteOpenHelper {

    public LiteDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table productos(nombre text, precio single, descripcion text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public int search(String nombre, float precio, String descripcion){
        SQLiteDatabase database= this.getWritableDatabase();
        Cursor c = database.rawQuery("SELECT nombre, precio, descripcion FROM productos WHERE nombre ='"+nombre+ "'",null);
        return c.getCount();
    }

    public boolean insert(String nombre, float precio, String descripcion){
        SQLiteDatabase database= this.getWritableDatabase();

        ContentValues contenedor = new ContentValues();
        contenedor.put("nombre",nombre);
        contenedor.put("precio", precio);
        contenedor.put("descripcion", descripcion);
        try {
            database.insertOrThrow("productos", null,contenedor);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        database.close();
        return false;

    }




    public int delete(String nombre){
        SQLiteDatabase database = this.getWritableDatabase();
        int cantElim = database.delete("productos", "nombre='"+nombre+ "'", null);
        database.close();
        return cantElim;
    }

    public int update(String nombre, float precio, String descripcion){
        SQLiteDatabase database= this.getWritableDatabase();
        ContentValues contenedor = new ContentValues();
        contenedor.put("nombre",nombre);
        contenedor.put("precio",precio);
        contenedor.put("descripcion",descripcion);
        int cantAct = database.update("productos", contenedor, "nombre='"+nombre+ "'", null);
        database.close();
        return cantAct;
    }


    public ArrayList<String> generarLista(){
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "select * from productos";
        Cursor registros = database.rawQuery(query, null);
        if(registros.moveToFirst()){
            do{
                lista.add(registros.getString(0) + "\n$"+registros.getString(1)+ "\n"+registros.getString(2));
            }while(registros.moveToNext());
        }
        return lista;
    }
}

