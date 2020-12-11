package com.e.base;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etNombre,etPrecio,etDescripcion;
    private ListView lvProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = (EditText) findViewById(R.id.etProducto);
        etPrecio = (EditText)findViewById((R.id.etPrecio));
        etDescripcion= (EditText)findViewById(R.id.etDescripcion);
        lvProductos =(ListView) findViewById(R.id.lvProductos);
        lvActualizar();

    }



    public void agregar(View v) {

        LiteDB db = new LiteDB(this, "stocks", null, 1);

        String sNombre = etNombre.getText().toString();
        String sPrecio = etPrecio.getText().toString();
        String sDescripcion = etDescripcion.getText().toString();
        if (db.search(sNombre, Float.parseFloat(sPrecio), sDescripcion) != 0) {
            Toast.makeText(this, getResources().getString(R.string.duplicado), Toast.LENGTH_LONG).show();
        } else {

            if (!sNombre.isEmpty() && !sPrecio.isEmpty()) {
                if (db.insert(sNombre, Float.parseFloat(sPrecio), sDescripcion)) {
                    Toast.makeText(this, getResources().getString(R.string.agregado), Toast.LENGTH_LONG).show();
                    lvActualizar();
                    db.close();
                    limpiarCampos();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.noagregado), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.completarambos), Toast.LENGTH_LONG).show();
            }

        }

    }


    public void borrar(View v){
        LiteDB db = new LiteDB(this, "stocks", null, 1);
        String sNombre = etNombre.getText().toString();
        if(!sNombre.isEmpty()){
            int cant = db.delete(sNombre);
            if(cant != 0){
                Toast.makeText(this, getResources().getString(R.string.eliminacion)+" "+cant+" "+getResources().getString(R.string.prod)+": "+sNombre, Toast.LENGTH_LONG).show();
                limpiarCampos();
            }else{
                Toast.makeText(this, getResources().getString(R.string.noeliminacion), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, getResources().getString(R.string.nodatanombre), Toast.LENGTH_LONG).show();
        }
        db.close();
        lvActualizar();
    }

    public void actualizar(View v){
        LiteDB db = new LiteDB(this, "stocks", null, 1);
        String sNombre = etNombre.getText().toString();
        String sPrecio = etPrecio.getText().toString();
        String sDescripcion = etDescripcion.getText().toString();
        if(!sNombre.isEmpty() && !sPrecio.isEmpty()){
            int cant= db.update(sNombre, Float.parseFloat(sPrecio), sDescripcion);
            if(cant != 0){
                Toast.makeText(this, getResources().getString(R.string.actualizacion)+" "+cant+" "+getResources().getString(R.string.prod)+": "+sNombre, Toast.LENGTH_LONG).show();
                limpiarCampos();
            }else{
                Toast.makeText(this, getResources().getString(R.string.noactualizacion), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, getResources().getString(R.string.completarambos), Toast.LENGTH_LONG).show();
        }
        db.close();
        lvActualizar();
    }



    public void lvActualizar(){
        LiteDB db = new LiteDB(this, "stocks", null, 1);
        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, db.generarLista());
        lvProductos.setAdapter(adaptador);
    }
    private void limpiarCampos(){
        etPrecio.setText("");
        etNombre.setText("");
        etDescripcion.setText("");
    }
}