import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CadastroClienteGUI extends JFrame {
    private JTextField nomeField;
    private JTextField telefoneField;
    private JTextField emailField;
    private List<Veiculo> veiculos;
    private JFrame menuInicial;

    public CadastroClienteGUI(JFrame menuInicial) {
        this.menuInicial = menuInicial; // Referência para o menu inicial
        setTitle("Cadastro de Cliente");
        setSize(350, 400);
        setLocationRelativeTo(menuInicial);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        veiculos = new ArrayList<>();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel nomeLabel = new JLabel("Nome:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx=0;
        gbc.weighty=0.1;
        add(nomeLabel, gbc);
        nomeField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx=0;
        gbc.weighty=0.1;
        add(nomeField, gbc);

        JLabel telefoneLabel = new JLabel("Telefone:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx=0;
        gbc.weighty=0.1;
        add(telefoneLabel, gbc);
        telefoneField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx=0;
        gbc.weighty=0.1;
        add(telefoneField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx=0;
        gbc.weighty=0.1;
        add(emailLabel, gbc);
        emailField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx=0;
        gbc.weighty=0.1;
        add(emailField, gbc);

        JButton adicionarVeiculoButton = new JButton("Adicionar Veículo");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weightx=1;
        gbc.weighty=0.6;
        add(adicionarVeiculoButton, gbc);

        JButton salvarButton = new JButton("Salvar Cliente");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx=1;
        gbc.weighty=0.6;
        add(salvarButton, gbc);

        JButton voltarMenuButton = new JButton("Voltar ao Menu");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.weightx=1;
        gbc.weighty=0.6;
        add(voltarMenuButton, gbc);

        adicionarVeiculoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarVeiculoDialog();
            }
        });

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarCliente();
            }
        });

        voltarMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                menuInicial.setVisible(true);
            }
        });
    }

    private void salvarCliente() {
        String nome = nomeField.getText();
        String telefone = telefoneField.getText();
        String email = emailField.getText();

        Cliente cliente = new Cliente(nome, telefone, email);
        for (Veiculo veiculo : veiculos) {
            cliente.adicionarVeiculo(veiculo);
        }

        try {
            List<Cliente> clientes = ClienteReader.lerClientesDeTxt("clientes.txt");
            clientes.add(cliente);
            ClienteWriter.salvarClientesEmTxt(clientes, "clientes.txt");
            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
            resetFields();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar o cliente.");
            ex.printStackTrace();
        }
    }

    private void resetFields() {
        nomeField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        veiculos.clear();
    }

    private void adicionarVeiculoDialog() {
        JDialog dialog = new JDialog(this, "Adicionar Veículo", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(menuInicial);
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
        gbc.weightx=1;
        gbc.weighty=0.1;
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
        gbc.weighty=0.4;
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
                    
                }else{
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
                    veiculos.add(veiculo);
                    JOptionPane.showMessageDialog(null, "Veículo adicionado com sucesso!");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao adicionar o veículo.");
                }
            }
        });

        // Inicialmente, esconda campos específicos de Moto
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
