package modelo;


/**
 * Produto Renda Variável: sem carência, rendimento mensal esperado.
 */
public class ProdutoRendaVariavel extends ProdutoInvestimento {

    private double rendimentoMensalEsperado; // Ex: 0.02 = 2% ao mês

    public ProdutoRendaVariavel(String nome, String descricao,
                                double rendimentoMensalEsperado) {
        super(nome, descricao);
        this.rendimentoMensalEsperado = rendimentoMensalEsperado;
    }

    @Override
    public double calcularRendimentoBruto(double valorAplicado, int dias) {
        // Rendimento proporcional ao período
        return (rendimentoMensalEsperado / 30.0) * dias * valorAplicado;
    }

    // ========================= Getters =========================

    public double getRendimentoMensalEsperado() {
        return rendimentoMensalEsperado;
    }

    public void setRendimentoMensalEsperado(double rendimentoMensalEsperado) {
        this.rendimentoMensalEsperado = rendimentoMensalEsperado;
    }

    @Override
    public String toString() {
        return "[Renda Variavel] " + super.toString()
                + " | Rend. Esperado: " + (rendimentoMensalEsperado * 100) + "% ao mes";
    }
}
