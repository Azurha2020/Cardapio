package dao;


import java.util.List;
import modelo.Refeicao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author Roberto
 */
public interface DAORefeicao {
    public int inserir(Refeicao refeicao);
    public int editar(Refeicao refeicao);
    public int apagar(int codigo);
    public List<Refeicao> listar();
}
