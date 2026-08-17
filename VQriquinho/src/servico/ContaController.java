package servico;

import modelo.*;


import java.util.ArrayList;
import java.util.List;


/**
 * Controller para operações sobre contas.
 * Demonstra polimorfismo ao iterar contas usando referência genérica Conta.
 */
public class ContaController {

    /**
     * Associa resultado à conta de origem.
     */
    public static class ResultadoPorConta {
        private Conta conta;
        private SimulacaoResultado resultado;

        public ResultadoPorConta(Conta conta, SimulacaoResultado resultado) {
            this.conta = conta;
            this.resultado = resultado;
        }

        public Conta getConta() {
            return conta;
        }

        public SimulacaoResultado getResultado() {
            return resultado;
        }

        @Override
        public String toString() {
            return conta.toString() + "\n" + resultado.toString();
        }
    }

    /**
     * Simula rendimento de todas as contas, usando polimorfismo.
     */
    public List<ResultadoPorConta> simularTodasAsContas(Cliente cliente, int dias) {
        List<ResultadoPorConta> resultados = new ArrayList<>();

        // Itera usando a referência genérica Conta (polimorfismo!)
        for (Conta conta : cliente.getContas()) {
            // Cada tipo de conta resolve simularRendimento() de forma diferente
            SimulacaoResultado resultado = conta.simularRendimento(dias);
            resultados.add(new ResultadoPorConta(conta, resultado));
        }

        return resultados;
    }
}
