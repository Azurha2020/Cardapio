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
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeRefeicoes {
    private List<Refeicao> refeicoes;
    private List<Ingrediente> despensa;
    private List<Prato> todosPratos;

    public GerenciadorDeRefeicoes() {
        this.refeicoes = new ArrayList<>();
        this.despensa = new ArrayList<>();
        this.todosPratos = new ArrayList<>();
    }

    public void adicionarRefeicao(Refeicao refeicao) {
        if (refeicao != null) {
            this.refeicoes.add(refeicao);
        }
    }

    public void removerRefeicao(Refeicao refeicao) {
        this.refeicoes.remove(refeicao);
    }

    public void editarRefeicao(int index, Refeicao novaRefeicao) {
        if (index >= 0 && index < refeicoes.size() && novaRefeicao != null) {
            this.refeicoes.set(index, novaRefeicao);
        } else {
            System.out.println("Índice inválido ou refeição nula.");
        }
    }

    public void adicionarIngrediente(Ingrediente ingrediente) {
        if (ingrediente != null) {
            this.despensa.add(ingrediente);
        }
    }

    public void removerIngrediente(Ingrediente ingrediente) {
        this.despensa.remove(ingrediente);
    }

    public void editarIngrediente(int index, Ingrediente novoIngrediente) {
        if (index >= 0 && index < despensa.size() && novoIngrediente != null) {
            this.despensa.set(index, novoIngrediente);
        } else {
            System.out.println("Índice inválido ou ingrediente nulo.");
        }
    }

    public void adicionarPrato(Prato prato) {
        if (prato != null) {
            this.todosPratos.add(prato);
        }
    }

    public void removerPrato(Prato prato) {
        this.todosPratos.remove(prato);
    }

    public void editarPrato(int index, Prato novoPrato) {
        if (index >= 0 && index < todosPratos.size() && novoPrato != null) {
            this.todosPratos.set(index, novoPrato);
        } else {
            System.out.println("Índice inválido ou prato nulo.");
        }
    }
    public List<Refeicao> getRefeicoes() {
        return refeicoes;
    }

    public List<Ingrediente> getDespensa() {
        return despensa;
    }

    public List<Prato> getTodosPratos() {
        return todosPratos;
    }
}