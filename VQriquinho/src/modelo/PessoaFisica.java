package modelo;


/**
 * Cliente Pessoa Física (PF) com CPF. Taxa: 0,1%.
 */
public class PessoaFisica extends Cliente {

    private String cpf;

    public PessoaFisica(String nome, String email, String cpf) {
        super(nome, email);
        this.cpf = cpf;
    }

    /**
     * Retorna taxa PF (0.1%).
     */
    @Override
    public double obterTaxaInvestimentoAutomatico() {
        return 0.001; // 0,1%
    }

    // ========================= Getters e Setters =========================

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "PF: " + super.toString() + " | CPF: " + cpf;
    }
}
