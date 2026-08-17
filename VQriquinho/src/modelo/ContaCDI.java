package modelo;



/**
 * Conta CDI: rende com CDI diário. Taxa: 0,07%.
 */
public class ContaCDI extends Conta {

    /** Taxa de serviço (0,07%) */
    private static final double TAXA_SERVICO = 0.0007;

    /** CDI mensal de referência */
    private static final double CDI_MENSAL = 0.009;

    public ContaCDI(String numero, double saldoInicial) {
        super(numero, saldoInicial);
    }

    @Override
    public SimulacaoResultado simularRendimento(int dias) {
        // Rendimento = (1/30 * CDI * dias) * saldo
        double rendimentoBruto = (1.0 / 30.0) * CDI_MENSAL * dias * getSaldo();
        double taxaServico = rendimentoBruto * TAXA_SERVICO;
        double rendimentoLiquido = rendimentoBruto - taxaServico;

        return new SimulacaoResultado(rendimentoBruto, taxaServico, rendimentoLiquido);
    }

    @Override
    public String toString() {
        return "[CDI] " + super.toString();
    }
}
