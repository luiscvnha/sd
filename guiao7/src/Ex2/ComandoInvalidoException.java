package Ex2;

public class ComandoInvalidoException extends Exception {
    public ComandoInvalidoException(String cmd) { super("Comando '" + cmd + "' inválido."); }
}
