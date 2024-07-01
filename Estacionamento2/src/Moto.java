import java.io.Serializable;

public class Moto extends Veiculo implements Serializable {
    private String modelo;
    private String marca;
    private int cilindradas;

    public Moto(String placa, String modelo, String marca, int cilindradas) {
        super(placa, "moto");
        this.modelo = modelo;
        this.marca = marca;
        this.cilindradas = cilindradas;
    }

    @Override
    public String getDetalhes() {
        return "Moto - Placa: " + placa + ", Marca: " + marca + ", Modelo: " + modelo + ", Cilindradas: " + cilindradas;
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

    public int getCilindradas() {
        return cilindradas;
    }

    public void setCilindradas(int cilindradas) {
        this.cilindradas = cilindradas;
    }

    @Override
    public String toString() {
        return getDetalhes();
    }
}
