package modelo;



/**
 * Conta genérica abstrata. Encapsula saldo e define simulação polimórfica.
 */
public abstract class Conta {

    private String numero;
    private double saldo;

    public Conta(String numero, double saldoInicial) {
        this.numero = numero;
        this.saldo = saldoInicial;
    }

    /**
     * Simula o rendimento da conta.
     */
    public abstract SimulacaoResultado simularRendimento(int dias);

    /**
     * Deposita valor positivo.
     */
    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
    }

    /**
     * Saca valor (se houver saldo suficiente).
     */
    public boolean sacar(double valor) {
        if (valor > 0 && valor <= this.saldo) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }

    // ========================= Getters =========================

    public String getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    /**
     * Setter protegido para subclasses ajustarem saldo (ex: rendimento).
     */
    protected void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return String.format("Conta %s | Saldo: R$ %.2f", numero, saldo);
    }
}
