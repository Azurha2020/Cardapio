package dao;


import dao.DAOgenerico;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Grupo;
import modelo.Ingrediente;
import modelo.Prato;

public class DAOPratoJDBC implements DAOPrato { // Adapte o nome da interface se houver

    @Override
    public int inserir(Prato prato) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO Pratos (nome, tempo, calorias, porcoes, pronto) ")
                .append("VALUES (?, ?, ?, ?, ?)");
        String insertPrato = sqlBuilder.toString();
        int linha = 0;

        try {
            // Insere o prato principal
            linha = DAOgenerico.executarComando(
                    insertPrato,
                    prato.getNome(),
                    prato.getTempo(),
                    prato.getCalorias(),
                    prato.getPorcoes(),
                    prato.isPronto()
            );

            // Recupera o ID do prato recém-inserido para salvar a lista de ingredientes na tabela associativa
            // Assumindo que a lista de ingredientes vem preenchida no objeto
            if (prato.getIngredientes() != null && !prato.getIngredientes().isEmpty()) {
                // Busca o ID do prato recém-criado (caso o DAOgenerico não retorne a chave gerada)
                int idPrato = buscarUltimoId();

                for (Ingrediente ing : prato.getIngredientes()) {
                    String insertItem = "INSERT INTO Ingrediente_Prato (prato, ingrediente) VALUES (?, ?)";
                    DAOgenerico.executarComando(insertItem, idPrato, ing.getId());
                }
            }

            System.out.println(linha + " alterou");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linha;
    }

    @Override
    public int editar(Prato prato) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE Pratos ")
                .append("SET nome = ?, tempo = ?, calorias = ?, porcoes = ?, pronto = ? ")
                .append("WHERE id = ?");
        String update = sqlBuilder.toString();
        int linha = 0;

        try {
            // Atualiza os dados básicos do prato
            linha = DAOgenerico.executarComando(
                    update,
                    prato.getNome(),
                    prato.getTempo(),
                    prato.getCalorias(),
                    prato.getPorcoes(),
                    prato.isPronto(),
                    prato.getId()
            );

            // Atualiza o relacionamento: limpa as associações antigas e insere as novas
            if (prato.getIngredientes() != null) {
                String deleteItens = "DELETE FROM ingrediente_prato WHERE prato = ?";
                DAOgenerico.executarComando(deleteItens, prato.getId());

                for (Ingrediente ing : prato.getIngredientes()) {
                    String insertItem = "INSERT INTO ingrediente_prato (prato, ingrediente) VALUES (?, ?)";
                    DAOgenerico.executarComando(insertItem, prato.getId(), ing.getId());
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
        sqlBuilder.append("DELETE FROM Pratos ").append("WHERE id = ?");
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
    public List<Prato> listar() {
        String select = "SELECT * FROM Pratos";
        List<Prato> pratos = new ArrayList<Prato>();

        try {
            ResultSet rset = DAOgenerico.executarConsulta(select);

            while (rset.next()) {
                Prato prato = new Prato();
                prato.setId(rset.getInt("id"));
                prato.setNome(rset.getString("nome"));
                prato.setTempo(rset.getInt("tempo"));
                prato.setCalorias(rset.getInt("calorias"));
                prato.setPorcoes(rset.getInt("porcoes"));
                prato.setPronto(rset.getBoolean("pronto"));

                // Busca a lista de ingredientes pertencentes a este prato
                prato.setIngredientes(carregarIngredientesDoPrato(prato.getId()));

                pratos.add(prato);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pratos;
    }

    // Método auxiliar para buscar os ingredientes vinculados a um prato específico
    private List<Ingrediente> carregarIngredientesDoPrato(int idPrato) {
    List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
    
    String sql = "SELECT i.id, i.nome, i.calorias, i.quantidade, i.grupo, g.nome AS nome_grupo " +
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
            ing.setQuantidade(rset.getDouble("quantidade"));

            Grupo g = new Grupo();
            g.setId(rset.getInt("grupo"));
            g.setNome(rset.getString("nome_grupo"));
            ing.setGrupo(g);

            ingredientes.add(ing);
        }
    } catch (Exception e) {
        e.printStackTrace();
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
        }
        return 0;
    }
}
