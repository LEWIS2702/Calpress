package com.example.windows.calpress;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Spinner spinner_tipo;
    Spinner spinner2;
    Button button;
    TextView totalTextView;
    TextView intereseTextView;
    TextView cuotasTextView;
    EditText tasa;
    EditText cuotas;
    EditText monto;
    Button buttontabla;



   TextView MyDispalayDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner_tipo = (Spinner) findViewById(R.id.tipo_prestamos);
        spinner2 = (Spinner) findViewById(R.id.frecuencia);
        button = (Button) findViewById(R.id.procesar);

        buttontabla = (Button) findViewById(R.id.button2);
        monto = (EditText)findViewById(R.id.monto);
        cuotas = (EditText)findViewById(R.id.cuotas);
        totalTextView = (TextView) findViewById(R.id.total);
        intereseTextView = (TextView) findViewById(R.id.interes);
        cuotasTextView = (TextView) findViewById(R.id.cuotasR);
        tasa = (EditText) findViewById(R.id.tasa);
        MyDispalayDate = (TextView) findViewById(R.id.fecha);

        MyDispalayDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy:" + month + "/" + day + "/" + year);
                String date = day  + "/" + month + "/" + year;
                MyDispalayDate.setTextColor(getResources().getColor(R.color.colortext));
                MyDispalayDate.setText(date);
            }
        };

        spinner_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colortext));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colortext));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.frecuencia, android.R.layout.simple_spinner_item);

        spinner_tipo.setAdapter(adapter);
        spinner2.setAdapter(adapter2);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttontabla.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                prestamosCapitalizado();

            }
        });



    }

    public double saberFrecuencia(double valor){
        String tipoFrecuencia = spinner2.getSelectedItem().toString();

       double c = 0.0;

        switch (tipoFrecuencia){
            case "Semanal":
                c = valor / 4 ;
                break;

            case "Quincenal":
                c = valor / 2;
                break;

            case "Mensual":
                c = valor;
                break;

        }


        return c;
    }


    public void prestamosCapitalizado() {
        String tipoPrestamos =  spinner_tipo.getSelectedItem().toString();
        String montoText = monto.getText().toString();
        String cuotasText =cuotas.getText().toString();
        final int cuotasInt = Integer.parseInt(cuotasText);
        String porcientoText = tasa.getText().toString();
        final int montoInt = Integer.parseInt(montoText);
        final double porcientoDouble = Double.parseDouble(porcientoText);
        double porcentaje = (porcientoDouble / 100);


        if (tipoPrestamos.equals("Amortizado")) {

            double n1 = Math.pow(1 + porcentaje, cuotasInt) * porcentaje;
            double n2 = Math.pow(1 + porcentaje, cuotasInt) - 1;
            double resultado = montoInt * (n1 / n2);
            double totalCuota = resultado * cuotasInt;
            double interesesCalculado = totalCuota - montoInt;
            double m =  saberFrecuencia(interesesCalculado);



            totalTextView.setText(Double.toString(m));
            intereseTextView.setText(String.format(Locale.getDefault(),"%.2f", m * cuotasInt ));
            cuotasTextView.setText(String.format(Locale.getDefault(),"%.2f",resultado));




        }else {
            double interesFinal = montoInt * porcentaje;
            double m =  saberFrecuencia(interesFinal);
            final double totalIntereses = m * cuotasInt;
            final double totalCuota = totalIntereses + montoInt;
            final double pago = totalCuota / cuotasInt;
            final String fecha = MyDispalayDate.getText().toString();

            totalTextView.setText(String.format(Locale.getDefault(),"%.2f",totalCuota));
            intereseTextView.setText(String.format(Locale.getDefault(),"%.2f",totalIntereses));
            cuotasTextView.setText(String.format(Locale.getDefault(),"%.2f",pago));

            buttontabla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, TablaPrestamos.class);
                    String tipoFrecuencia = spinner2.getSelectedItem().toString();
                    i.putExtra("cuota", pago);
                    i.putExtra("cantidadCuotas", cuotasInt);
                    i.putExtra("totalPrestamos", totalCuota);
                    i.putExtra("totalIntereses", totalIntereses);
                    i.putExtra("montoPrestamo", montoInt);
                    i.putExtra("frecuencia", tipoFrecuencia);
                    i.putExtra("tasa", porcientoDouble);
                    i.putExtra("fecha", fecha);
                    startActivity(i);
                }
            });
    }



    }






}