import java.io.Serializable;

public class Caminhao extends Veiculo implements Serializable {
    private int cargaMax;
    private int comprimentoCaminhao;

    public Caminhao(String placa, int cargaMax, int comprimentoCaminhao) {
        super(placa, "caminhao");
        this.cargaMax = cargaMax;
        this.comprimentoCaminhao = comprimentoCaminhao;
    }

    @Override
    public String getDetalhes() {
        return "Caminhao - Placa: " + placa + ", Carga Máxima: " + cargaMax + ", Comprimento: " + comprimentoCaminhao;
    }

    public float getCargaMax() {
        return cargaMax;
    }

    public void setCargaMax(int cargaMax) {
        this.cargaMax = cargaMax;
    }

    public float getComprimentoCaminhao() {
        return comprimentoCaminhao;
    }

    public void setComprimentoCaminhao(int comprimentoCaminhao) {
        this.comprimentoCaminhao = comprimentoCaminhao;
    }

    @Override
    public String toString() {
        return getDetalhes();
    }
}
