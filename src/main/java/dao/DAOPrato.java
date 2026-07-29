package dao;


import java.util.List;
import modelo.Prato;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author Roberto
 */
public interface DAOPrato {
    public int inserir(Prato prato);
    public int editar(Prato prato);
    public int apagar(int codigo);
    public List<Prato> listar();
}
