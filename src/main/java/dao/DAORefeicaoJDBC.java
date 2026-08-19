/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Grupo;
import modelo.Ingrediente;
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

            // 4. Agrupa e subtrai do estoque a quantidade total consumida de cada ingrediente
            if (refeicao.getPratos() != null && !refeicao.getPratos().isEmpty()) {
                Map<Integer, Double> consumoPorIngrediente = new HashMap<>();

                // Agrupa os ingredientes apenas dos pratos que NÃO estão prontos
                for (Prato p : refeicao.getPratos()) {
                    if (!p.isPronto() && p.getIngredientes() != null) {
                        for (Ingrediente ing : p.getIngredientes()) {
                            consumoPorIngrediente.put(
                                    ing.getId(),
                                    consumoPorIngrediente.getOrDefault(ing.getId(), 0.0) + ing.getQuantidade()
                            );
                        }
                    }
                }

                // Prepara a instrução SQL antes do laço para reaproveitamento
                String updateEstoque = "UPDATE Ingredientes SET quantidade = quantidade - ? WHERE id = ?";

                // Executa a atualização acumulada para cada ingrediente
                for (Map.Entry<Integer, Double> entry : consumoPorIngrediente.entrySet()) {
                    DAOgenerico.executarComando(updateEstoque, entry.getValue(), entry.getKey());
                }
            }

            System.out.println(linha + " alterou");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro " + e.getMessage(), e);
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
            String sql = "DELETE FROM Prato_Refeicao WHERE refeicao = ?";
            DAOgenerico.executarComando(sql, refeicao.getId());
            DAOPrato dAOPrato = DAOFactory.criaDAOprato();
            String insertPrato = "INSERT INTO Prato_Refeicao (refeicao, prato) VALUES (?, ?)";
            for (Prato prato : refeicao.getPratos()) {
                DAOgenerico.executarComando(insertPrato, refeicao.getId(), prato.getId());
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

        // 4. Agrupa e subtrai do estoque os ingredientes dos pratos não prontos
        if (refeicao.getPratos() != null && !refeicao.getPratos().isEmpty()) {
            Map<Integer, Double> consumoPorIngrediente = new HashMap<>();

            for (Prato p : refeicao.getPratos()) {
                if (!p.isPronto() && p.getIngredientes() != null) {
                    for (Ingrediente ing : p.getIngredientes()) {
                        consumoPorIngrediente.put(
                            ing.getId(),
                            consumoPorIngrediente.getOrDefault(ing.getId(), 0.0) + ing.getQuantidade()
                        );
                    }
                }
            }

            String updateEstoque = "UPDATE Ingredientes SET quantidade = quantidade - ? WHERE id = ?";

            for (Map.Entry<Integer, Double> entry : consumoPorIngrediente.entrySet()) {
                DAOgenerico.executarComando(updateEstoque, entry.getValue(), entry.getKey());
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Erro " + e.getMessage(), e);
    }
    return linha;
}
   @Override
public int apagar(int codigo) {
    int linha = 0;

    try {
        // 1. Mapeia e acumula os ingredientes dos pratos NÃO PRONTOS vinculados a esta refeição
        String sqlIngredientes = "SELECT ip.ingrediente, ip.quantidade " +
                                 "FROM Prato_Refeicao pr " +
                                 "JOIN Pratos p ON pr.prato = p.id " +
                                 "JOIN Ingrediente_Prato ip ON p.id = ip.prato " +
                                 "WHERE pr.refeicao = ? AND p.pronto = false";

        Map<Integer, Double> devolucaoPorIngrediente = new HashMap<>();

        ResultSet rset = DAOgenerico.executarConsulta(sqlIngredientes, codigo);
        while (rset.next()) {
            int idIngrediente = rset.getInt("ingrediente");
            double quantidade = rset.getDouble("quantidade");

            devolucaoPorIngrediente.put(
                idIngrediente,
                devolucaoPorIngrediente.getOrDefault(idIngrediente, 0.0) + quantidade
            );
        }

        // 2. Devolve (soma) as quantidades acumuladas de volta ao estoque de ingredientes
        if (!devolucaoPorIngrediente.isEmpty()) {
            String updateEstoque = "UPDATE Ingredientes SET quantidade = quantidade + ? WHERE id = ?";

            for (Map.Entry<Integer, Double> entry : devolucaoPorIngrediente.entrySet()) {
                DAOgenerico.executarComando(updateEstoque, entry.getValue(), entry.getKey());
            }
        }

        // 3. Remove os relacionamentos nas tabelas associativas
        DAOgenerico.executarComando("DELETE FROM Prato_Refeicao WHERE refeicao = ?", codigo);
        DAOgenerico.executarComando("DELETE FROM Grupo_Refeicao WHERE refeicao = ?", codigo);

        // 4. Remove a refeição
        linha = DAOgenerico.executarComando("DELETE FROM Refeicoes WHERE id = ?", codigo);

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Erro ao apagar refeição: " + e.getMessage(), e);
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
            throw new RuntimeException("Erro " + e.getMessage(), e);
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
            throw new RuntimeException("Erro " + e.getMessage(), e);
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
            throw new RuntimeException("Erro " + e.getMessage(), e);
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
            throw new RuntimeException("Erro " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public Refeicao buscar(int id) {
        // 1. Filtra a consulta pelo ID passado por parâmetro
        String select = "SELECT * FROM Refeicoes WHERE id = " + id;

        try {
            ResultSet rset = DAOgenerico.executarConsulta(select);
            if (rset.next()) {
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

                return ref; // 3. Retorna a refeição encontrada
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro: " + e.getMessage(), e);
        }

        return null; // Retorna null apenas se o registro NÃO existir no banco
    }
}
