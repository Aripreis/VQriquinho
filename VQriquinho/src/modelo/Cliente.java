package modelo;


import java.util.ArrayList;
import java.util.List;


/**
 * Cliente abstrato. Define taxa polimórfica e exige ao menos 1 conta.
 */
public abstract class Cliente {

    private String nome;
    private String email;
    private List<Conta> contas;

    public Cliente(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.contas = new ArrayList<>();
    }

    /**
     * Retorna a taxa de serviço (polimórfica) para investimentos automáticos.
     */
    public abstract double obterTaxaInvestimentoAutomatico();

    // ========================= Gerenciamento de Contas =========================

    public void adicionarConta(Conta conta) {
        this.contas.add(conta);
    }

    public void removerConta(Conta conta) {
        this.contas.remove(conta);
    }

    public List<Conta> getContas() {
        return contas;
    }

    // ========================= Getters e Setters =========================

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return nome + " (" + email + ")";
    }
}
