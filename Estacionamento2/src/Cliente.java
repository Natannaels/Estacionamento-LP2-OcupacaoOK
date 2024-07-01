import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cliente {
    private String nome;
    private String telefone;
    private String email;
    private List<Veiculo> veiculos;

    public Cliente(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.veiculos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void adicionarVeiculo(Veiculo veiculo) {
        veiculos.add(veiculo);
    }

    public void removerVeiculo(Veiculo veiculo) {
        Iterator<Veiculo> iterator = veiculos.iterator();
        while (iterator.hasNext()) {
            Veiculo v = iterator.next();
            if (v.equals(veiculo)) {
                iterator.remove();
                break; // Assuming each vehicle is unique for this client
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(nome).append("\n");
        sb.append("Telefone: ").append(telefone).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Veículos:\n");
        for (Veiculo veiculo : veiculos) {
            sb.append("- ").append(veiculo.toString()).append("\n");
        }
        return sb.toString();
    }
}
