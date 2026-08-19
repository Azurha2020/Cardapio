/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import modelo.Ingrediente;
import modelo.Refeicao;

/**
 *
 * @author Roberto
 */
public interface DAOIngrediente {
      public int inserir(Ingrediente ingrediente);
    public int editar(Ingrediente ingrediente);
    public int apagar(int codigo);
    public List<Ingrediente> listar();
    public Ingrediente buscarUltimaOcorrenciaPorNome(DAOIngrediente daoIngrediente, String nome);
    public int retirarIngrediente(Ingrediente ingrediente);
    public int devolverIngrediente(Ingrediente ingrediente);
    public Ingrediente buscar(int id);
}
