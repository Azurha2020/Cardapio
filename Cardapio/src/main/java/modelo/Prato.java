/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Roberto
 */
public class Prato {

    private int id;
    private String nome;
    private int tempo;
    private List<Ingrediente> ingredientes;
    private int calorias;
    int porcoes;
    private boolean pronto;

    public Prato(int id, String nome, int tempo, List<Ingrediente> ingredientes,int porcoes, Boolean Pronto) {
        this.id = id;
        this.nome = nome;
        this.tempo = tempo;
        this.ingredientes = ingredientes;
        calorias = 0;
        for (Ingrediente ingrediente : this.ingredientes) {
            int caloria = ingrediente.getCaloria();
            caloria *= ingrediente.getQuantidade();
            this.calorias = caloria;
        }
        this.porcoes=porcoes;
        boolean pronto;

    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
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

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public boolean TodosIngredientesDisponiveis(List<Ingrediente> despensa) {
        // Se a despensa estiver nula ou vazia, e o prato precisa de ingredientes, não dá para fazer
        if (despensa == null || despensa.isEmpty()) {
            return this.ingredientes == null || this.ingredientes.isEmpty();
        }

        // Passa por cada ingrediente que o prato necessita
        for (Ingrediente ingPrato : this.ingredientes) {
            boolean achouIngrediente = false;

            // Procura o ingrediente correspondente na despensa
            for (Ingrediente ingDespensa : despensa) {
                if (ingPrato.getId() == ingDespensa.getId()) {
                    achouIngrediente = true;

                    // Se achou, mas a quantidade na despensa for menor do que o prato precisa
                    if (ingDespensa.getQuantidade() < ingPrato.getQuantidade()) {
                        return false; // Quantidade insuficiente
                    }
                    break; // Achou e a quantidade é suficiente, pode parar de procurar este item
                }
            }

            // Se rodou a despensa inteira e não achou o ingrediente requisitado
            if (!achouIngrediente) {
                return false;
            }
        }

        // Se passou por todos os filtros, significa que a despensa tem tudo o que o prato precisa
        return true;
    }

    public int getCalorias() {
        return calorias;
    }

    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }
//    metodo que permite levar em conta que alguns pratos pondem já estar feitos

    public boolean isPronto() {
        return pronto;
    }

    public void setPronto(boolean pronto) {
        this.pronto = pronto;
    }
}
