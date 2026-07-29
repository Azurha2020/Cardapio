/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import modelo.Grupo;

/**
 *
 * @author Roberto
 */
public interface DAOGrupo {
    public int inserir(Grupo grupo);
    public int editar(Grupo grupo);
    public int apagar(int codigo);
    public List<Grupo> listar();
}
