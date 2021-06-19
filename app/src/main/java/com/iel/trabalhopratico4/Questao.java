package com.iel.trabalhopratico4;

import java.util.ArrayList;
import java.util.List;

public class Questao {
    private String imagem;
    private String pergunta;
    private List<String> respostas;
    private int respostaCerta;

    public Questao(String imagem, String pergunta, int respostaCerta, String... respostas){
        this.respostas = new ArrayList<String>();
        this.pergunta = pergunta;
        this.imagem = imagem;
        this.respostaCerta = respostaCerta;
        for (String resposta:respostas)
            this.respostas.add(resposta);
    }

    public String getPergunta() {
        return pergunta;
    }

    public String getImagem() { return imagem; }

    public List<String> getRespostas() {
        return respostas;
    }

    public int getRespostaCerta() {
        return respostaCerta;
    }
}
