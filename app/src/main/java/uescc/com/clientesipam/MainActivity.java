package uescc.com.clientesipam;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    ListView listView;
    public static List<Cliente> clientes;
    private AdaptadorClientes adaptador;
    database db;
    ArrayList<String> lista;
    ArrayAdapter adp;
    List<String> item=null;
    Cliente cliente = new Cliente();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientes = new ArrayList<Cliente>();

        adaptador = new AdaptadorClientes(this,clientes);

        listView = (ListView) findViewById(R.id.listViewClientes);

        registerForContextMenu(listView);
        mostrar();

    }

    public void mostrar(){
        db=new database(this);
        Cursor c=db.getDatos();
        item=new ArrayList<String>();
        String dui="",nombre="",apellido="",modo="", tipo="", codigo="";
        if(c.moveToFirst()){
            do{
                codigo=c.getString(0);
                dui=c.getString(1);
                nombre=c.getString(2);
                apellido=c.getString(3);
                modo=c.getString(4);
                tipo=c.getString(5);

                this.cliente.setPagoCli(modo);
                this.cliente.setNombreCli(nombre);
                this.cliente.setApellidoCli(apellido);
                this.cliente.setDuiCli(dui);
                this.cliente.setTipoCli(tipo);
                this.cliente.setCodigoCli(codigo);
                MainActivity.clientes.add(cliente);
                //Toast.makeText(getApplicationContext(), ""+getResources().getString(R.string.msg_agregado), Toast.LENGTH_LONG).show();
                this.cliente = new Cliente();

             item.add(codigo+" "+dui+" "+nombre+" "+apellido+" "+modo+" "+tipo);
            }while (c.moveToNext());
            //ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item);
            //listView.setAdapter(adapter);
        }

        //TextView lblCodigo = (TextView)item.findViewById(R.id.LblCodigo);
        //TextView lblNombre = (TextView)item.findViewById(R.id.LblNombre);
        //TextView lblApellidos = (TextView)item.findViewById(R.id.LblApellido);
        //TextView lblTipo = (TextView)item.findViewById(R.id.LblTipo);
        //TextView lblFormaDePago = (TextView)item.findViewById(R.id.LblFormaDePago);
        //TextView lblDui = (TextView)item.findViewById(R.id.LblDUI);

    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        listView.setAdapter(adaptador);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opcion_agregar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.MnuOpc1:
                Intent intent = new Intent(MainActivity.this,CrearClienteActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contexto_eliminar,menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Tengo que eliminar el registro
        //para modificar hay que crear una nueva actividad
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){

            case R.id.eliminar:
                String codigo=clientes.get(info.position).getCodigoCli();

                int id=Integer.parseInt(codigo);
                eliminarPersona(id);
                System.out.println(codigo);

                //clientes.remove(info.position);
                adaptador.notifyDataSetChanged();
                return true;


            case R.id.modificar:
                editarPersona((int)info.id);
                return true;

            default:
                return super.onContextItemSelected(item);



        }
    }

    public void eliminarPersona(int id) {


        database db=new database(this);
        db.eliminar(id);
        db.close();

        Toast.makeText(getApplicationContext(), "Eliminado cliente"+id, Toast.LENGTH_LONG).show();

    }
    public void editarPersona(int id) {
        //obtengo el cliente seleccionado
        Cliente cliente = clientes.get(id);
        // Se dirige a la actividad ModificarClienteActivity.
        Intent intent = new Intent(MainActivity.this, ModificarClienteActivity.class);
        // Se le coloca parametros para enviar a la actividad ModificarClienteActivity del cliente seleccionado
        intent.putExtra("index", id );
        intent.putExtra("codigo", cliente.getCodigoCli());
        intent.putExtra("nombre", cliente.getNombreCli());
        intent.putExtra("apellido", cliente.getApellidoCli());
        intent.putExtra("dui", cliente.getDuiCli());
        intent.putExtra("tipo", cliente.getTipoCli());
        intent.putExtra("pago", cliente.getPagoCli());


        startActivity(intent);

    }

    class AdaptadorClientes extends ArrayAdapter<Cliente>
    {
        Activity context;

        public AdaptadorClientes(Activity context, List<Cliente> datos){
            super(context,R.layout.listitem_cliente,datos);

        }

        public View getView(int posicion, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.listitem_cliente, null);

            TextView lblCodigo = (TextView)item.findViewById(R.id.LblCodigo);
            TextView lblNombre = (TextView)item.findViewById(R.id.LblNombre);
            TextView lblApellidos = (TextView)item.findViewById(R.id.LblApellido);
            TextView lblTipo = (TextView)item.findViewById(R.id.LblTipo);
            TextView lblFormaDePago = (TextView)item.findViewById(R.id.LblFormaDePago);
            TextView lblDui = (TextView)item.findViewById(R.id.LblDUI);

            lblCodigo.setText(clientes.get(posicion).getCodigoCli());
            lblNombre.setText(clientes.get(posicion).getNombreCli());
            lblApellidos.setText(clientes.get(posicion).getApellidoCli());
            lblTipo.setText(clientes.get(posicion).getTipoCli());
            lblFormaDePago.setText(clientes.get(posicion).getPagoCli());
            lblDui.setText(clientes.get(posicion).getDuiCli());


            return (item);
        }





    }
}
