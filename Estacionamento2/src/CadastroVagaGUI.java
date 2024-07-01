import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CadastroVagaGUI extends JFrame {
    private JTextField numeroField;
    private JTextField andarField;
    private JTextField secaoField;
    private JComboBox<String> tipoVeiculoComboBox;
    private JTextField statusField;
    private List<Vaga> vagas;
    private JFrame menuInicial;

    public CadastroVagaGUI(JFrame menuInicial) {
        this.menuInicial = menuInicial; // Referência para o menu inicial
        setTitle("Cadastro de Vagas de Estacionamento");
        setSize(400, 300);
        setLocationRelativeTo(menuInicial);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Carregar vagas existentes do arquivo ao iniciar
        try {
            vagas = VagaReader.lerVagasDeTxt("vagas.txt");
        } catch (IOException e) {
            vagas = new ArrayList<>(); // Em caso de erro na leitura, iniciar uma lista vazia
            JOptionPane.showMessageDialog(null, "Erro ao carregar vagas existentes.");
            e.printStackTrace();
        }

        JLabel numeroLabel = new JLabel("Numero:");
        numeroField = new JTextField();

        JLabel andarLabel = new JLabel("Andar:");
        andarField = new JTextField();

        JLabel secaoLabel = new JLabel("Seção:");
        secaoField = new JTextField();

        JLabel tipoVeiculoLabel = new JLabel("Tipo de Veículo:");
        String[] tiposDeVeiculo = {"carro","moto", "caminhao"};
        tipoVeiculoComboBox = new JComboBox<>(tiposDeVeiculo);

        JLabel statusLabel = new JLabel("Status:");
        statusField = new JTextField("livre"); // Status padrão
        statusField.setEditable(false); // Status é fixo como "livre"

        JButton salvarButton = new JButton("Salvar");
        JButton voltarMenuButton = new JButton("Voltar ao Menu"); // Botão para voltar ao menu inicial

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numero = Integer.parseInt(numeroField.getText());
                String andar = andarField.getText();
                String secao = secaoField.getText();
                String localizacao = "Andar " + andar + " - Seção " + secao;
                String tipoVeiculo = (String) tipoVeiculoComboBox.getSelectedItem();
                String status = statusField.getText();

                Vaga vaga = new Vaga(numero, localizacao, status, tipoVeiculo);
                vagas.add(vaga);

                try {
                    VagaWriter.salvarVagasEmTxt(vagas, "vagas.txt");
                    JOptionPane.showMessageDialog(null, "Vaga salva com sucesso!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar a vaga.");
                    ex.printStackTrace();
                }

                // Limpar os campos após salvar
                numeroField.setText("");
                andarField.setText("");
                secaoField.setText("");
            }
        });

        voltarMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a janela atual
                menuInicial.setVisible(true); // Torna o menu inicial visível novamente
            }
        });

        add(numeroLabel);
        add(numeroField);
        add(andarLabel);
        add(andarField);
        add(secaoLabel);
        add(secaoField);
        add(tipoVeiculoLabel);
        add(tipoVeiculoComboBox);
        add(statusLabel);
        add(statusField);
        add(salvarButton);
        add(voltarMenuButton);
    }
}
