/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import dao.DAOIngrediente;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Roberto
 */
public class Refeicao {
//    tempo limite para preparo da refeição

    private int id;

    public Refeicao() {
    }
    private int tempoMaximo;
    private int tempoTtl;
//    numero de pratos desejado para refeição
    private int componentes;
//    pratos selecionados para a refeição
    private List<Prato> pratos = new ArrayList<>();
    private int caloriaMax;
    private int caloriaTtl;
//    grupos de ingradiente que o usuario pode optar por exigir
    private List<Grupo> grupos;
    private DAOIngrediente daoing = dao.DAOFactory.criaDAOing();

    public Refeicao(int tempoMaximo, int componentes, List<Ingrediente> ingredientesDisponiveis) {
        this.tempoMaximo = tempoMaximo;
        this.componentes = componentes;
    }

    public Refeicao(int tempoMaximo, int componentes, int caloriaMax) {
        this.tempoMaximo = tempoMaximo;
        this.componentes = componentes;
        this.caloriaMax = caloriaMax;
    }

    public Refeicao(int tempoMaximo, int componentes, List<Ingrediente> dispensa, int caloriaMax, List<Grupo> grupos) {
        this.tempoMaximo = tempoMaximo;
        this.componentes = componentes;
        this.caloriaMax = caloriaMax;
        this.grupos = grupos;
    }

    public List<Prato> prepararRefeicao(List<Prato> todosPratos, List<Ingrediente> despensa) {
        this.pratos.clear();
        tempoTtl = 0;
        caloriaTtl = 0;
        List<Prato> PratosParaSorteio = new ArrayList<>(todosPratos);
        while (pratos.size() < componentes && !PratosParaSorteio.isEmpty()) {
            int pratosDisponiveis = PratosParaSorteio.size();
            int indice = ThreadLocalRandom.current().nextInt(pratosDisponiveis);
            Prato candidato = PratosParaSorteio.get(indice);
            if (!candidato.isPronto()) {
                boolean dentroDoTempo = (candidato.getTempo() + tempoTtl) <= tempoMaximo;
                boolean dentroDaCaloria;

                if (caloriaMax > 0) {
                    dentroDaCaloria = (candidato.getCalorias() + caloriaTtl) <= caloriaMax;
                } else {
                    dentroDaCaloria = true;
                }

                // O método que criamos anteriormente
                boolean temIngredientes = candidato.TodosIngredientesDisponiveis(despensa);

                if (dentroDaCaloria && dentroDoTempo && temIngredientes) {
                    // 1. Deduz os ingredientes da despensa antes de adicionar o prato
                    subtrairIngredientesDaDespensa(candidato, despensa);

                    // 2. Atualiza os totais da refeição
                    tempoTtl += candidato.getTempo();
                    caloriaTtl += candidato.getCalorias();
                    System.out.println(this.getCaloriaTtl());
                    // 3. Adiciona o prato
                    pratos.add(candidato);
                }
            } else {
                if (candidato.getCalorias() + this.caloriaTtl <= this.caloriaMax || this.caloriaMax == 0) {
                    pratos.add(candidato);
                    tempoTtl += 1;
                    caloriaTtl += candidato.getCalorias();
                }
            }
            PratosParaSorteio.remove(indice);
        }
        return pratos;
    }

    public void subtrairIngredientesDaDespensa(Prato prato, List<Ingrediente> despensa) {
        for (Ingrediente ingPrato : prato.getIngredientes()) {
            for (Ingrediente ingDespensa : despensa) {
                if (ingPrato.getId() == ingDespensa.getId()) {
                    double novaQuantidade = ingDespensa.getQuantidade() - ingPrato.getQuantidade();
                    ingDespensa.setQuantidade(novaQuantidade);
                    break;
                }
            }
        }
    }

    public List<Prato> mudarRefeicao(List<Prato> pratosParaSorteio, List<Ingrediente> despensa) {
        // 1. Reembolsa os ingredientes dos pratos atuais para a despensa
        reembolsarIngredientesParaDespensa(despensa);

        // 2. Limpa a lista de pratos atuais da refeição para o novo sorteio
        this.pratos.clear();

        // 3. Chama novamente o método prepararRefeicao com a lista de opções enviada
        return prepararRefeicao(pratosParaSorteio, despensa);
    }

// Método auxiliar privado para realizar o reembolso
    public void reembolsarIngredientesParaDespensa(List<Ingrediente> despensa) {
        if (this.pratos == null || this.pratos.isEmpty()) {
            return;
        }

        for (Prato prato : this.pratos) {
            // Só devolve os ingredientes se o prato NÃO estava pronto antes de entrar na refeição
            if (!prato.isPronto()) {
                for (Ingrediente ingPrato : prato.getIngredientes()) {
                    for (Ingrediente ingDespensa : despensa) {
                        if (ingPrato.getId() == ingDespensa.getId()) {
                            // Devolve (soma) a quantidade que o prato tinha gastado
                            double novaQuantidade = ingDespensa.getQuantidade() + ingPrato.getQuantidade();
                            ingDespensa.setQuantidade(novaQuantidade);
                            break; // Passa para o próximo ingrediente do prato
                        }
                    }
                }
            }
        }
    }

    public int getTempoMaximo() {
        return tempoMaximo;
    }

    public void setTempoMaximo(int tempoMaximo) {
        this.tempoMaximo = tempoMaximo;
    }

    public int getTempoTtl() {
        return tempoTtl;
    }

    public void setTempoTtl(int tempoTtl) {
        this.tempoTtl = tempoTtl;
    }

    public int getComponentes() {
        return componentes;
    }

    public void setComponentes(int componentes) {
        this.componentes = componentes;
    }

    public List<Prato> getPratos() {
        return pratos;
    }

    public void setPratos(List<Prato> pratos) {
        this.pratos = pratos;
    }

    public int getCaloriaMax() {
        return caloriaMax;
    }

    public void setCaloriaMax(int caloriaMax) {
        this.caloriaMax = caloriaMax;
    }

    public int getCaloriaTtl() {
        return caloriaTtl;
    }

    public void setCaloriaTtl(int caloriaTtl) {
        this.caloriaTtl = caloriaTtl;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
