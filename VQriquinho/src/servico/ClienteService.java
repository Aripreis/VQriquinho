package servico;

import modelo.*;



/**
 * Controller responsável pelo cadastro de clientes e vinculação da primeira conta.
 */
public class ClienteService {

    private ContaFactory contaFactory;
    private BancoDeDados bancoDeDados;

    public ClienteService(ContaFactory contaFactory, BancoDeDados bancoDeDados) {
        this.contaFactory = contaFactory;
        this.bancoDeDados = bancoDeDados;
    }

    /**
     * Cadastra cliente PF e cria sua conta inicial.
     */
    public String cadastrarClientePF(String nome, String email, String cpf,
                                     String tipoContaInicial, double saldoInicial) {
        // Passo 3: Instancia PessoaFisica
        PessoaFisica novoCliente = new PessoaFisica(nome, email, cpf);

        // Delega para o método genérico de vinculação e persistência
        return vincularContaESalvar(novoCliente, tipoContaInicial, saldoInicial);
    }

    /**
     * Cadastra cliente PJ e cria sua conta inicial.
     */
    public String cadastrarClientePJ(String nome, String email, String cnpj,
                                     String tipoContaInicial, double saldoInicial) {
        // Passo 3: Instancia PessoaJuridica
        PessoaJuridica novoCliente = new PessoaJuridica(nome, email, cnpj);

        // Delega para o método genérico de vinculação e persistência
        return vincularContaESalvar(novoCliente, tipoContaInicial, saldoInicial);
    }

    /**
     * Cria conta via Factory, vincula ao cliente e salva no banco.
     */
    private String vincularContaESalvar(Cliente novoCliente, String tipoContaInicial,
                                        double saldoInicial) {
        try {
            // Passo 4: Factory cria a conta
            Conta novaConta = contaFactory.criarConta(tipoContaInicial, novoCliente, saldoInicial);

            // Passo 5: Vincula a conta ao cliente
            novoCliente.adicionarConta(novaConta);

            // Validação: garante que o cliente possui pelo menos 1 conta
            if (novoCliente.getContas().isEmpty()) {
                return "[ERRO] Falha ao vincular conta ao cliente.";
            }

            // Passo 6: Persiste no banco de dados
            boolean salvo = bancoDeDados.salvar(novoCliente);

            if (salvo) {
                // Passo 7: Retorna sucesso
                return "[SUCESSO] Cliente '" + novoCliente.getNome()
                        + "' cadastrado com conta " + tipoContaInicial.toUpperCase()
                        + " (Nº " + novaConta.getNumero() + ").";
            } else {
                return "[ERRO] Falha ao salvar cliente no banco de dados.";
            }

        } catch (IllegalArgumentException e) {
            // Tipo de conta inválido
            return "[ERRO] " + e.getMessage();
        }
    }

    // ========================= Acesso ao Banco =========================

    public BancoDeDados getBancoDeDados() {
        return bancoDeDados;
    }
}
