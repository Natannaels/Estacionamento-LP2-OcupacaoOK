public class Vaga {
    private int numero;
    private String localizacao;
    private String status;
    private String tipoVeiculo;

    public Vaga(int numero, String localizacao, String tipoVeiculo) {
        this.numero = numero;
        this.localizacao = localizacao;
        this.status = "livre"; // Status padrão
        this.tipoVeiculo = tipoVeiculo;
    }

    public Vaga(int numero, String localizacao, String status, String tipoVeiculo) {
        this.numero = numero;
        this.localizacao = localizacao;
        this.status = status;
        this.tipoVeiculo = tipoVeiculo;
    }

    public int getNumero() { return numero; }
    public String getLocalizacao() { return localizacao; }
    public String getStatus() { return status; }
    public String getTipoVeiculo() { return tipoVeiculo; }

    @Override
    public String toString() {
        return "Numero: " + numero + "\n" +
                "Localizacao: " + localizacao + "\n" +
                "Status: " + status + "\n" +
                "TipoVeiculo: " + tipoVeiculo + "\n";
    }

    public static Vaga fromString(String str) {
        String[] lines = str.split("\n");
        if (lines.length < 4) {
            throw new IllegalArgumentException("Formato inválido: " + str);
        }
        int numero = Integer.parseInt(lines[0].split(": ")[1].trim());
        String localizacao = lines[1].split(": ")[1].trim();
        String status = lines[2].split(": ")[1].trim();
        String tipoVeiculo = lines[3].split(": ")[1].trim();
        return new Vaga(numero, localizacao, status, tipoVeiculo);
    }
}
