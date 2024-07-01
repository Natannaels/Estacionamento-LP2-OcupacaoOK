import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VagaReader {
    public static List<Vaga> lerVagasDeTxt(String caminhoArquivo) throws IOException {
        List<Vaga> vagas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            StringBuilder vagaStr = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.startsWith("Numero:")) {
                    if (vagaStr.length() > 0) {
                        vagas.add(Vaga.fromString(vagaStr.toString()));
                        vagaStr.setLength(0);
                    }
                }
                vagaStr.append(linha).append("\n");
            }
            if (vagaStr.length() > 0) {
                vagas.add(Vaga.fromString(vagaStr.toString()));
            }
        }
        return vagas;
    }
}
