/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Grupo;
import modelo.Prato;
import modelo.Refeicao;

/**
 *
 * @author Roberto
 */
public class DAORefeicaoJDBC implements DAORefeicao {

    @Override
    public int inserir(Refeicao refeicao) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO Refeicoes (tempottl, tempomaximo, num_pratos, caloriattl, caloriamax) ")
                .append("VALUES (?, ?, ?, ?, ?)");
        String insertRefeicao = sqlBuilder.toString();
        int linha = 0;

        try {
            // 1. Insere o registro principal da refeição
            linha = DAOgenerico.executarComando(
                    insertRefeicao,
                    refeicao.getTempoTtl(),
                    refeicao.getTempoMaximo(),
                    refeicao.getComponentes(),
                    refeicao.getCaloriaTtl(),
                    refeicao.getCaloriaMax()
            );

            int idRefeicao = buscarUltimoId();

            // 2. Insere os relacionamentos com Pratos
            if (refeicao.getPratos() != null && !refeicao.getPratos().isEmpty()) {
                String insertPrato = "INSERT INTO Prato_Refeicao (refeicao, prato) VALUES (?, ?)";
                for (Prato p : refeicao.getPratos()) {
                    DAOgenerico.executarComando(insertPrato, idRefeicao, p.getId());
                }
            }

            // 3. Insere os relacionamentos com Grupos
            if (refeicao.getGrupos() != null && !refeicao.getGrupos().isEmpty()) {
                String insertGrupo = "INSERT INTO Grupo_Refeicao (refeicao, grupo) VALUES (?, ?)";
                for (Grupo g : refeicao.getGrupos()) {
                    DAOgenerico.executarComando(insertGrupo, idRefeicao, g.getId());
                }
            }

            System.out.println(linha + " alterou");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linha;
    }

    @Override
    public int editar(Refeicao refeicao) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE Refeicoes ")
                .append("SET tempottl = ?, tempomaximo = ?, num_pratos = ?, caloriattl = ?, caloriamax = ? ")
                .append("WHERE id = ?");
        String update = sqlBuilder.toString();
        int linha = 0;

        try {
            // 1. Atualiza a refeição
            linha = DAOgenerico.executarComando(
                    update,
                    refeicao.getTempoTtl(),
                    refeicao.getTempoMaximo(),
                    refeicao.getComponentes(),
                    refeicao.getCaloriaTtl(),
                    refeicao.getCaloriaMax(),
                    refeicao.getId()
            );

            // 2. Atualiza a lista de pratos (limpa velhos e insere novos)
            if (refeicao.getPratos() != null) {
                String deletePratos = "DELETE FROM Prato_Refeicao WHERE refeicao = ?";
                DAOgenerico.executarComando(deletePratos, refeicao.getId());

                String insertPrato = "INSERT INTO Prato_Refeicao (refeicao, prato) VALUES (?, ?)";
                for (Prato p : refeicao.getPratos()) {
                    DAOgenerico.executarComando(insertPrato, refeicao.getId(), p.getId());
                }
            }

            // 3. Atualiza a lista de grupos (limpa velhos e insere novos)
            if (refeicao.getGrupos() != null) {
                String deleteGrupos = "DELETE FROM Grupo_Refeicao WHERE refeicao = ?";
                DAOgenerico.executarComando(deleteGrupos, refeicao.getId());

                String insertGrupo = "INSERT INTO Grupo_Refeicao (refeicao, grupo) VALUES (?, ?)";
                for (Grupo g : refeicao.getGrupos()) {
                    DAOgenerico.executarComando(insertGrupo, refeicao.getId(), g.getId());
                }
            }

            System.out.println(linha + " alterou");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linha;
    }

    @Override
    public int apagar(int codigo) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("DELETE FROM Refeicoes WHERE id = ?");
        String delete = sqlBuilder.toString();
        int linha = 0;

        try {

            linha = DAOgenerico.executarComando(delete, codigo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linha;
    }

    @Override
    public List<Refeicao> listar() {
        String select = "SELECT * FROM Refeicoes";
        List<Refeicao> refeicoes = new ArrayList<>();

        try {
            ResultSet rset = DAOgenerico.executarConsulta(select);

            while (rset.next()) {
                Refeicao ref = new Refeicao();
                ref.setId(rset.getInt("id"));
                ref.setTempoTtl(rset.getInt("tempottl"));
                ref.setTempoMaximo(rset.getInt("tempomaximo"));
                ref.setComponentes(rset.getInt("num_pratos"));
                ref.setCaloriaTtl(rset.getInt("caloriattl"));
                ref.setCaloriaMax(rset.getInt("caloriamax"));

                // Carrega os objetos relacionados
                ref.setPratos(carregarPratosDaRefeicao(ref.getId()));
                ref.setGrupos(carregarGruposDaRefeicao(ref.getId()));

                refeicoes.add(ref);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return refeicoes;
    }

    // Método auxiliar para buscar os Pratos associados a esta refeição 
    private List<Prato> carregarPratosDaRefeicao(int idRefeicao) {
        List<Prato> pratos = new ArrayList<>();
        DAOPrato daoPrato = new DAOPratoJDBC();

        String sql = "SELECT prato FROM Prato_Refeicao WHERE refeicao = ?";

        try {
            ResultSet rset = DAOgenerico.executarConsulta(sql, idRefeicao);
            List<Prato> todosPratos = daoPrato.listar();

            while (rset.next()) {
                int idPratoBuscado = rset.getInt("prato");
                for (Prato p : todosPratos) {
                    if (p.getId() == idPratoBuscado) {
                        pratos.add(p);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pratos;
    }

    // Método auxiliar para buscar os Grupos exigidos nesta refeição
    private List<Grupo> carregarGruposDaRefeicao(int idRefeicao) {
        List<Grupo> grupos = new ArrayList<>();
        String sql = "SELECT g.id, g.nome FROM Grupos g "
                + "INNER JOIN Grupo_Refeicao gr ON g.id = gr.grupo "
                + "WHERE gr.refeicao = ?";

        try {
            ResultSet rset = DAOgenerico.executarConsulta(sql, idRefeicao);
            while (rset.next()) {
                Grupo g = new Grupo();
                g.setId(rset.getInt("id"));
                g.setNome(rset.getString("nome"));
                grupos.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grupos;
    }

    private int buscarUltimoId() {
        String sql = "SELECT MAX(id) AS ultimo_id FROM Refeicoes";
        try {
            ResultSet rset = DAOgenerico.executarConsulta(sql);
            if (rset.next()) {
                return rset.getInt("ultimo_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
