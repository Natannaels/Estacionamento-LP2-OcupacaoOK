import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VagaWriter {
    public static void salvarVagasEmTxt(List<Vaga> vagas, String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (Vaga vaga : vagas) {
                writer.write(vaga.toString() + "\n");
            }
        }
    }
}
