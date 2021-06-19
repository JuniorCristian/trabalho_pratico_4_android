package com.iel.trabalhopratico4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ResultadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        ImageView image = findViewById(R.id.imgResultado);
        TextView txtResposta = findViewById(R.id.txtResultado);
        TextView txtPontuacao = findViewById(R.id.txtPontuacao);

        Intent intent = getIntent();
        float pontuacao = intent.getFloatExtra(PerguntaActivity.PONTUACAO, 0);
        int pontos = intent.getIntExtra(PerguntaActivity.PONTOS, 0);
        int total_perguntas = intent.getIntExtra(PerguntaActivity.TOTAL_PERGUNTAS, 0);

        if(pontuacao >= 70) {
            image.setImageResource(R.drawable.sucesso);
            txtResposta.setText("Parabéns, você é o maioral");
        }else {
            image.setImageResource(R.drawable.falha);
            txtResposta.setText("Que pena, você é um fracassado");
        }
        txtPontuacao.setText("Você acertou " + pontos + " de "  + total_perguntas + " questões (" + String.format("%.1f", pontuacao).replace(".", ",") + "% de acerto)");
    }


}