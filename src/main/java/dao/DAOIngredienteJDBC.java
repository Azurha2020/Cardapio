/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Grupo;
import modelo.Ingrediente;
import modelo.Refeicao;

/**
 *
 * @author Roberto
 */
public class DAOIngredienteJDBC implements DAOIngrediente {

    @Override
    public int inserir(Ingrediente ingrediente) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO Ingredientes (nome, grupo, calorias, quantidade) ")
                .append("VALUES (?, ?, ?, ?)");
        String insert = sqlBuilder.toString();
        int linha = 0;
        try {
            linha = DAOgenerico.executarComando(
                    insert,
                    ingrediente.getNome(),
                    ingrediente.getGrupo().getId(),
                    ingrediente.getCaloria(),
                    ingrediente.getQuantidade()
            );
            System.out.println(linha + " alterou");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro " + e.getMessage(), e);
        }
        return linha;
    }

    @Override
    public int editar(Ingrediente ingrediente) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE Ingredientes ")
                .append("SET nome = ?, grupo = ?, calorias = ?, quantidade = ? ")
                .append("WHERE id = ?");
        String update = sqlBuilder.toString();
        int linha = 0;
        try {
            linha = DAOgenerico.executarComando(
                    update,
                    ingrediente.getNome(),
                    ingrediente.getGrupo().getId(),
                    ingrediente.getCaloria(),
                    ingrediente.getQuantidade(),
                    ingrediente.getId()
            );
            System.out.println(linha + " alterou");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro " + e.getMessage(), e);
        }
        return linha;
    }

    @Override
    public int apagar(int codigo) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("DELETE FROM Ingredientes ")
                .append("WHERE id = ?");
        String delete = sqlBuilder.toString();
        int linha = 0;
        try {
            linha = DAOgenerico.executarComando(delete, codigo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro " + e.getMessage(), e);
        }
        return linha;
    }

    @Override
    public List<Ingrediente> listar() {
        String select = "SELECT i.id, i.nome, i.calorias, i.quantidade,i.grupo, g.nome AS nome_grupo "
                + "FROM Ingredientes i "
                + "INNER JOIN Grupos g ON i.grupo = g.id";

        List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();

        try {
            ResultSet rset = DAOgenerico.executarConsulta(select);

            while (rset.next()) {
                Ingrediente ingrediente = new Ingrediente();
                ingrediente.setId(rset.getInt("id"));
                ingrediente.setNome(rset.getString("nome"));
                ingrediente.setCaloria(rset.getInt("calorias"));
                ingrediente.setQuantidade(rset.getDouble("quantidade"));

                // Montando o objeto Grupo associado
                Grupo grupo = new Grupo();
                grupo.setId(rset.getInt("grupo"));
                grupo.setNome(rset.getString("nome_grupo"));

                ingrediente.setGrupo(grupo);

                ingredientes.add(ingrediente);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro " + e.getMessage(), e);
        }

        return ingredientes;
    }

    public Ingrediente buscarUltimaOcorrenciaPorNome(DAOIngrediente daoIngrediente, String nomeBuscado) {
        List<Ingrediente> todosIngredientes = daoIngrediente.listar();
        Ingrediente ultimoEncontrado = null;
        for (Ingrediente ing : todosIngredientes) {
            if (nomeBuscado.equalsIgnoreCase(ing.getNome())) {
                ultimoEncontrado = ing;
            }
        }

        return ultimoEncontrado;
    }

    @Override
    public int retirarIngrediente(Ingrediente ingrediente) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE Ingredientes ")
                .append("SET nome = ?, grupo = ?, calorias = ?, quantidade = quantidade - ? ")
                .append("WHERE id = ?");
        String update = sqlBuilder.toString();
        int linha = 0;
        try {
            linha = DAOgenerico.executarComando(
                    update,
                    ingrediente.getNome(),
                    ingrediente.getGrupo().getId(),
                    ingrediente.getCaloria(),
                    ingrediente.getQuantidade(),
                    ingrediente.getId()
            );
            System.out.println(linha + " alterou");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro " + e.getMessage(), e);
        }
        return linha;
    }

    @Override
    public int devolverIngrediente(Ingrediente ingrediente) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE Ingredientes ")
                .append("SET nome = ?, grupo = ?, calorias = ?, quantidade = quantidade + ? ")
                .append("WHERE id = ?");
        String update = sqlBuilder.toString();
        int linha = 0;
        try {
            linha = DAOgenerico.executarComando(
                    update,
                    ingrediente.getNome(),
                    ingrediente.getGrupo().getId(),
                    ingrediente.getCaloria(),
                    ingrediente.getQuantidade(),
                    ingrediente.getId()
            );
            System.out.println(linha + " alterou");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro " + e.getMessage(), e);
        }
        return linha;
    }

    @Override
    public Ingrediente buscar(int id) {
        String select = "SELECT i.id, i.nome, i.calorias, i.quantidade, i.grupo, g.nome AS nome_grupo "
                + "FROM Ingredientes i "
                + "INNER JOIN Grupos g ON i.grupo = g.id "
                + "WHERE i.id = " + id;

        try {
            ResultSet rset = DAOgenerico.executarConsulta(select);

            if (rset.next()) {
                Ingrediente ingrediente = new Ingrediente();
                ingrediente.setId(rset.getInt("id"));
                ingrediente.setNome(rset.getString("nome"));
                ingrediente.setCaloria(rset.getInt("calorias"));
                ingrediente.setQuantidade(rset.getDouble("quantidade"));

                // Instancia e preenche o Grupo associado
                Grupo grupo = new Grupo();
                grupo.setId(rset.getInt("grupo"));
                grupo.setNome(rset.getString("nome_grupo"));

                ingrediente.setGrupo(grupo);

                return ingrediente; // Retorna o objeto preenchido
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar ingrediente: " + e.getMessage(), e);
        }

        return null; // Retorna null se não encontrar no banco
    }

}
