import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClienteReader {
    public static List<Cliente> lerClientesDeTxt(String caminhoArquivo) throws IOException {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            String nome = null;
            String telefone = null;
            String email = null;
            List<Veiculo> veiculos = new ArrayList<>();

            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.startsWith("Nome: ")) {
                    if (nome != null) {
                        Cliente cliente = new Cliente(nome, telefone, email);
                        cliente.getVeiculos().addAll(veiculos);
                        clientes.add(cliente);
                        nome = null;
                        telefone = null;
                        email = null;
                        veiculos = new ArrayList<>();
                    }
                    nome = linha.substring("Nome: ".length());
                } else if (linha.startsWith("Telefone: ")) {
                    telefone = linha.substring("Telefone: ".length());
                } else if (linha.startsWith("Email: ")) {
                    email = linha.substring("Email: ".length());
                } else if (linha.startsWith("- ")) {
                    // Verifica se é uma linha de veículo
                    String detalhesVeiculo = linha.substring(2); // Remove o prefixo "- "
                    Veiculo veiculo = criarVeiculo(detalhesVeiculo);
                    if (veiculo != null) {
                        veiculos.add(veiculo);
                    }
                }
            }

            // Adicionar o último cliente lido
            if (nome != null) {
                Cliente cliente = new Cliente(nome, telefone, email);
                cliente.getVeiculos().addAll(veiculos);
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    private static Veiculo criarVeiculo(String detalhesVeiculo) {
        // Exemplo de detalhesVeiculo: "Carro - Placa: ABC1234, Marca: Marca A, Modelo: Modelo X, Cor: Vermelho"
        String[] partes = detalhesVeiculo.split(" - ");
        if (partes.length < 2) {
            return null; // Formato inválido
        }
    
        String tipoVeiculo = partes[0]; // Tipo de veículo (Carro, Moto, etc.)
        String detalhes = partes[1]; // Detalhes do veículo (Placa: ABC1234, Marca: Marca A, Modelo: Modelo X, Cor: Vermelho)
    
        String[] campos = detalhes.split(", ");
        String placa = null;
        String marca = null;
        String modelo = null;
        String cor = null;
        int cilindradas = 0;
        int cargaMax = 0;
        int comprimento = 0;
    
        for (String campo : campos) {
            String[] campoValor = campo.split(": ");
            if (campoValor.length != 2) {
                continue; // Ignora campos mal formados
            }
            String nomeCampo = campoValor[0];
            String valorCampo = campoValor[1];
    
            switch (nomeCampo) {
                case "Placa":
                    placa = valorCampo;
                    break;
                case "Marca":
                    marca = valorCampo;
                    break;
                case "Modelo":
                    modelo = valorCampo;
                    break;
                case "Cor":
                    cor = valorCampo;
                    break;
                case "Cilindradas":
                    cilindradas = Integer.parseInt(valorCampo);
                    break;
                case "Carga Máxima":
                    cargaMax = Integer.parseInt(valorCampo);
                    break;
                case "Comprimento":
                    comprimento = Integer.parseInt(valorCampo);
                    break;
                default:
                    // Outros campos podem ser ignorados ou tratados conforme necessário
                    break;
            }
        }
    
        Veiculo veiculo = null;
        switch (tipoVeiculo) {
            case "Carro":
                veiculo = new Carro(placa, cor, modelo, marca);
                break;
            case "Moto":
                veiculo = new Moto(placa, marca, modelo, cilindradas);
                break;
            case "Caminhao":
                veiculo = new Caminhao(placa, cargaMax, comprimento);
                break;
            default:
                // Caso não reconheça o tipo de veículo, pode retornar null ou tratar conforme necessário
                break;
        }
    
        return veiculo;
    }
    
}
