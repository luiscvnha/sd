package Ex1;

public interface Banco {
    int criarConta(double saldoInicial);

    double fecharConta(int id) throws ContaInvalida;

    double consultar(int id) throws ContaInvalida;

    double consultarTotal(int[] id_contas);

    void levantar(int id, double valor) throws ContaInvalida, SaldoInsuficiente;

    void depositar(int id, double valor) throws ContaInvalida;

    void transferir(int id_origem, int id_destino, double valor) throws ContaInvalida, SaldoInsuficiente;
}
