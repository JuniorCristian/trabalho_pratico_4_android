package com.iel.trabalhopratico4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    public static final String JSON_CONTENT = "JSON_Content";

    private String conteudoJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSONAsyncTask quizJson = new JSONAsyncTask();
        quizJson.execute(getString(R.string.linkJSON));
    }

    public void iniciar(View view){
        Intent intent = new Intent(this, PerguntaActivity.class);
        intent.putExtra(JSON_CONTENT, conteudoJSON);
        startActivity(intent);

    }

    class JSONAsyncTask extends AsyncTask <String, String, String> {

        Button botao = findViewById(R.id.btnStart);

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPreExecute() {
            botao.setEnabled(false);
            botao.setText("Agurade...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String content = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();

                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String linha = "";

                while ((linha = reader.readLine()) != null){
                    buffer.append(linha + "\n");
                }
                content = buffer.toString();
            } catch (IOException ioe) {
                Log.e("JSON", ioe.getMessage());
            }finally {
                try {
                    connection.disconnect();
                    reader.close();
                } catch (IOException e) {
                    Log.w("JSON", e.getMessage());
                }
            }
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                conteudoJSON = get();
                JSONObject json = new JSONObject(conteudoJSON);
                if(json.getInt("response_code") != 1){
                    AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                    ad.setMessage("Não foi possivel carregar o arquivo");
                    ad.setCancelable(true).create().show();
                }
                botao.setEnabled(true);
                botao.setText("Iniciar");
            } catch (Exception ee) {
                Log.e("JSON", ee.getMessage());
            }
        }

    }
}