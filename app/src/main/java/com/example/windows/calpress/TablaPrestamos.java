package com.example.windows.calpress;

import android.content.ClipData;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

public class TablaPrestamos extends AppCompatActivity {

    TextView prueba;
    TextView interesTable;
    TextView montoTable;
    TextView tasaTable;
    TextView tipoTable;
    TableView<String[]> tableView;
    private static final String[] headerTable =  {"#", "Fecha", "Saldo", "Interes", "Capital","Cuotas" };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_prestamos);


        tableView = (TableView<String[]>) findViewById(R.id.tableView);
        interesTable = (TextView) findViewById(R.id.interesTable);
        tasaTable = (TextView) findViewById(R.id.tasaTable);
        tipoTable = (TextView) findViewById(R.id.frecuenciaTable);
        montoTable = (TextView) findViewById(R.id.montoTable);

        final SimpleTableHeaderAdapter dataAdapter = new SimpleTableHeaderAdapter(this,headerTable);
        dataAdapter.setTextColor(ContextCompat.getColor(this, R.color.black));
        tableView.setHeaderAdapter(dataAdapter);
        int colorEvenRows = getResources().getColor(R.color.white);
        int colorOddRows = getResources().getColor(R.color.colortext);
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));





        crearDataTable();
        // prueba = (TextView) findViewById(R.id.prueba);
        //recibirDatos();
    }


    public  void recibirDatos(){



    }


    public void crearDataTable(){
        Bundle extras = getIntent().getExtras();
        double cuotas = extras.getDouble("cuota");
        double totalPrestamos = extras.getDouble("totalPrestamos");
        double totalIntereses = extras.getDouble("totalIntereses");
        int montoPrestamos = extras.getInt("montoPrestamo");
        int cantidadCuotas = extras.getInt("cantidadCuotas");
        String frecuencia = extras.getString("frecuencia");
        double tasa = extras.getDouble("tasa");
        String fecha = extras.getString("fecha");



        tipoTable.setText(frecuencia);
        interesTable.setText(Double.toString(totalIntereses));
        montoTable.setText(Integer.toString(montoPrestamos));
        tasaTable.setText(Double.toString(tasa));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();

        try {

            c.setTime(sdf.parse(fecha));
        } catch (ParseException e) {
            e.printStackTrace();
        }



        String[][] tablaResultado = new String[cantidadCuotas][6];
        int numeros = 0;
        for (int row = 0; row < tablaResultado.length; row++) {
            numeros++;
            if(frecuencia.equals("Mensual")){
                c.add(Calendar.MONTH, periodo());
            }else{
                c.add(Calendar.DATE, periodo());
            }

            String end_date = sdf.format(c.getTime());
            for (int col = 0; col <  tablaResultado[row].length; col++) {
                tablaResultado[row][0] = Integer.toString(numeros);
                tablaResultado[row][1] = end_date;
                double m = cuotas * numeros;
                tablaResultado[row][2] = String.format(Locale.getDefault(),"%.2f",totalPrestamos - m);
                tablaResultado[row][3] = Double.toString(totalIntereses / cantidadCuotas);
                tablaResultado[row][4] = Integer.toString(montoPrestamos / cantidadCuotas);
                tablaResultado[row][5] = String.format(Locale.getDefault(),"%.2f",cuotas);

            } }
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, tablaResultado));

    }
    public int periodo(){
        int valor = 0;
        switch (tipoTable.getText().toString()){
            case "Mensual":
                valor = 1;
                break;
            case "Quincenal":
                valor = 15;
                break;
            case "Semanal":
                valor = 7;
                break;
    }
  return valor;
}
}