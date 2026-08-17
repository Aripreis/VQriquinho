package modelo;


import java.util.ArrayList;
import java.util.List;


/**
 * Conta Investimento Automático: distribui saldo entre produtos e calcula rendimento.
 * Considera regras de carência e taxas polimórficas de cliente (PF/PJ).
 */
public class ContaInvestimentoAutomatico extends Conta {

    private Cliente titular;
    private List<ProdutoInvestimento> carteira;

    public ContaInvestimentoAutomatico(String numero, double saldoInicial, Cliente titular) {
        super(numero, saldoInicial);
        this.titular = titular;
        this.carteira = new ArrayList<>();
    }

    /**
     * Adiciona produto à carteira.
     */
    public void alocarProduto(ProdutoInvestimento produto) {
        this.carteira.add(produto);
    }

    /**
     * Remove produto da carteira.
     */
    public void removerProduto(ProdutoInvestimento produto) {
        this.carteira.remove(produto);
    }

    public List<ProdutoInvestimento> getCarteira() {
        return carteira;
    }

    public Cliente getTitular() {
        return titular;
    }

    /**
     * Simulação com distribuição em carteira, validação de carência e taxas do titular.
     */
    @Override
    public SimulacaoResultado simularRendimento(int dias) {
        // Passo 1: new SimulacaoResultado()
        SimulacaoResultado resultado = new SimulacaoResultado();
        double somaRendimentos = 0;

        // Distribui o saldo igualmente entre os produtos da carteira
        double valorPorProduto = carteira.isEmpty() ? 0 : getSaldo() / carteira.size();

        // Passos 2-8: Loop iterando sobre a Carteira de Produtos
        for (ProdutoInvestimento produto : carteira) {

            // Verifica se é Renda Fixa para aplicar regra de carência
            if (produto instanceof ProdutoRendaFixa) {
                ProdutoRendaFixa rf = (ProdutoRendaFixa) produto;
                int carencia = rf.getPeriodoCarenciaDias();

                if (dias < carencia) {
                    // Produto ignorado: dentro da carência
                    resultado.addAviso(
                        "Produto '" + rf.getNome() + "' ignorado: simulacao de "
                        + dias + " dias esta dentro do periodo de carencia de "
                        + carencia + " dias."
                    );
                    continue; // Pula para o próximo produto
                }
            }

            // POLIMORFISMO: cada produto calcula seu rendimento de forma própria
            double rendimento = produto.calcularRendimentoBruto(valorPorProduto, dias);
            somaRendimentos += rendimento;
        }

        // Passo 9: Obtém a taxa do cliente via polimorfismo (PF=0.001, PJ=0.0015)
        double taxaCliente = titular.obterTaxaInvestimentoAutomatico();

        // Passo 10: Calcula taxa de serviço
        double taxaServico = somaRendimentos * taxaCliente;

        // Passos 11-12: Define valores no resultado
        resultado.setRendimentoBruto(somaRendimentos);
        resultado.setTaxaServico(taxaServico);
        resultado.setRendimentoLiquido(somaRendimentos - taxaServico);

        // Passo 13: Retorna o resultado completo
        return resultado;
    }

    @Override
    public String toString() {
        return "[Investimento Automatico] " + super.toString()
                + " | Produtos na carteira: " + carteira.size();
    }
}
