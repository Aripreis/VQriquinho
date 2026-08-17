package servico;

import modelo.*;


import java.util.ArrayList;
import java.util.List;


/**
 * Simula uma camada de persistência (Mock de DAO/Repository) em memória.
 */
public class BancoDeDados {

    private List<Cliente> clientes;
    private List<ProdutoInvestimento> produtos;

    public BancoDeDados() {
        this.clientes = new ArrayList<>();
        this.produtos = new ArrayList<>();
    }

    // ========================= CRUD CLIENTES =========================

    /**
     * Salva cliente.
     */
    public boolean salvar(Cliente cliente) {
        clientes.add(cliente);
        return true;
    }

    /**
     * Remove cliente.
     */
    public boolean remover(Cliente cliente) {
        return clientes.remove(cliente);
    }

    /**
     * Busca cliente por nome.
     */
    public Cliente buscarPorNome(String nome) {
        for (Cliente c : clientes) {
            if (c.getNome().equalsIgnoreCase(nome)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Lista todos clientes.
     */
    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes);
    }

    /**
     * Retorna total de clientes.
     */
    public int totalClientes() {
        return clientes.size();
    }

    // ========================= CRUD PRODUTOS =========================

    /**
     * Salva produto.
     */
    public boolean salvarProduto(ProdutoInvestimento produto) {
        produtos.add(produto);
        return true;
    }

    /**
     * Remove produto.
     */
    public boolean removerProduto(ProdutoInvestimento produto) {
        return produtos.remove(produto);
    }

    /**
     * Lista produtos.
     */
    public List<ProdutoInvestimento> listarProdutos() {
        return new ArrayList<>(produtos);
    }

    /**
     * Busca produto por índice.
     */
    public ProdutoInvestimento buscarProdutoPorIndice(int indice) {
        if (indice >= 0 && indice < produtos.size()) {
            return produtos.get(indice);
        }
        return null;
    }

    /**
     * Retorna total de produtos.
     */
    public int totalProdutos() {
        return produtos.size();
    }
}
