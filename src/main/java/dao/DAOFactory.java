/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Azurha
 */
public class DAOFactory {
    public static DAOGrupo criaDAOgrupo(){
        return new DAOGrupoJDBC();
    } 
    public static DAOIngrediente criaDAOing(){
        return new DAOIngredienteJDBC();
    }
    public static DAOPrato criaDAOprato(){
        return new DAOPratoJDBC();
    }
    public static DAORefeicao criaDAOref(){
        return new DAORefeicaoJDBC();
    }
}
