## Programa de Gerenciamento de Estacionamento


### Descrição Geral
Este sistema permite o gerenciamento eficiente de um estacionamento, possibilitando o cadastro e a gestão de vagas utilizando arquivos de persistência em formato `.txt`.


### Estrutura do Sistema


#### 1. Classes Visuais
As classes visuais são responsáveis pela interface gráfica do usuário, facilitando a interação com o sistema. As principais classes incluem:


- **`MenuInicialGUI`**: Interface inicial do sistema.
- **`EntradaGUI`**: Interface para a entrada de veículos no estacionamento.
- **`SaidaGUI`**: Interface para a saída de veículos do estacionamento.


#### 2. Classes de Modelo
As classes de modelo representam os dados principais do sistema e fornecem funcionalidades para manipulação desses dados.


- **`VagaReader`**: Classe auxiliar para a leitura de vagas de um arquivo de persistência.
- **`VagaWriter`**: Classe auxiliar para a escrita e atualização do status das vagas no arquivo de persistência.


As classes que permitem a criação de instâncias de objetos específicos são:


- **`Caminhao`**: Representa um caminhão.
- **`Carro`**: Representa um carro.
- **`Moto`**: Representa uma moto.
- **`Cliente`**: Representa um cliente do estacionamento.
- **`Veiculo`**: Classe abstrata que serve como base para `Caminhao`, `Carro` e `Moto`.


#### 3. Classes de Serviço
As classes de serviço fornecem funcionalidades adicionais para a leitura e escrita de dados de clientes e vagas, além de gerenciamento de recibos.


- **`ClienteReader`**: Classe para leitura de dados de clientes de um arquivo de persistência.
- **`ClienteWriter`**: Classe para escrita e atualização de dados de clientes no arquivo de persistência.
- **`Recibo`**: Classe responsável pela geração de recibos para transações no estacionamento.
- **`VagaReader`**: (Mencionada também nas classes de modelo) Classe para leitura de vagas.
- **`VagaWriter`**: (Mencionada também nas classes de modelo) Classe para escrita e atualização de vagas.


### Classes Detalhadas


#### Classe `Carro`
**Descrição:** Representa um carro no sistema.
- **Atributos:**
  - `placa`: `String` - A placa do carro.
  - `cor`: `String` - A cor do carro.
  - `modelo`: `String` - O modelo do carro.
  - `marca`: `String` - A marca do carro.
- **Métodos:**
  - Construtor para inicializar os atributos.
  - Getters e setters para cada atributo.
  - `toString()`: Retorna uma string representando o carro.


#### Classe `Moto`
**Descrição:** Representa uma moto no sistema.
- **Atributos:**
  - `placa`: `String` - A placa da moto.
  - `marca`: `String` - A marca da moto.
  - `modelo`: `String` - O modelo da moto.
  - `cilindradas`: `int` - As cilindradas da moto.
- **Métodos:**
  - Construtor para inicializar os atributos.
  - Getters e setters para cada atributo.
  - `toString()`: Retorna uma string representando a moto.


#### Classe `Caminhao`
**Descrição:** Representa um caminhão no sistema.
- **Atributos:**
  - `placa`: `String` - A placa do caminhão.
  - `cargaMaxima`: `double` - A carga máxima do caminhão.
  - `comprimento`: `double` - O comprimento do caminhão.
- **Métodos:**
  - Construtor para inicializar os atributos.
  - Getters e setters para cada atributo.
  - `toString()`: Retorna uma string representando o caminhão.


#### Classe `Vaga`
**Descrição:** Representa uma vaga de estacionamento.
- **Atributos:**
  - `numero`: `int` - O número da vaga.
  - `tipo`: `String` - O tipo da vaga (Carro, Moto, Caminhão).
  - `disponivel`: `boolean` - Indica se a vaga está disponível.
- **Métodos:**
  - Construtor para inicializar os atributos.
  - Getters e setters para cada atributo.
  - `isDisponivel()`: Retorna se a vaga está disponível.
  - `reservar()`: Marca a vaga como reservada.
  - `liberar()`: Marca a vaga como disponível.


#### Classe `Reserva`
**Descrição:** Representa uma reserva de vaga.
- **Atributos:**
  - `vaga`: `Vaga` - A vaga reservada.
  - `veiculo`: `Veiculo` - O veículo que fez a reserva.
  - `dataReserva`: `Date` - A data da reserva.
  - `dataFim`: `Date` - A data de término da reserva.
- **Métodos:**
  - Construtor para inicializar os atributos.
  - Getters e setters para cada atributo.
  - `cancelar()`: Cancela a reserva.


### Funcionamento Geral
O sistema utiliza arquivos `.txt` para persistir os dados de vagas e clientes. As classes de leitura e escrita (`VagaReader`, `VagaWriter`, `ClienteReader`, `ClienteWriter`) gerenciam essas operações de I/O, garantindo que o estado do estacionamento seja mantido entre execuções do programa.


---


Sinta-se à vontade para adicionar mais detalhes ou modificar algo conforme necessário. Podemos continuar a detalhar as classes ou adicionar mais informações sobre outras funcionalidades do sistema.