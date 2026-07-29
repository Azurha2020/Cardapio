/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Roberto
 */
public class Ingrediente {
    private int id;
    private String nome;
    private Grupo grupo;
    private int caloria;
    private double quantidade;

    public Ingrediente(int id, String nome, Grupo grupo, int caloria, double quantidade) {
        this.id = id;
        this.nome = nome;
        this.grupo = grupo;
        this.caloria = caloria;
        this.quantidade = quantidade;
    }

    public Ingrediente() {
       
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
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

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public int getCaloria() {
        return caloria;
    }

    public void setCaloria(int caloria) {
        this.caloria = caloria;
    }
    
}
