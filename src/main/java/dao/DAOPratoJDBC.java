package dao;


import dao.DAOgenerico;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Grupo;
import modelo.Ingrediente;
import modelo.Prato;
public class DAOPratoJDBC implements DAOPrato {

    @Override
    public int inserir(Prato prato) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO Pratos (nome, preparo, tempo, calorias, porcoes, pronto) ")
                .append("VALUES (?, ?, ?, ?, ?, ?)");
        String insertPrato = sqlBuilder.toString();
        int linha = 0;

        try {
           
            linha = DAOgenerico.executarComando(
                    insertPrato,
                    prato.getNome(),
                    prato.getPreparo(),
                    prato.getTempo(),
                    prato.getCalorias(),
                    prato.getPorcoes(),
                    prato.isPronto()
            );

            // Recupera o ID do prato recém-inserido para salvar a lista de ingredientes na tabela associativa
            if (prato.getIngredientes() != null && !prato.getIngredientes().isEmpty()) {
                int idPrato = buscarUltimoId();

                for (Ingrediente ing : prato.getIngredientes()) {
                    String insertItem = "INSERT INTO Ingrediente_Prato (prato, ingrediente, quantidade) VALUES (?, ?, ?)";
                    DAOgenerico.executarComando(insertItem, idPrato, ing.getId(), ing.getQuantidade());
                }
            }

            System.out.println(linha + " alterou");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir prato: " + e.getMessage(), e);
        }
        return linha;
    }

    @Override
    public int editar(Prato prato) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE Pratos ")
                .append("SET nome = ?, preparo = ?, tempo = ?, calorias = ?, porcoes = ?, pronto = ? ")
                .append("WHERE id = ?");
        String update = sqlBuilder.toString();
        int linha = 0;

        try {
            // Atualiza os dados básicos do prato incluindo 'preparo'
            linha = DAOgenerico.executarComando(
                    update,
                    prato.getNome(),
                    prato.getPreparo(),
                    prato.getTempo(),
                    prato.getCalorias(),
                    prato.getPorcoes(),
                    prato.isPronto(),
                    prato.getId()
            );

            // Atualiza o relacionamento: limpa as associações antigas e insere as novas
            if (prato.getIngredientes() != null) {
                String deleteItens = "DELETE FROM Ingrediente_Prato WHERE prato = ?";
                DAOgenerico.executarComando(deleteItens, prato.getId());

                for (Ingrediente ing : prato.getIngredientes()) {
                    String insertItem = "INSERT INTO Ingrediente_Prato (prato, ingrediente, quantidade) VALUES (?, ?, ?)";
                    DAOgenerico.executarComando(insertItem, prato.getId(), ing.getId(), ing.getQuantidade());
                }
            }

            System.out.println(linha + " alterou");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao editar prato: " + e.getMessage(), e);
        }
        return linha;
    }

    @Override
    public int apagar(int codigo) {
        int linha = 0;
        try {
            // 1. Remove primeiro as dependências da tabela associativa
            String deleteItens = "DELETE FROM Ingrediente_Prato WHERE prato = ?";
            DAOgenerico.executarComando(deleteItens, codigo);

            // 2. Remove o prato principal
            String deletePrato = "DELETE FROM Pratos WHERE id = ?";
            linha = DAOgenerico.executarComando(deletePrato, codigo);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao apagar prato: " + e.getMessage(), e);
        }
        return linha;
    }

    @Override
    public List<Prato> listar() {
        String select = "SELECT * FROM Pratos";
        List<Prato> pratos = new ArrayList<>();

        try {
            ResultSet rset = DAOgenerico.executarConsulta(select);

            while (rset.next()) {
                Prato prato = new Prato();
                prato.setId(rset.getInt("id"));
                prato.setNome(rset.getString("nome"));
                prato.setPreparo(rset.getString("preparo"));
                prato.setTempo(rset.getInt("tempo"));
                prato.setCalorias(rset.getInt("calorias"));
                prato.setPorcoes(rset.getInt("porcoes"));
                prato.setPronto(rset.getBoolean("pronto"));

                // Busca a lista de ingredientes do prato
                prato.setIngredientes(carregarIngredientesDoPrato(prato.getId()));

                pratos.add(prato);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar pratos: " + e.getMessage(), e);
        }

        return pratos;
    }

    private List<Ingrediente> carregarIngredientesDoPrato(int idPrato) {
        List<Ingrediente> ingredientes = new ArrayList<>();

        String sql = "SELECT i.id, i.nome, i.calorias, ip.quantidade AS quantidade_no_prato, i.grupo, g.nome AS nome_grupo " +
                     "FROM Ingredientes i " +
                     "INNER JOIN Ingrediente_Prato ip ON i.id = ip.ingrediente " +
                     "INNER JOIN Grupos g ON i.grupo = g.id " +
                     "WHERE ip.prato = ?";

        try {
            ResultSet rset = DAOgenerico.executarConsulta(sql, idPrato);
            while (rset.next()) {
                Ingrediente ing = new Ingrediente();
                ing.setId(rset.getInt("id"));
                ing.setNome(rset.getString("nome"));
                ing.setCaloria(rset.getInt("calorias"));
                ing.setQuantidade(rset.getDouble("quantidade_no_prato"));

                Grupo g = new Grupo();
                g.setId(rset.getInt("grupo"));
                g.setNome(rset.getString("nome_grupo"));
                ing.setGrupo(g);

                ingredientes.add(ing);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao carregar ingredientes do prato: " + e.getMessage(), e);
        }
        return ingredientes;
    }

    private int buscarUltimoId() {
        String sql = "SELECT MAX(id) AS ultimo_id FROM Pratos";
        try {
            ResultSet rset = DAOgenerico.executarConsulta(sql);
            if (rset.next()) {
                return rset.getInt("ultimo_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar último ID: " + e.getMessage(), e);
        }
        return 0;
    }
}