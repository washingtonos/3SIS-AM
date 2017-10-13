package br.com.fiap.am.model;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Renan Lima on 02/10/2017.
 */

public class Produto {

    private String nome;
    private String preco;
    private String descricao;
    private String quantidade;
    private String imagemUrl;
    private String usuarioId;
    private TextView precoProduto;
    private TextView nomeProduto;
    private ImageView imagemProduto;


    public ImageView getImagemProduto() {
        return imagemProduto;
    }

    public void setImagemProduto(ImageView imagemProduto) {
        this.imagemProduto = imagemProduto;
    }

    public TextView getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(TextView nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public TextView getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(TextView precoProduto) {
        this.precoProduto = precoProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
