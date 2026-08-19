/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package aplicacao;

import dao.DAOGrupo;
import dao.DAOIngrediente;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import modelo.Grupo;
import modelo.Ingrediente;

/**
 *
 * @author Roberto
 */
public class frmNovoIngrediente extends javax.swing.JFrame {

    // Instância do DAO de Grupo
    private DAOGrupo daoGrupo = dao.DAOFactory.criaDAOgrupo();
    DAOIngrediente daoIngrediente = dao.DAOFactory.criaDAOing(); // Ou new DAOIngredienteJDBC()
    private Ingrediente ingrediente = null;

    /**
     * Creates new form frmNovoIngrediente
     */
    public frmNovoIngrediente() {
        initComponents();
        carregarGrupos();
    }
    // Construtor usado para EDIÇÃO

    public frmNovoIngrediente(Ingrediente ingrediente) {
        this(); // Chama o construtor padrão para inicializar os componentes e a combo
        if (ingrediente != null) {
            this.ingrediente = ingrediente;
            preencherCampos();
        }
    }

    private void carregarGrupos() {
        try {
            List<Grupo> lista = daoGrupo.listar();

            // Crie o modelo sem restrição de tipo
            DefaultComboBoxModel model = new DefaultComboBoxModel();

            for (Grupo grupo : lista) {
                model.addElement(grupo); // Aceita o objeto Grupo normalmente
            }

            comboGrupos.setModel(model); // Aplica o modelo à JComboBox

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar grupos: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCampos() {
        nomeField.setText(this.ingrediente.getNome());
        qtnField.setText(String.valueOf(this.ingrediente.getQuantidade()));
        caloriaField.setText(String.valueOf(this.ingrediente.getCaloria()));

        // Seleciona o Grupo correto no ComboBox comparando pelo ID
        if (this.ingrediente.getGrupo() != null) {
            javax.swing.DefaultComboBoxModel model = (javax.swing.DefaultComboBoxModel) comboGrupos.getModel();

            for (int i = 0; i < model.getSize(); i++) {
                Grupo g = (Grupo) model.getElementAt(i); // Retorna Object diretamente

                if (g != null && g.getId() == this.ingrediente.getGrupo().getId()) {
                    comboGrupos.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void inserir() {
        try {
            // 1. Validação de campos vazios
            if (nomeField.getText().trim().isEmpty() || qtnField.getText().trim().isEmpty() || caloriaField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Preencha o Nome e a Quantidade do ingrediente.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return ;
            }

            // 2. Recupera o Grupo selecionado na ComboBox
            Grupo grupoSelecionado = (Grupo) comboGrupos.getSelectedItem();
            if (grupoSelecionado == null) {
                JOptionPane.showMessageDialog(this,
                        "Selecione um grupo válido.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return ;
            }

            // 3. Conversão de tipos
            String nome = nomeField.getText().trim();
            double quantidade = Double.parseDouble(qtnField.getText().trim());
            int calorias = Integer.parseInt(caloriaField.getText().trim());

            // Exemplo: int calorias = Integer.parseInt(caloriaField.getText().trim());
            // 4. Criação do objeto
            Ingrediente ingrediente = new Ingrediente();
            ingrediente.setNome(nome);
            ingrediente.setGrupo(grupoSelecionado);
            ingrediente.setQuantidade(quantidade);
            ingrediente.setCaloria(calorias);

            // 5. Execução do INSERT no banco
            int resultado = daoIngrediente.inserir(ingrediente);

            if (resultado > 0) {
                JOptionPane.showMessageDialog(this,
                        "Ingrediente salvo com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                new frmPrincipal().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar no banco.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return ;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "O campo quantidade deve conter um valor numérico válido.",
                    "Erro de Digitação", JOptionPane.ERROR_MESSAGE);
            return ;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro inesperado: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return ;
        }
    }
    private void editarIngrediente() {
    try {
        if (nomeField.getText().trim().isEmpty() || qtnField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Preencha o Nome e a Quantidade do ingrediente.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Grupo grupoSelecionado = (Grupo) comboGrupos.getSelectedItem();
        if (grupoSelecionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um grupo válido.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Atualiza os dados no objeto existente mantendo o ID original
        this.ingrediente.setNome(nomeField.getText().trim());
        this.ingrediente.setQuantidade(Double.parseDouble(qtnField.getText().trim()));
        this.ingrediente.setGrupo(grupoSelecionado);

        // Executa o UPDATE via DAO
        DAOIngrediente daoIng = dao.DAOFactory.criaDAOing();
        int resultado = daoIng.editar(this.ingrediente);

        if (resultado > 0) {
            JOptionPane.showMessageDialog(this, 
                "Ingrediente atualizado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            new frmPrincipal().setVisible(true);
                this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar o ingrediente.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, 
            "O campo quantidade deve conter um valor numérico válido.", 
            "Erro de Digitação", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Erro ao editar: " + e.getMessage(), 
            "Erro", JOptionPane.ERROR_MESSAGE);
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nomeField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        qtnField = new javax.swing.JTextField();
        comboGrupos = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        btnAceitar = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        caloriaField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nome: ");

        nomeField.setColumns(15);

        jLabel2.setText("Quantidade:");

        qtnField.setColumns(8);

        comboGrupos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Grupo: ");

        btnAceitar.setText("Aceitar");
        btnAceitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceitarActionPerformed(evt);
            }
        });

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        jLabel4.setText("Calorias Por porção:");

        caloriaField.setColumns(10);
        caloriaField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                caloriaFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnVoltar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAceitar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(nomeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(comboGrupos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(qtnField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(caloriaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 127, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nomeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(qtnField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(caloriaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboGrupos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 342, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceitar)
                    .addComponent(btnVoltar))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        new frmPrincipal().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnAceitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceitarActionPerformed
        if (ingrediente != null) {
            editarIngrediente();
        } else {
            inserir();
           
        }
    }//GEN-LAST:event_btnAceitarActionPerformed

    private void caloriaFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_caloriaFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_caloriaFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmNovoIngrediente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmNovoIngrediente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmNovoIngrediente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmNovoIngrediente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmNovoIngrediente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceitar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JTextField caloriaField;
    private javax.swing.JComboBox<String> comboGrupos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nomeField;
    private javax.swing.JTextField qtnField;
    // End of variables declaration//GEN-END:variables
}
