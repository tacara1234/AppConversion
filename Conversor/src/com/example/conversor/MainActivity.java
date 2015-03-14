package com.example.conversor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends Activity {

	private EditText valorAconvertir;
	private TextView valorConvertido;
	private RadioButton rMXN, rEURO, rUSD, rMXN2, rEURO2, rUSD2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rMXN = (RadioButton) findViewById(R.id.rMXN);
		rEURO = (RadioButton) findViewById(R.id.rEURO);
		rUSD = (RadioButton) findViewById(R.id.rUSD);
		rMXN2 = (RadioButton) findViewById(R.id.rMXN2);
		rEURO2 = (RadioButton) findViewById(R.id.rEURO2);
		rUSD2 = (RadioButton) findViewById(R.id.rUSD2);
		valorAconvertir = (EditText) findViewById(R.id.valorAconvertir);
		valorConvertido = (TextView) findViewById(R.id.valorConvertido);
		//cambiante = (TextView) findViewById(R.id.cambiante);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void lecturaWeb() throws IOException {
		Message msj1 = new Message();
		Message msj2 = new Message();
		URL pagina = new URL(
				"https://www.ecb.europa.eu/stats/exchange/eurofxref/html/index.en.html");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				pagina.openStream()));
		String entrada;
		String valMXN = "";
		String valUSD = "";
		while ((entrada = in.readLine()) != null) {

			if (entrada.contains("<td headers=\"aa\" id=\"USD\">USD</td>")) {

				entrada = in.readLine();
				entrada = in.readLine();

				valUSD = entrada.substring(49, 53);
				msj1.obj = Double.parseDouble(valUSD);
				puente1.sendMessage(msj1);

			}
			if (entrada.contains("<td headers=\"aa\" id=\"MXN\">MXN</td>")) {

				entrada = in.readLine();
				entrada = in.readLine();

				valMXN = entrada.substring(49, 54);
				msj2.obj = Double.parseDouble(valMXN);
				puente2.sendMessage(msj2);
			}

		}

		in.close();
	}

	String val1, resultado;
	double num1;
	DecimalFormat df = new DecimalFormat("0.00");
	Double valorFinalUSD = 0.0, valorFinalMXN;

	private Handler puente1 = new Handler() {
		public void handleMessage(Message msg) {
			valorFinalUSD = (Double) msg.obj;

		}
	};

	private Handler puente2 = new Handler() {
		public void handleMessage(Message msg) {
			valorFinalMXN = (Double) msg.obj;
		}
	};

	public void Convertir(View myView) throws IOException {

		new Thread() {
			public void run() {
				try {

					lecturaWeb();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

		if (rMXN.isChecked() == true) {
			rMXN2.invalidate();

			if (rUSD2.isChecked() == true) {
				val1 = valorAconvertir.getText().toString();
				num1 = Double.parseDouble(val1);
				// Convertir a EditText
				resultado = String.valueOf(df.format(num1 / 15 ));
				valorConvertido.setText(resultado);
			}
			if (rEURO2.isChecked() == true) {
				val1 = valorAconvertir.getText().toString();
				num1 = Double.parseDouble(val1);
				// Convertir a EditText
				resultado = String.valueOf(df.format(num1 / 17));
				valorConvertido.setText(resultado);
			}
		}

		if (rEURO.isChecked() == true) {
			rEURO2.invalidate();
			if (rUSD2.isChecked() == true) {
				val1 = valorAconvertir.getText().toString();
				num1 = Double.parseDouble(val1);

				resultado = String.valueOf(df.format(num1 * valorFinalUSD));
				valorConvertido.setText(resultado);
			}
			if (rMXN2.isChecked() == true) {
				val1 = valorAconvertir.getText().toString();
				num1 = Double.parseDouble(val1);

				resultado = String.valueOf(df.format(num1 * valorFinalMXN));
				valorConvertido.setText(resultado);

			}
		}

		if (rUSD.isChecked() == true) {
			rUSD2.invalidate();
			if (rMXN2.isChecked() == true) {
				val1 = valorAconvertir.getText().toString();
				num1 = Double.parseDouble(val1);
				// Convertir a EditText
				resultado = String.valueOf(df.format(num1 * 15));
				valorConvertido.setText(resultado);
			}
			if (rEURO2.isChecked() == true) {
				val1 = valorAconvertir.getText().toString();
				num1 = Double.parseDouble(val1);
				// Convertir a EditText
				resultado = String.valueOf(df.format(num1 * .93));
				valorConvertido.setText(resultado);

			}
		}
	}

}
