package modelo;


/**
 * Produto Renda Fixa: rendimento mensal fixo com carência em dias.
 */
public class ProdutoRendaFixa extends ProdutoInvestimento {

    private double rendimentoMensalFixo; // Ex: 0.012 = 1.2% ao mês
    private int periodoCarenciaDias;

    public ProdutoRendaFixa(String nome, String descricao,
                            double rendimentoMensalFixo, int periodoCarenciaDias) {
        super(nome, descricao);
        this.rendimentoMensalFixo = rendimentoMensalFixo;
        this.periodoCarenciaDias = periodoCarenciaDias;
    }

    @Override
    public double calcularRendimentoBruto(double valorAplicado, int dias) {
        // Rendimento proporcional ao período
        return (rendimentoMensalFixo / 30.0) * dias * valorAplicado;
    }

    // ========================= Getters =========================

    public double getRendimentoMensalFixo() {
        return rendimentoMensalFixo;
    }

    public void setRendimentoMensalFixo(double rendimentoMensalFixo) {
        this.rendimentoMensalFixo = rendimentoMensalFixo;
    }

    public int getPeriodoCarenciaDias() {
        return periodoCarenciaDias;
    }

    public void setPeriodoCarenciaDias(int periodoCarenciaDias) {
        this.periodoCarenciaDias = periodoCarenciaDias;
    }

    @Override
    public String toString() {
        return "[Renda Fixa] " + super.toString()
                + " | Rend. Mensal: " + (rendimentoMensalFixo * 100) + "%"
                + " | Carencia: " + periodoCarenciaDias + " dias";
    }
}
