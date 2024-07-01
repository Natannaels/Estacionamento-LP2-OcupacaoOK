import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MenuInicial extends JFrame {
    private JTextField horarioTextField;

    public MenuInicial() {
        setTitle("Menu Inicial");
        setSize(300, 300); // Ajustei o tamanho para acomodar o campo de horário
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1)); // Ajustei para 6 linhas para acomodar o novo botão

        JButton cadastrarVagaButton = new JButton("Cadastrar Vaga");
        JButton cadastrarClienteButton = new JButton("Cadastrar Cliente");
        JButton consultarClientesButton = new JButton("Consultar Clientes");
        JButton fazerReservaButton = new JButton("Fazer uma Reserva");
        JButton atualizarHorarioButton = new JButton("Atualizar Horário"); // Novo botão para atualizar horário
        JButton entradaVeiculoButton = new JButton("Entrada de Veículo"); // Novo botão para entrada de veículo

        JLabel horarioLabel = new JLabel("Horário Atual (HH:mm):");
        horarioTextField = new JTextField();
        carregarHorarioAtual();

        cadastrarVagaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CadastroVagaGUI(MenuInicial.this).setVisible(true);
                setVisible(false);
            }
        });

        cadastrarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CadastroClienteGUI(MenuInicial.this).setVisible(true);
                setVisible(false);
            }
        });

        consultarClientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConsultaClienteGUI(MenuInicial.this).setVisible(true);
                setVisible(false);
            }
        });

        fazerReservaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReservarGUI(MenuInicial.this).setVisible(true);
                setVisible(false);
            }
        });

        atualizarHorarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarHorarioAtual();
            }
        });

        entradaVeiculoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EntradaGUI(MenuInicial.this).setVisible(true);
                setVisible(false);
            }
        });

        add(cadastrarVagaButton);
        add(cadastrarClienteButton);
        add(consultarClientesButton);
        add(fazerReservaButton);
        add(horarioLabel);
        add(horarioTextField);
        add(atualizarHorarioButton);
        add(entradaVeiculoButton); // Adiciona o botão de entrada de veículo
    }

    private void carregarHorarioAtual() {
        try (BufferedReader br = new BufferedReader(new FileReader("horarioAtual.txt"))) {
            String horarioAtual = br.readLine();
            horarioTextField.setText(horarioAtual);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar horário atual: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarHorarioAtual() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("horarioAtual.txt"))) {
            bw.write(horarioTextField.getText());
            JOptionPane.showMessageDialog(this, "Horário atualizado com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar horário atual: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuInicial().setVisible(true);
            }
        });
    }
}
