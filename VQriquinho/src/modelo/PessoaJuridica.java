package modelo;


/**
 * Cliente Pessoa Jurídica (PJ) com CNPJ. Taxa: 0,15%.
 */
public class PessoaJuridica extends Cliente {

    private String cnpj;

    public PessoaJuridica(String nome, String email, String cnpj) {
        super(nome, email);
        this.cnpj = cnpj;
    }

    /**
     * Retorna taxa PJ (0.15%).
     */
    @Override
    public double obterTaxaInvestimentoAutomatico() {
        return 0.0015; // 0,15%
    }

    // ========================= Getters e Setters =========================

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return "PJ: " + super.toString() + " | CNPJ: " + cnpj;
    }
}
