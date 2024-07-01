import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ReservarGUI extends JFrame {
    private MenuInicial menuInicial;
    private JComboBox<String> clientesComboBox;
    private JComboBox<String> veiculosComboBox;
    private JTextField horarioTextField;
    private JButton confirmarButton;

    public ReservarGUI(MenuInicial menuInicial) {
        this.menuInicial = menuInicial;
        setTitle("Fazer uma Reserva");
        setSize(400, 300);
        setLocationRelativeTo(menuInicial);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel clienteLabel = new JLabel("Selecione o Cliente:");
        clientesComboBox = new JComboBox<>(carregarClientes());
        JLabel veiculoLabel = new JLabel("Selecione o Veículo:");
        veiculosComboBox = new JComboBox<>();
        JLabel horarioLabel = new JLabel("Horário da Reserva (HH:mm):");
        horarioTextField = new JTextField();
        confirmarButton = new JButton("Confirmar Reserva");
        JButton voltarButton = new JButton("Voltar ao Menu Inicial");

        clientesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarVeiculos((String) clientesComboBox.getSelectedItem());
            }
        });

        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarReserva();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarMenuInicial();
            }
        });

        add(clienteLabel);
        add(clientesComboBox);
        add(veiculoLabel);
        add(veiculosComboBox);
        add(horarioLabel);
        add(horarioTextField);
        add(confirmarButton);
        add(voltarButton); // Adiciona o botão "Voltar ao Menu Inicial"

        // Carregar veículos do primeiro cliente por padrão
        if (clientesComboBox.getItemCount() > 0) {
            carregarVeiculos((String) clientesComboBox.getSelectedItem());
        }
    }

    private String[] carregarClientes() {
        ArrayList<String> clientesList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("clientes.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Nome: ")) {
                    String nome = line.substring(6);
                    clientesList.add(nome);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return clientesList.toArray(new String[0]);
    }

    private void carregarVeiculos(String clienteSelecionado) {
        veiculosComboBox.removeAllItems();
        try (BufferedReader br = new BufferedReader(new FileReader("clientes.txt"))) {
            String line;
            boolean encontrouCliente = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Nome: ")) {
                    String nome = line.substring(6);
                    if (nome.equals(clienteSelecionado)) {
                        encontrouCliente = true;
                    }
                } else if (encontrouCliente && line.startsWith("-")) {
                    // Lendo veículo do cliente encontrado
                    String veiculo = line.trim();
                    if (!veiculo.contains("Moto")) { // Exclui veículos do tipo "Moto"
                        veiculosComboBox.addItem(veiculo);
                    }
                } else if (encontrouCliente && line.isEmpty()) {
                    // Fim dos veículos do cliente encontrado
                    encontrouCliente = false;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar veículos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarReserva() {
        String clienteSelecionado = (String) clientesComboBox.getSelectedItem();
        String veiculoSelecionado = (String) veiculosComboBox.getSelectedItem();
        String horarioReserva = horarioTextField.getText();

        if (clienteSelecionado == null || veiculoSelecionado == null || horarioReserva.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar vaga disponível e reservar
        boolean reservaEfetuada = reservarVaga(clienteSelecionado, veiculoSelecionado, horarioReserva);
        if (reservaEfetuada) {
            JOptionPane.showMessageDialog(this, "Reserva efetuada com sucesso!");
            // Fechar janela após reserva
            dispose();
            // Atualizar menu inicial se necessário
            menuInicial.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível realizar a reserva. Verifique as vagas disponíveis.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private boolean reservarVaga(String cliente, String veiculo, String horario) {
        String arquivo = "vagas.txt";
        String statusBuscado = "Status: livre";
        String novoStatus = "Status: reservado";
        String reservasArquivo = "reservas.txt";
        veiculo=getTipoVeiculoFromVeiculo(veiculo);

        // Criando um nome único para o arquivo temporário usando timestamp
        Path tempFile = Paths.get("vagas_temp.txt");
    
        try {
            // Abre o arquivo original para leitura
            BufferedReader br = new BufferedReader(new FileReader(arquivo));
    
            // Abre o arquivo temporário para escrita
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile.toFile()));
            PrintWriter pw = new PrintWriter(bw);
    
            String linha;
            boolean encontrouVaga = false;
            String numeroVaga = "";
            String numeroSelect = "";//salva
            //String armazenaVeic = "";
            String linhaAUX;
    
            // Processa cada linha do arquivo original
            while ((linha = br.readLine()) != null) {
                // Verifica se a linha contém o número da vaga
                if (linha.startsWith("Numero: ")) {
                    numeroVaga = linha.substring(8).trim(); // Extrai o número da vaga após "Numero: "
                }
                
    
                // Verifica se a linha contém o status desejado
                if (linha.contains(statusBuscado) && numeroSelect=="") {
                    linhaAUX = linha;
                    linha=br.readLine();


                    if(linha.contains("TipoVeiculo: " + veiculo)){
                        numeroSelect=numeroVaga;
                        linhaAUX = linhaAUX.replace(statusBuscado, novoStatus);
                        pw.println(linhaAUX);
                        pw.println(linha);
                        System.out.println("Encontrou vaga ;-;");
                        encontrouVaga = true;
                    }
                    else{
                        pw.println(linhaAUX);
                        pw.println(linha);
                    }
                }
                else {
                    // Escreve a linha original no arquivo temporário
                    pw.println(linha);
                }
                
                
            }
    
            // Fecha os recursos de leitura e escrita
            br.close();
            pw.close();
            bw.close();
    
            // Verifica se encontrou alguma vaga com status livre e fez alterações
            if (encontrouVaga) {
                // Verifica se o arquivo original existe e exclui se existir
                Path arquivoOriginal = Paths.get(arquivo);
                if (Files.exists(arquivoOriginal)) {
                    Files.delete(arquivoOriginal);
                }
    
                // Move o arquivo temporário para substituir o arquivo original
                Files.move(tempFile, arquivoOriginal);
    
                // Adiciona a reserva ao arquivo de reservas
                try (PrintWriter reservasWriter = new PrintWriter(new BufferedWriter(new FileWriter(reservasArquivo, true)))) {
                    reservasWriter.println("Cliente: " + cliente);
                    reservasWriter.println("Veiculo: " + veiculo);
                    reservasWriter.println("Horario: " + horario);
                    reservasWriter.println("Numero da Vaga: " + numeroSelect);
                    reservasWriter.println(novoStatus);
                    reservasWriter.println(); // Adiciona uma linha em branco entre reservas
                }
    
                System.out.println("Vaga reservada com sucesso!");
                return true;
            } else {
                // Se não encontrou nenhuma vaga com status livre para o veículo desejado, exclui o arquivo temporário
                Files.deleteIfExists(tempFile);
                System.out.println("Nenhuma vaga disponível com status livre para o veículo selecionado.");
                return false;
            }
    
        } catch (IOException e) {
            System.err.println("Erro ao manipular o arquivo: " + e.getMessage());
            e.printStackTrace(); // Mostra o stack trace para ajudar a identificar o erro
            return false;
        }
    }
    

    private void voltarMenuInicial() {
        dispose(); // Fecha a janela de reservas
        if (menuInicial != null) {
            menuInicial.setVisible(true); // Torna o menu inicial visível novamente
        } else {
            // Implemente o comportamento desejado caso o menu inicial seja nulo
            System.out.println("Menu inicial não definido.");
        }
    }

    private String getTipoVeiculoFromVeiculo(String veiculo) {
        String tipoVeiculo = "";
        if (veiculo.contains("Carro")) {
            tipoVeiculo = "carro";
        } else if (veiculo.contains("Moto")) {
            tipoVeiculo = "moto";
        } else if (veiculo.contains("Caminhao")) {
            tipoVeiculo = "caminhao";
        }
        return tipoVeiculo;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ReservarGUI(null).setVisible(true); // Passando null para o MenuInicial temporariamente
            }
        });
    }
}
