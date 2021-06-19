package com.iel.trabalhopratico4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class PerguntaActivity extends AppCompatActivity {
    public static final String ACERTO = "acerto";
    public static final String RESPOSTA_CERTA = "resposta_certa";
    public static final String PONTOS = "pontos";
    public static final String PONTUACAO = "pontuacao";
    public static final String TOTAL_PERGUNTAS = "total_perguntas";
    private TextView pergunta;
    private ImageView imagem;
    private Button resposta1;
    private Button resposta2;
    private Button resposta3;
    private Button resposta4;
    private int respostaCerta = 0;
    private String correta;
    private Handler handler = new Handler();

    private List<Questao> questoes;
    private int total_perguntas;

    private int pontos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pergunta);

        pergunta = findViewById(R.id.txtPergunta);
        imagem = findViewById(R.id.imageView);
        resposta1 = findViewById(R.id.btnResposta1);
        resposta2 = findViewById(R.id.btnResposta2);
        resposta3 = findViewById(R.id.btnResposta3);
        resposta4 = findViewById(R.id.btnResposta4);

        questoes = new ArrayList<Questao>();


        Intent intentMain = getIntent();
        try {
            JSONObject json = new JSONObject(intentMain.getStringExtra(MainActivity.JSON_CONTENT));
            JSONArray results = json.getJSONArray("results");
            System.out.println(results);
            for (int i = 0; i < results.length(); i++) {
                JSONObject questao = results.getJSONObject(i);
                String pergunta = questao.getString("pergunta");
                String imagem = questao.getString("imagem");
                String correta = questao.getString("resposta_correta");
                String incorreta1 = questao.getString("resposta_incorreta_1");
                String incorreta2 = questao.getString("resposta_incorreta_2");
                String incorreta3 = questao.getString("resposta_incorreta_3");

                List<String> respostas = new ArrayList<String>();
                respostas.add(correta);
                respostas.add(incorreta1);
                respostas.add(incorreta2);
                respostas.add(incorreta3);

                int respostaCorreta = 0;

                Collections.shuffle(respostas);
                for (int k = 0; k < respostas.size(); k++) {
                    if (correta.equals(respostas.get(k)))
                        respostaCorreta = k + 1;
                }

                questoes.add(new Questao(imagem, pergunta, respostaCorreta, respostas.get(0), respostas.get(1), respostas.get(2), respostas.get(3)));
            }
        } catch (JSONException je) {
            Log.e("JSON", je.getMessage());
        }
        total_perguntas = questoes.size();

        if (!questoes.isEmpty()) {
            carregaQuestao();
        } else {
            Intent intent = new Intent(this, ResultadoActivity.class);
            intent.putExtra(PONTOS, pontos);
            intent.putExtra(TOTAL_PERGUNTAS, total_perguntas);
            intent.putExtra(PONTUACAO, (float) pontos / total_perguntas * 100);
            startActivity(intent);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (!questoes.isEmpty()) {
            carregaQuestao();
        } else {
            Intent intent = new Intent(this, ResultadoActivity.class);
            intent.putExtra(PONTOS, pontos);
            intent.putExtra(TOTAL_PERGUNTAS, total_perguntas);
            intent.putExtra(PONTUACAO, (float) pontos / total_perguntas * 100);
            startActivity(intent);
        }
    }

//    public void responder(View view) {
//        int respostaEscolhida = 0;
//        boolean acerto = respostaEscolhida == respostaCerta;
//        if (acerto)
//            pontos++;
//    }

    private void carregaQuestao(){
        Questao questao = questoes.remove(0);

        new Thread() {
            public void run() {
                Bitmap img = null;
                try {
                    URL link = new URL(questao.getImagem());
                    HttpsURLConnection connex = (HttpsURLConnection) link.openConnection();
                    InputStream input = connex.getInputStream();
                    img = BitmapFactory.decodeStream(input);

                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
                Bitmap imgAux = img;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imagem.setImageBitmap(imgAux);
                    }
                });
            }
        }.start();
        pergunta.setText(questao.getPergunta());
        respostaCerta = questao.getRespostaCerta();
        switch (respostaCerta){
            case 1:
                respostaCerta = R.id.btnResposta1;
                break;
            case 2:
                respostaCerta = R.id.btnResposta2;
                break;
            case 3:
                respostaCerta = R.id.btnResposta3;
                break;
            case 4:
                respostaCerta = R.id.btnResposta4;
                break;
        }
        resposta1.setText(questao.getRespostas().get(0));
        resposta2.setText(questao.getRespostas().get(1));
        resposta3.setText(questao.getRespostas().get(2));
        resposta4.setText(questao.getRespostas().get(3));

    }

    @SuppressLint("ResourceAsColor")
    public void clicaBotao(View view){
        System.out.println("Botão Clicado: "+view.getId());
        System.out.println("Botão certo: "+respostaCerta);
        System.out.println("Botão 1: "+R.id.btnResposta1);
        System.out.println("Botão 2: "+R.id.btnResposta2);
        System.out.println("Botão 3: "+R.id.btnResposta3);
        System.out.println("Botão 4: "+R.id.btnResposta4);
        if(view.getId() == respostaCerta){
            view.setBackgroundColor(Color.rgb(97, 232, 67));
            pontos++;
        }else{
            view.setBackgroundColor(Color.rgb(182, 50, 50));
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    view.setBackgroundColor(Color.rgb(51, 181, 229));
                    onRestart();
                }
            }
        });
        thread.start();
    }
}