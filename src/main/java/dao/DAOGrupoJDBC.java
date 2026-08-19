/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Grupo;

/**
 *
 * @author Roberto
 */
public class DAOGrupoJDBC implements DAOGrupo{

    @Override
    public int inserir(Grupo grupo) {
        StringBuilder sqlBuilder= new StringBuilder();
        sqlBuilder.append("Insert into Grupos(nome) ").append("Values (?)");
        String insert=sqlBuilder.toString();
        int linha=0;
        try {
            linha=DAOgenerico.executarComando(insert, grupo.getNome());
            System.out.println(linha+" alterou");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro " + e.getMessage(), e);
        }
        return linha;
    }

    @Override
    public int editar(Grupo grupo) {
        StringBuilder sqlBuilder= new StringBuilder();
        sqlBuilder.append("update Grupos ").append(" set nome= ?").append("where id=?");
        String update=sqlBuilder.toString();
        int linha=0;
        try {
            linha=DAOgenerico.executarComando(update,grupo.getNome(), grupo.getId());
            System.out.println(linha+" alterou");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro " + e.getMessage(), e);
        }
        return linha;
    }

    @Override
    public int apagar(int codigo) {
        StringBuilder sqlBuilder= new StringBuilder();
        sqlBuilder.append("Delete from Grupos ").append("where id=?");
        String insert=sqlBuilder.toString();
        int linha=0;
        try {
            linha=DAOgenerico.executarComando(insert, codigo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro " + e.getMessage(), e);
        }
        return linha;
    }

    @Override
    public List<Grupo> listar() {
           String select = "SELECT * FROM Grupos";

        List<Grupo> grupos = new ArrayList<Grupo>();

        try {        
            ResultSet rset = DAOgenerico.executarConsulta(select);


            while (rset.next()) {

                Grupo grupo = new Grupo();
                grupo.setId(rset.getInt("id"));
                grupo.setNome(rset.getString("nome"));

                grupos.add(grupo);

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro " + e.getMessage(), e);
        }

        return grupos;
    
    
    }
    
}
