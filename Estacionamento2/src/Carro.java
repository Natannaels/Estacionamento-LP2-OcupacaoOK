import java.io.Serializable;

public class Carro extends Veiculo implements Serializable {
    private String cor;
    private String modelo;
    private String marca;

    public Carro(String placa, String cor, String modelo, String marca) {
        super(placa, "carro");
        this.cor = cor;
        this.modelo = modelo;
        this.marca = marca;
    }

    @Override
    public String getDetalhes() {
        return "Carro - Placa: " + placa + ", Cor: " + cor + ", Marca: " + marca + ", Modelo: " + modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return getDetalhes();
    }
}
