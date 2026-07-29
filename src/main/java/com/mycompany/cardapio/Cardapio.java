/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.cardapio;

import dao.DAOGrupo;
import dao.DAOGrupoJDBC;
import dao.DAOIngrediente;
import dao.DAOIngredienteJDBC;
import dao.DAOPratoJDBC;
import dao.DAOPrato;
import dao.DAORefeicao;
import dao.DAORefeicaoJDBC;
import java.util.ArrayList;
import java.util.List;
import modelo.Grupo;
import modelo.Ingrediente;
import modelo.Prato;
import modelo.Refeicao;

public class Cardapio {
    
 
    public static void main(String[] args) {
        DAOGrupo daoGrupo = new DAOGrupoJDBC();
        DAOIngrediente daoIngrediente = new DAOIngredienteJDBC();
        DAOPrato daoPrato = new DAOPratoJDBC();
        DAORefeicao daoRefeicao=new DAORefeicaoJDBC();
        System.out.println("=== 1. RECUPERANDO PRATOS E GRUPOS DO BANCO ===");
        List<Prato> pratosDisponiveis = daoPrato.listar();
        List<Grupo> gruposDisponiveis = daoGrupo.listar();

        System.out.println("Pratos encontrados: " + pratosDisponiveis.size());
        System.out.println("Grupos encontrados: " + gruposDisponiveis.size());

        System.out.println("\n=== 2. CRIANDO E INSERINDO REFEIÇÃO ===");
        Refeicao ref1 = new Refeicao();
        ref1.setTempoMaximo(100);
        ref1.setTempoTtl(35);
        ref1.setComponentes(2);
        ref1.setCaloriaMax(600);
        ref1.setCaloriaTtl(490);

        // Associa os pratos existentes
        List<Prato> pratosRef1 = new ArrayList<>();
        if (!pratosDisponiveis.isEmpty()) {
            pratosRef1.add(pratosDisponiveis.get(0)); // Adiciona primeiro prato
        }
        ref1.setPratos(pratosRef1);

        // Associa um grupo exigido (Ex: Proteínas)
        List<Grupo> gruposRef1 = new ArrayList<>();
        if (!gruposDisponiveis.isEmpty()) {
            gruposRef1.add(gruposDisponiveis.get(0));
        }
        ref1.setGrupos(gruposRef1);

        daoRefeicao.inserir(ref1);


        System.out.println("\n=== 3. EDITANDO A REFEIÇÃO ===");
        List<Refeicao> refeicoesBanco = daoRefeicao.listar();
        if (!refeicoesBanco.isEmpty()) {
            Refeicao refeicaoParaEditar = refeicoesBanco.get(0);

            // Atualiza calorias e insere o segundo prato se existir
            refeicaoParaEditar.setCaloriaTtl(550);
            if (pratosDisponiveis.size() > 1) {
                refeicaoParaEditar.getPratos().add(pratosDisponiveis.get(1));
            }

            daoRefeicao.editar(refeicaoParaEditar);
        }


        System.out.println("\n=== 4. LISTANDO REFEIÇÕES COMPLETAS ===");
        List<Refeicao> listaFinal = daoRefeicao.listar();

        for (Refeicao r : listaFinal) {
            System.out.println("ID Refeicao: " + r.getId());
            System.out.println("Tempo Total: " + r.getTempoTtl() + " min (Max: " + r.getTempoMaximo() + " min)");
            System.out.println("Componentes (Pratos): " + r.getComponentes());
            System.out.println("Calorias Totais: " + r.getCaloriaTtl() + " kcal (Max: " + r.getCaloriaMax() + " kcal)");

            System.out.println("Grupos Exigidos:");
            if (r.getGrupos() != null && !r.getGrupos().isEmpty()) {
                for (Grupo g : r.getGrupos()) {
                    System.out.println("  - " + g.getNome());
                }
            } else {
                System.out.println("  (Nenhum grupo especificado)");
            }

            System.out.println("Pratos da Refeicao:");
            if (r.getPratos() != null && !r.getPratos().isEmpty()) {
                for (Prato p : r.getPratos()) {
                    System.out.println("  - " + p.getNome() + " (" + p.getCalorias() + " kcal)");
                }
            } else {
                System.out.println("  (Nenhum prato associado)");
            }
            System.out.println("--------------------------------------------------");
        }
    }
}
