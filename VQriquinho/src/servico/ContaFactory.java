package servico;

import modelo.*;



/**
 * Factory para criação de contas, centralizando lógica e geração de números.
 */
public class ContaFactory {

    private int contadorContas = 0;

    /**
     * Gera próximo número de conta sequencial.
     */
    private String gerarNumeroConta() {
        contadorContas++;
        return String.format("%03d", contadorContas);
    }

    /**
     * Cria nova conta do tipo especificado.
     */
    public Conta criarConta(String tipoConta, Cliente titular, double saldoInicial) {
        String numero = gerarNumeroConta();

        switch (tipoConta.toUpperCase()) {
            case "CORRENTE":
                return new ContaCorrente(numero, saldoInicial);

            case "CDI":
                return new ContaCDI(numero, saldoInicial);

            case "INVESTIMENTO":
                return new ContaInvestimentoAutomatico(numero, saldoInicial, titular);

            default:
                throw new IllegalArgumentException(
                    "Tipo de conta invalido: '" + tipoConta
                    + "'. Tipos aceitos: CORRENTE, CDI, INVESTIMENTO."
                );
        }
    }
}
