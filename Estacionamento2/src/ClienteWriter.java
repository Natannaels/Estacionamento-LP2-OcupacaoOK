import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ClienteWriter {
    public static void salvarClientesEmTxt(List<Cliente> clientes, String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            writer.write("# Clientes\n");
            for (Cliente cliente : clientes) {
                writer.write("Nome: " + cliente.getNome() + "\n");
                writer.write("Telefone: " + cliente.getTelefone() + "\n");
                writer.write("Email: " + cliente.getEmail() + "\n");
                writer.write("Veiculos:\n");
                for (Veiculo veiculo : cliente.getVeiculos()) {
                    writer.write("- " + veiculo.toString() + "\n");
                }
                writer.write("\n"); // separador entre clientes
            }
        }
    }
}
