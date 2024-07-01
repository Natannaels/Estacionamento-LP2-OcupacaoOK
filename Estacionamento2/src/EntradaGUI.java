import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class EntradaGUI extends JFrame {
    private MenuInicial menuInicial;
    private JComboBox<String> reservasComboBox;
    private JButton confirmarButton;

    public EntradaGUI(MenuInicial menuInicial) {
        this.menuInicial = menuInicial;
        setTitle("Entrada de Veículo");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JLabel reservaLabel = new JLabel("Selecione a Reserva:");
        reservasComboBox = new JComboBox<>(carregarReservas());
        confirmarButton = new JButton("Confirmar Entrada");

        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarEntrada();
            }
        });

        add(reservaLabel);
        add(reservasComboBox);
        add(confirmarButton);
    }

    private String[] carregarReservas() {
        List<String> reservasArquivo = new ArrayList<>();
        String arquivo = "reservas.txt";
    
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            StringBuilder reservaInfo = new StringBuilder();
            String line;
            boolean dentroReserva = false;
    
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Cliente: ")) {
                    if (dentroReserva) {
                        // Adiciona a reserva anterior antes de iniciar a nova
                        reservasArquivo.add(reservaInfo.toString());
                        reservaInfo.setLength(0);
                    }
                    reservaInfo.append(line).append("\n");
                    dentroReserva = true;
                } else if (dentroReserva) {
                    reservaInfo.append(line).append("\n");
                    if (line.startsWith("Status: reservado")) {
                        // Adiciona a reserva completa
                        reservasArquivo.add(reservaInfo.toString());
                        reservaInfo.setLength(0);
                        dentroReserva = false;
                    }
                }
            }
    
            // Adiciona a última reserva, se existir
            if (reservaInfo.length() > 0) {
                reservasArquivo.add(reservaInfo.toString());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar reservas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    
        return reservasArquivo.toArray(new String[0]);
    }

    private void confirmarEntrada() {
        String reservaSelecionada = (String) reservasComboBox.getSelectedItem();
        if (reservaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cliente = "";
        String veiculo = "";
        String numeroVaga = "";
        try (BufferedReader br = new BufferedReader(new StringReader(reservaSelecionada))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Cliente: ")) {
                    cliente = line.substring(9);
                } else if (line.startsWith("Veiculo: ")) {
                    veiculo = line.substring(9);
                } else if (line.startsWith("Numero da Vaga: ")) {
                    numeroVaga = line.substring(16);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao processar reserva: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!atualizarStatusVaga(numeroVaga, "ocupado")) {
            JOptionPane.showMessageDialog(this, "Não foi possível atualizar a vaga.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!registrarMovimentacao(cliente, veiculo, numeroVaga)) {
            JOptionPane.showMessageDialog(this, "Não foi possível registrar a movimentação.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!atualizarStatusReserva(cliente, veiculo, numeroVaga, "ocupado")) {
            JOptionPane.showMessageDialog(this, "Não foi possível atualizar a reserva.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Entrada do veículo confirmada com sucesso!");
        dispose();
        menuInicial.setVisible(true);
    }

    private boolean atualizarStatusVaga(String numeroVaga, String novoStatus) {
        String arquivo = "vagas.txt";
        String statusBuscado = "Numero: " + numeroVaga;
        String statusAntigo = "Status: reservado";
        novoStatus = "Status: ocupado";
        File tempFile = new File("vagas_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
             PrintWriter pw = new PrintWriter(bw)) {

            String linha;
            boolean encontrouVaga = false;

            while ((linha = br.readLine()) != null) {
                if (linha.startsWith("Numero: " + numeroVaga)) {
                    pw.println(linha);
                    linha = br.readLine();
                    pw.println(linha);
                    linha = br.readLine();
                    linha = linha.replace(statusAntigo, novoStatus);
                    pw.println(linha);
                    linha = br.readLine();
                    encontrouVaga = true;
                }
                pw.println(linha);
            }

            br.close();
            pw.close();
            bw.close();

            if (encontrouVaga) {
                File arquivoOriginal = new File(arquivo);
                if (arquivoOriginal.exists() && !arquivoOriginal.delete()) {
                    throw new IOException("Não foi possível excluir o arquivo original.");
                }

                if (!tempFile.renameTo(arquivoOriginal)) {
                    throw new IOException("Não foi possível renomear o arquivo temporário.");
                }
                System.out.println("DEU TRUE!");
                return true;
            } else {
                if (!tempFile.delete()) {
                    throw new IOException("Não foi possível excluir o arquivo temporário.");
                }
                System.out.println("DEU FALSE!");
                return false;
            }

        } catch (IOException e) {
            System.err.println("Erro ao atualizar status da vaga: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean registrarMovimentacao(String cliente, String veiculo, String numeroVaga) {
        String arquivo = "movimentacao.txt";
        StringBuilder registro = new StringBuilder();
        registro.append("Veiculo: ").append(veiculo).append("\n");
        registro.append("Numero da vaga: ").append(numeroVaga).append("\n");

        // Obter o horário de entrada do arquivo horarioAtual.txt
        String horarioEntrada = lerHorarioAtual();
        if (horarioEntrada == null) {
            JOptionPane.showMessageDialog(this, "Erro ao ler horário atual.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        registro.append("HorarioEntrada: ").append(horarioEntrada).append("\n");
        registro.append("\n");
        try {
            // Escreve o registro no arquivo de movimentação
            Files.write(Paths.get(arquivo), registro.toString().getBytes(), StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar movimentação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("Erro ao registrar movimentação: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private String lerHorarioAtual() {
        String arquivo = "horarioAtual.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            return br.readLine(); // Retorna a primeira linha do arquivo (horário atual)
        } catch (IOException e) {
            System.err.println("Erro ao ler horário atual: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private boolean atualizarStatusReserva(String cliente, String veiculo, String numeroVaga, String novoStatus) {
        String arquivo = "reservas.txt";
        String statusBuscado = "Status: reservado";
        novoStatus = "Status: ocupado";
        File tempFile = new File("reservas_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
             PrintWriter pw = new PrintWriter(bw)) {

            String linha;
            boolean encontrouReserva = false;

            while ((linha = br.readLine()) != null) {
                if(linha.contains(numeroVaga)){
                    pw.println(linha);
                    linha=br.readLine();
                    if (linha.contains(statusBuscado) && !encontrouReserva) {
                        linha = linha.replace(statusBuscado, novoStatus);
                        encontrouReserva = true;
                        pw.println(linha);
                    } else {
                        pw.println(linha);
                    }
                }
                else{
                    pw.println(linha);
                }
            }

            br.close();
            pw.close();
            bw.close();

            if (encontrouReserva) {
                File arquivoOriginal = new File(arquivo);
                if (arquivoOriginal.exists() && !arquivoOriginal.delete()) {
                    throw new IOException("Não foi possível excluir o arquivo original.");
                }

                if (!tempFile.renameTo(arquivoOriginal)) {
                    throw new IOException("Não foi possível renomear o arquivo temporário.");
                }

                return true;
            } else {
                if (!tempFile.delete()) {
                    throw new IOException("Não foi possível excluir o arquivo temporário.");
                }
                return false;
            }

        } catch (IOException e) {
            System.err.println("Erro ao atualizar status da reserva: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EntradaGUI(new MenuInicial()).setVisible(true);
            }
        });
    }
}
