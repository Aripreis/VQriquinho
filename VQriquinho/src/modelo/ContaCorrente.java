package modelo;



/**
 * Conta Corrente: sem rendimento. Simulação retorna zero.
 */
public class ContaCorrente extends Conta {

    public ContaCorrente(String numero, double saldoInicial) {
        super(numero, saldoInicial);
    }

    /**
     * Sem rendimento. Retorna SimulacaoResultado zerado.
     */
    @Override
    public SimulacaoResultado simularRendimento(int dias) {
        return new SimulacaoResultado(0, 0, 0);
    }

    @Override
    public String toString() {
        return "[Corrente] " + super.toString();
    }
}
