package modelo;


import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de valor (Value Object) contendo o resultado da simulação (bruto, taxa, líquido e avisos).
 */
public class SimulacaoResultado {

    private double rendimentoBruto;
    private double taxaServico;
    private double rendimentoLiquido;
    private List<String> avisos;

    public SimulacaoResultado() {
        this.rendimentoBruto = 0;
        this.taxaServico = 0;
        this.rendimentoLiquido = 0;
        this.avisos = new ArrayList<>();
    }

    public SimulacaoResultado(double rendimentoBruto, double taxaServico, double rendimentoLiquido) {
        this.rendimentoBruto = rendimentoBruto;
        this.taxaServico = taxaServico;
        this.rendimentoLiquido = rendimentoLiquido;
        this.avisos = new ArrayList<>();
    }

    // ========================= Getters e Setters =========================

    public double getRendimentoBruto() {
        return rendimentoBruto;
    }

    public void setRendimentoBruto(double rendimentoBruto) {
        this.rendimentoBruto = rendimentoBruto;
    }

    public double getTaxaServico() {
        return taxaServico;
    }

    public void setTaxaServico(double taxaServico) {
        this.taxaServico = taxaServico;
    }

    public double getRendimentoLiquido() {
        return rendimentoLiquido;
    }

    public void setRendimentoLiquido(double rendimentoLiquido) {
        this.rendimentoLiquido = rendimentoLiquido;
    }

    public List<String> getAvisos() {
        return avisos;
    }

    public void addAviso(String aviso) {
        this.avisos.add(aviso);
    }

    // ========================= toString =========================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Resultado da Simulacao ===\n");
        sb.append(String.format("  Rendimento Bruto:   R$ %.2f\n", rendimentoBruto));
        sb.append(String.format("  Taxa de Servico:    R$ %.2f\n", taxaServico));
        sb.append(String.format("  Rendimento Liquido: R$ %.2f\n", rendimentoLiquido));
        if (!avisos.isEmpty()) {
            sb.append("  --- Avisos ---\n");
            for (String aviso : avisos) {
                sb.append("    [!] ").append(aviso).append("\n");
            }
        }
        return sb.toString();
    }
}
