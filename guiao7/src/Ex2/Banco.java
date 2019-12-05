package Ex2;

import java.io.IOException;
import java.util.List;


public interface Banco {
    void close() throws IOException;

    int criarConta(double saldoInicial) throws BancoRemotoException;

    double fecharConta(int id) throws ContaInvalidaException, BancoRemotoException;

    double consultar(int id) throws ContaInvalidaException, BancoRemotoException;

    double consultarTotal(int[] id_contas) throws BancoRemotoException;

    void levantar(int id, double valor, String descricao) throws ContaInvalidaException, SaldoInsuficienteException, BancoRemotoException;

    void depositar(int id, double valor, String descricao) throws ContaInvalidaException, BancoRemotoException;

    void transferir(int id_origem, int id_destino, double valor, String descricao) throws ContaInvalidaException, SaldoInsuficienteException, BancoRemotoException;

    List<Movimento> movimentos(int id) throws ContaInvalidaException, BancoRemotoException;
}
