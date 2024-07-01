import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class ConsultaClienteGUI extends JFrame {
    private JFrame menuInicial;
    private List<Cliente> clientes; // Lista de clientes carregada do arquivo
    private JTextField pesquisaField;
    private JPanel resultadosPanel;
    private JTextField nomeField;
    private JTextField telefoneField;
    private JTextField emailField;
    private Cliente clienteAtual; // Cliente atualmente selecionado para edição

    public ConsultaClienteGUI(JFrame menuInicial) {
        this.menuInicial = menuInicial; // Referência para o menu inicial
        setTitle("Consulta e Atualização de Clientes");
        setSize(600, 400);
        setLocationRelativeTo(menuInicial);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Carregar clientes do arquivo
        try {
            clientes = ClienteReader.lerClientesDeTxt("clientes.txt");
        } catch (IOException ex) {
            clientes = null;
            JOptionPane.showMessageDialog(this, "Erro ao ler clientes do arquivo.");
            ex.printStackTrace();
        }

        resultadosPanel = new JPanel();
        resultadosPanel.setLayout(new BoxLayout(resultadosPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultadosPanel);

        pesquisaField = new JTextField();
        JButton pesquisarButton = new JButton("Pesquisar");
        JButton salvarButton = new JButton("Salvar Alterações");
        JButton voltarMenuButton = new JButton("Voltar ao Menu");

        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pesquisa = pesquisaField.getText().trim();
                if (pesquisa.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira um nome para pesquisar.");
                } else {
                    resultadosPanel.removeAll();
                    for (Cliente cliente : clientes) {
                        if (cliente.getNome().toLowerCase().contains(pesquisa.toLowerCase())) {
                            JButton clienteButton = new JButton(cliente.getNome());
                            clienteButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    clienteAtual = cliente;
                                    nomeField.setText(cliente.getNome());
                                    telefoneField.setText(cliente.getTelefone());
                                    emailField.setText(cliente.getEmail());
                                    exibirVeiculosCliente(cliente); // Exibir os veículos do cliente
                                }
                            });
                            resultadosPanel.add(clienteButton);
                        }
                    }
                    resultadosPanel.revalidate();
                    resultadosPanel.repaint();
                }
            }
        });

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clienteAtual != null) {
                    clienteAtual.setNome(nomeField.getText());
                    clienteAtual.setTelefone(telefoneField.getText());
                    clienteAtual.setEmail(emailField.getText());

                    try {
                        ClienteWriter.salvarClientesEmTxt(clientes, "clientes.txt");
                        JOptionPane.showMessageDialog(null, "Dados do cliente atualizados com sucesso!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao salvar as alterações.");
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhum cliente carregado para atualização.");
                }
            }
        });

        voltarMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a janela atual
                menuInicial.setVisible(true); // Torna o menu inicial visível novamente
            }
        });

        JPanel pesquisaPanel = new JPanel(new BorderLayout());
        pesquisaPanel.add(pesquisaField, BorderLayout.CENTER);
        pesquisaPanel.add(pesquisarButton, BorderLayout.EAST);

        JPanel editarPanel = new JPanel(new GridLayout(5, 2));
        editarPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        editarPanel.add(nomeField);
        editarPanel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        editarPanel.add(telefoneField);
        editarPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        editarPanel.add(emailField);

        // Adicionando campos para veículos
        //ocutei o txt de veiculos:
        //editarPanel.add(new JLabel("Veículos:"));
        JButton adicionarVeiculoButton = new JButton("Adicionar Veículo");
        editarPanel.add(adicionarVeiculoButton);

        adicionarVeiculoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarVeiculoDialog(); // Chama o método para abrir o diálogo de adição de veículo
            }
        });

        editarPanel.add(salvarButton);
        editarPanel.add(voltarMenuButton);

        add(pesquisaPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(editarPanel, BorderLayout.SOUTH);
    }

    // Método para exibir os veículos do cliente atual
    private void exibirVeiculosCliente(Cliente cliente) {
        // Limpar o painel de veículos antes de adicionar os novos veículos
        resultadosPanel.removeAll();
        resultadosPanel.revalidate();
        resultadosPanel.repaint();

        JPanel veiculosPanel = new JPanel();
        veiculosPanel.setLayout(new BoxLayout(veiculosPanel, BoxLayout.Y_AXIS));

        for (Veiculo veiculo : cliente.getVeiculos()) {
            JPanel veiculoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            veiculoPanel.setBorder(BorderFactory.createEtchedBorder());

            JLabel labelVeiculo = new JLabel(veiculo.toString());
            JButton removerButton = new JButton("Remover");

            removerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cliente.removerVeiculo(veiculo); // Remove o veículo do cliente
                    exibirVeiculosCliente(cliente); // Atualiza a exibição dos veículos
                }
            });

            veiculoPanel.add(labelVeiculo);
            veiculoPanel.add(removerButton);
            veiculosPanel.add(veiculoPanel);
        }

        resultadosPanel.add(veiculosPanel);
        resultadosPanel.revalidate();
        resultadosPanel.repaint();
    }
    

    // Método para abrir o diálogo de adicionar veículo
    private void adicionarVeiculoDialog() {
        JDialog dialog = new JDialog(this, "Adicionar Veículo", true);
        dialog.setSize(300, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel tipoLabel = new JLabel("Tipo de Veículo:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(tipoLabel, gbc);

        String[] tiposDeVeiculo = {"Carro", "Moto", "Caminhao"};
        JComboBox<String> tipoVeiculoComboBox = new JComboBox<>(tiposDeVeiculo);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(tipoVeiculoComboBox, gbc);

        JLabel placaLabel = new JLabel("Placa:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(placaLabel, gbc);
        JTextField placaField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(placaField, gbc);

        JLabel marcaLabel = new JLabel("Marca:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(marcaLabel, gbc);
        JTextField marcaField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(marcaField, gbc);

        JLabel modeloLabel = new JLabel("Modelo:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(modeloLabel, gbc);
        JTextField modeloField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(modeloField, gbc);

        JLabel corLabel = new JLabel("Cor:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(corLabel, gbc);
        JTextField corField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(corField, gbc);

        JLabel cilindradasLabel = new JLabel("Cilindradas:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(cilindradasLabel, gbc);
        JTextField cilindradasField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(cilindradasField, gbc);

        JLabel cargaMaxLabel = new JLabel("Carga Máxima (kg):");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(cargaMaxLabel, gbc);
        JTextField cargaMaxField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(cargaMaxField, gbc);

        JLabel comprimentoLabel = new JLabel("Comprimento (m):");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(comprimentoLabel, gbc);
        JTextField comprimentoField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weightx=1;
        gbc.weighty=0.1;
        dialog.add(comprimentoField, gbc);

        JButton adicionarButton = new JButton("Adicionar");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.weightx=1;
        gbc.weighty=0.6;
        dialog.add(adicionarButton, gbc);

        tipoVeiculoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipo = (String) tipoVeiculoComboBox.getSelectedItem();
                if (tipo.equals("Moto")) {
                    corLabel.setVisible(false);
                    corField.setVisible(false);
                    cargaMaxField.setVisible(false);
                    cargaMaxLabel.setVisible(false);
                    comprimentoField.setVisible(false);
                    comprimentoLabel.setVisible(false);
                    cilindradasLabel.setVisible(true);
                    cilindradasField.setVisible(true);
                    marcaField.setVisible(true);
                    marcaLabel.setVisible(true);
                    modeloField.setVisible(true);
                    modeloLabel.setVisible(true);

                } else if(tipo.equals("Carro")){
                    cilindradasLabel.setVisible(false);
                    cilindradasField.setVisible(false);
                    cargaMaxField.setVisible(false);
                    cargaMaxLabel.setVisible(false);
                    comprimentoField.setVisible(false);
                    comprimentoLabel.setVisible(false);
                    corLabel.setVisible(true);
                    corField.setVisible(true);
                    marcaField.setVisible(true);
                    marcaLabel.setVisible(true);
                    modeloField.setVisible(true);
                    modeloLabel.setVisible(true);
                    
                }else if(tipo.equals("Caminhao")){
                    marcaField.setVisible(false);
                    marcaLabel.setVisible(false);
                    modeloField.setVisible(false);
                    modeloLabel.setVisible(false);
                    corLabel.setVisible(false);
                    corField.setVisible(false);
                    cilindradasField.setVisible(false);
                    cilindradasLabel.setVisible(false);
                    cargaMaxField.setVisible(true);
                    cargaMaxLabel.setVisible(true);
                    comprimentoField.setVisible(true);
                    comprimentoLabel.setVisible(true);
                    
                }
            }
        });

        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipo = (String) tipoVeiculoComboBox.getSelectedItem();
                String placa = placaField.getText();
                String marca = marcaField.getText();
                String modelo = modeloField.getText();
                Veiculo veiculo = null;

                try {
                    if (tipo.equals("Carro")) {
                        String cor = corField.getText();
                        veiculo = new Carro(placa, cor, modelo, marca);
                    } else if (tipo.equals("Moto")) {
                        int cilindradas = Integer.parseInt(cilindradasField.getText());
                        veiculo = new Moto(placa, marca, modelo, cilindradas);
                    }
                    else if (tipo.equals("Caminhao")) {
                        int cargaMax = Integer.parseInt(cargaMaxField.getText());
                        int comprimento = Integer.parseInt(comprimentoField.getText());
                        veiculo = new Caminhao(placa, cargaMax,comprimento);
                }

                    if (veiculo != null) {
                        clienteAtual.adicionarVeiculo(veiculo);
                        JOptionPane.showMessageDialog(null, "Veículo adicionado com sucesso!");
                        exibirVeiculosCliente(clienteAtual); // Atualiza a exibição dos veículos após adicionar
                        dialog.dispose(); // Fecha o diálogo após adicionar
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao adicionar o veículo.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira valores numéricos válidos para carga máxima e comprimento.");
                }
            }
        });

        

        // Inicialmente esconder os campos de cor, carga máxima e comprimento
        corLabel.setVisible(true);
        corField.setVisible(true);
        cilindradasLabel.setVisible(false);
        cilindradasField.setVisible(false);
        cargaMaxField.setVisible(false);
        cargaMaxLabel.setVisible(false);
        comprimentoField.setVisible(false);
        comprimentoLabel.setVisible(false);

        dialog.setVisible(true);
    }
}
