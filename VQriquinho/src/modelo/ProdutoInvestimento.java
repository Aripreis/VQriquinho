package modelo;


/**
 * Produto genérico abstrato. Define contrato calcularRendimentoBruto().
 */
public abstract class ProdutoInvestimento {

    private String nome;
    private String descricao;

    public ProdutoInvestimento(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    /**
     * Calcula o rendimento bruto (polimórfico).
     */
    public abstract double calcularRendimentoBruto(double valorAplicado, int dias);

    // ========================= Getters =========================

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return nome + " - " + descricao;
    }
}
