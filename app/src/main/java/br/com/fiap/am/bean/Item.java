package br.com.fiap.am.bean;

/**
 * Created by Luan Ianhes on 30/09/2017.
 */

public class Item {


    private int id = 0;
    private String nome;
    private String descricao;
    private String preco;
    private String quantidade;
    private String usuario;
    private String imagem;
    private int usuarioId;


    public Item(){}


    public Item(int id, String nome, String preco, String descricao, String quantidade, String imagem, int usuarioId, String usuario){
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.imagem = imagem;
        this.usuario = usuario;
        this.usuarioId = usuarioId;
    }



    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    //para testes sme o json
   @Override
    public String toString() {
        return nome +
                " Descrição: " + descricao +
                " Quantidade: " + quantidade +
                " Preco: " + preco;
    }
}
