import modelo.*;
import servico.*;
import servico.ContaController.ResultadoPorConta;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Aplicação CLI VcRiquinho.
 * 
 * Funcionalidades: CRUD Clientes/Produtos, Gestão de Contas, Simulação de rendimentos.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final BancoDeDados bd = new BancoDeDados();
    private static final ContaFactory contaFactory = new ContaFactory();
    private static final ClienteService clienteService = new ClienteService(contaFactory, bd);
    private static final ContaController contaController = new ContaController();
    private static Cliente clienteLogado = null;

    public static void main(String[] args) {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("   VcRiquinho - Escritorio de Investimentos");
        System.out.println("   Sistema de Gerenciamento de Clientes e Investimentos");
        System.out.println("========================================================");

        menuPrincipal();

        System.out.println();
        System.out.println("  Obrigado por usar o VcRiquinho! Ate logo.");
        scanner.close();
    }

    // ================================================================
    //  MENU PRINCIPAL
    // ================================================================

    private static void menuPrincipal() {
        int opcao;
        do {
            System.out.println();
            System.out.println("============= MENU PRINCIPAL =============");
            System.out.println("  1. Cadastrar Cliente");
            System.out.println("  2. Login (Selecionar Cliente)");
            System.out.println("  3. Gerenciar Produtos de Investimento");
            System.out.println("  4. Listar Todos os Clientes");
            System.out.println("  0. Sair");
            System.out.println("==========================================");

            opcao = lerOpcao();

            switch (opcao) {
                case 1: cadastrarCliente(); break;
                case 2: loginCliente(); break;
                case 3: menuProdutos(); break;
                case 4: listarClientes(); break;
                case 0: break;
                default: System.out.println("  Opcao invalida!");
            }
        } while (opcao != 0);
    }

    // ================================================================
    //  CADASTRO DE CLIENTE (CREATE)
    // ================================================================

    private static void cadastrarCliente() {
        System.out.println();
        System.out.println("--- CADASTRO DE CLIENTE ---");
        System.out.println("  1. Pessoa Fisica (PF)");
        System.out.println("  2. Pessoa Juridica (PJ)");
        System.out.println("  0. Cancelar");

        int tipo = lerOpcao();
        if (tipo == 0) return;
        if (tipo != 1 && tipo != 2) {
            System.out.println("  Opcao invalida!");
            return;
        }

        String nome = lerTexto("Nome");
        String email = lerTexto("E-mail");

        System.out.println();
        System.out.println("  Todo cliente deve possuir pelo menos uma conta.");
        String tipoConta = selecionarTipoConta();
        if (tipoConta == null) return;
        double saldo = lerDouble("Saldo inicial da conta (R$)");

        String resultado;
        if (tipo == 1) {
            String cpf = lerTexto("CPF");
            resultado = clienteService.cadastrarClientePF(nome, email, cpf, tipoConta, saldo);
        } else {
            String cnpj = lerTexto("CNPJ");
            resultado = clienteService.cadastrarClientePJ(nome, email, cnpj, tipoConta, saldo);
        }

        System.out.println();
        System.out.println("  " + resultado);
    }

    // ================================================================
    //  LOGIN (SELECIONAR CLIENTE)
    // ================================================================

    private static void loginCliente() {
        List<Cliente> clientes = bd.listarTodos();
        if (clientes.isEmpty()) {
            System.out.println();
            System.out.println("  Nenhum cliente cadastrado. Cadastre um cliente primeiro.");
            return;
        }

        System.out.println();
        System.out.println("--- LOGIN ---");
        System.out.println("  Clientes cadastrados:");
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + clientes.get(i));
        }
        System.out.println("    0. Voltar");

        int indice = lerOpcao();
        if (indice == 0) return;
        if (indice < 1 || indice > clientes.size()) {
            System.out.println("  Opcao invalida!");
            return;
        }

        clienteLogado = clientes.get(indice - 1);
        System.out.println();
        System.out.println("  Bem-vindo(a), " + clienteLogado.getNome() + "!");
        menuCliente();
    }

    // ================================================================
    //  LISTAR CLIENTES (READ ALL)
    // ================================================================

    private static void listarClientes() {
        List<Cliente> clientes = bd.listarTodos();
        System.out.println();
        System.out.println("--- CLIENTES CADASTRADOS ---");
        if (clientes.isEmpty()) {
            System.out.println("  Nenhum cliente cadastrado.");
        } else {
            for (int i = 0; i < clientes.size(); i++) {
                Cliente c = clientes.get(i);
                System.out.println("  " + (i + 1) + ". " + c
                    + " | Contas: " + c.getContas().size());
            }
            System.out.println("  Total: " + clientes.size() + " cliente(s)");
        }
    }

    // ================================================================
    //  MENU DO CLIENTE LOGADO
    // ================================================================

    private static void menuCliente() {
        int opcao;
        do {
            System.out.println();
            System.out.println("========== AREA DO CLIENTE ==========");
            System.out.println("  Cliente: " + clienteLogado.getNome());
            System.out.println("=====================================");
            System.out.println("  1. Ver Meus Dados");
            System.out.println("  2. Atualizar Meus Dados");
            System.out.println("  3. Gerenciar Minhas Contas");
            System.out.println("  4. Simular Rendimentos");
            System.out.println("  5. Excluir Minha Conta do Sistema");
            System.out.println("  6. Logout");
            System.out.println("  0. Sair do Sistema");

            opcao = lerOpcao();

            switch (opcao) {
                case 1: verDadosCliente(); break;
                case 2: atualizarCliente(); break;
                case 3: menuContas(); break;
                case 4: menuSimulacao(); break;
                case 5:
                    if (excluirCliente()) return;
                    break;
                case 6:
                    clienteLogado = null;
                    System.out.println("  Logout realizado com sucesso.");
                    return;
                case 0:
                    System.out.println("  Obrigado por usar o VcRiquinho! Ate logo.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("  Opcao invalida!");
            }
        } while (true);
    }

    // ================================================================
    //  VER DADOS DO CLIENTE (READ)
    // ================================================================

    private static void verDadosCliente() {
        System.out.println();
        System.out.println("--- MEUS DADOS ---");
        System.out.println("  " + clienteLogado);

        if (clienteLogado instanceof PessoaFisica) {
            System.out.println("  Tipo: Pessoa Fisica (PF)");
        } else {
            System.out.println("  Tipo: Pessoa Juridica (PJ)");
        }

        System.out.println("  Taxa Investimento Automatico: "
            + (clienteLogado.obterTaxaInvestimentoAutomatico() * 100) + "%");
        System.out.println();
        System.out.println("  Contas (" + clienteLogado.getContas().size() + "):");

        if (clienteLogado.getContas().isEmpty()) {
            System.out.println("    Nenhuma conta cadastrada.");
        } else {
            for (int i = 0; i < clienteLogado.getContas().size(); i++) {
                Conta c = clienteLogado.getContas().get(i);
                System.out.println("    " + (i + 1) + ". " + c);

                // Se for conta de investimento, mostra os produtos alocados
                if (c instanceof ContaInvestimentoAutomatico) {
                    ContaInvestimentoAutomatico cia = (ContaInvestimentoAutomatico) c;
                    if (!cia.getCarteira().isEmpty()) {
                        for (ProdutoInvestimento p : cia.getCarteira()) {
                            System.out.println("       -> " + p);
                        }
                    } else {
                        System.out.println("       -> (Nenhum produto alocado)");
                    }
                }
            }
        }
    }

    // ================================================================
    //  ATUALIZAR DADOS DO CLIENTE (UPDATE)
    // ================================================================

    private static void atualizarCliente() {
        System.out.println();
        System.out.println("--- ATUALIZAR DADOS ---");
        System.out.println("  Dados atuais: " + clienteLogado);
        System.out.println("  (Pressione Enter para manter o valor atual)");
        System.out.println();

        System.out.print("  Novo nome [" + clienteLogado.getNome() + "]: ");
        String nome = scanner.nextLine().trim();
        if (!nome.isEmpty()) clienteLogado.setNome(nome);

        System.out.print("  Novo e-mail [" + clienteLogado.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) clienteLogado.setEmail(email);

        if (clienteLogado instanceof PessoaFisica) {
            PessoaFisica pf = (PessoaFisica) clienteLogado;
            System.out.print("  Novo CPF [" + pf.getCpf() + "]: ");
            String cpf = scanner.nextLine().trim();
            if (!cpf.isEmpty()) pf.setCpf(cpf);
        } else if (clienteLogado instanceof PessoaJuridica) {
            PessoaJuridica pj = (PessoaJuridica) clienteLogado;
            System.out.print("  Novo CNPJ [" + pj.getCnpj() + "]: ");
            String cnpj = scanner.nextLine().trim();
            if (!cnpj.isEmpty()) pj.setCnpj(cnpj);
        }

        System.out.println();
        System.out.println("  Dados atualizados com sucesso!");
        System.out.println("  Novos dados: " + clienteLogado);
    }

    // ================================================================
    //  EXCLUIR CLIENTE (DELETE)
    // ================================================================

    private static boolean excluirCliente() {
        System.out.println();
        System.out.println("--- EXCLUIR CLIENTE ---");
        System.out.println("  ATENCAO: Esta acao e irreversivel!");
        System.out.println("  Cliente: " + clienteLogado);
        System.out.print("  Confirma exclusao? (S/N): ");
        String confirmacao = scanner.nextLine().trim().toUpperCase();

        if (confirmacao.equals("S")) {
            bd.remover(clienteLogado);
            System.out.println("  Cliente '" + clienteLogado.getNome() + "' removido com sucesso.");
            clienteLogado = null;
            return true;
        }
        System.out.println("  Exclusao cancelada.");
        return false;
    }

    // ================================================================
    //  MENU DE GERENCIAMENTO DE CONTAS
    // ================================================================

    private static void menuContas() {
        int opcao;
        do {
            System.out.println();
            System.out.println("--- GERENCIAR CONTAS ---");
            System.out.println("  1. Listar Minhas Contas");
            System.out.println("  2. Abrir Nova Conta");
            System.out.println("  3. Depositar");
            System.out.println("  4. Sacar");
            System.out.println("  5. Alocar Produto em Conta de Investimento");
            System.out.println("  0. Voltar");

            opcao = lerOpcao();

            switch (opcao) {
                case 1: listarContas(); break;
                case 2: abrirNovaConta(); break;
                case 3: depositar(); break;
                case 4: sacar(); break;
                case 5: alocarProdutoNaConta(); break;
                case 0: break;
                default: System.out.println("  Opcao invalida!");
            }
        } while (opcao != 0);
    }

    private static void listarContas() {
        System.out.println();
        System.out.println("--- MINHAS CONTAS ---");
        List<Conta> contas = clienteLogado.getContas();
        if (contas.isEmpty()) {
            System.out.println("  Nenhuma conta cadastrada.");
        } else {
            for (int i = 0; i < contas.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + contas.get(i));
            }
        }
    }

    private static void abrirNovaConta() {
        System.out.println();
        System.out.println("--- ABRIR NOVA CONTA ---");
        String tipoConta = selecionarTipoConta();
        if (tipoConta == null) return;

        double saldo = lerDouble("Saldo inicial (R$)");

        try {
            Conta novaConta = contaFactory.criarConta(tipoConta, clienteLogado, saldo);
            clienteLogado.adicionarConta(novaConta);
            System.out.println();
            System.out.println("  Conta " + tipoConta + " (N. "
                + novaConta.getNumero() + ") criada com sucesso!");
            System.out.println("  " + novaConta);
        } catch (IllegalArgumentException e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private static void depositar() {
        System.out.println();
        System.out.println("--- DEPOSITAR ---");
        Conta conta = selecionarConta();
        if (conta == null) return;

        double valor = lerDouble("Valor do deposito (R$)");
        conta.depositar(valor);
        System.out.println("  Deposito realizado! Novo saldo: R$ "
            + String.format("%.2f", conta.getSaldo()));
    }

    private static void sacar() {
        System.out.println();
        System.out.println("--- SACAR ---");
        Conta conta = selecionarConta();
        if (conta == null) return;

        double valor = lerDouble("Valor do saque (R$)");
        if (conta.sacar(valor)) {
            System.out.println("  Saque realizado! Novo saldo: R$ "
                + String.format("%.2f", conta.getSaldo()));
        } else {
            System.out.println("  [ERRO] Saldo insuficiente ou valor invalido.");
            System.out.println("  Saldo atual: R$ " + String.format("%.2f", conta.getSaldo()));
        }
    }

    private static void alocarProdutoNaConta() {
        System.out.println();
        System.out.println("--- ALOCAR PRODUTO EM CONTA DE INVESTIMENTO ---");

        // Filtra apenas contas de Investimento Automático
        List<ContaInvestimentoAutomatico> contasInv = new ArrayList<>();
        for (Conta c : clienteLogado.getContas()) {
            if (c instanceof ContaInvestimentoAutomatico) {
                contasInv.add((ContaInvestimentoAutomatico) c);
            }
        }

        if (contasInv.isEmpty()) {
            System.out.println("  Voce nao possui contas de Investimento Automatico.");
            System.out.println("  Abra uma conta desse tipo primeiro.");
            return;
        }

        // Seleciona a conta de investimento
        System.out.println("  Suas contas de Investimento Automatico:");
        for (int i = 0; i < contasInv.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + contasInv.get(i));
        }

        System.out.print("  Selecione a conta: ");
        int indConta = lerOpcaoSemPrompt();
        if (indConta < 1 || indConta > contasInv.size()) {
            System.out.println("  Opcao invalida!");
            return;
        }
        ContaInvestimentoAutomatico contaInv = contasInv.get(indConta - 1);

        List<ProdutoInvestimento> produtos = bd.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("  Nenhum produto de investimento cadastrado no sistema.");
            System.out.println("  Cadastre produtos primeiro (Menu Principal > opcao 3).");
            return;
        }

        System.out.println();
        System.out.println("  Produtos disponiveis:");
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + produtos.get(i));
        }

        System.out.print("  Selecione o produto: ");
        int indProd = lerOpcaoSemPrompt();
        if (indProd < 1 || indProd > produtos.size()) {
            System.out.println("  Opcao invalida!");
            return;
        }

        ProdutoInvestimento produto = produtos.get(indProd - 1);
        contaInv.alocarProduto(produto);
        System.out.println();
        System.out.println("  Produto '" + produto.getNome()
            + "' alocado na conta " + contaInv.getNumero() + "!");
    }

    // ================================================================
    //  MENU DE SIMULAÇÃO DE RENDIMENTOS
    // ================================================================

    private static void menuSimulacao() {
        int opcao;
        do {
            System.out.println();
            System.out.println("--- SIMULACAO DE RENDIMENTOS ---");
            System.out.println("  1. Simular Uma Conta Especifica");
            System.out.println("  2. Simular Todas as Minhas Contas");
            System.out.println("  0. Voltar");

            opcao = lerOpcao();

            switch (opcao) {
                case 1: simularContaEspecifica(); break;
                case 2: simularTodasAsContas(); break;
                case 0: break;
                default: System.out.println("  Opcao invalida!");
            }
        } while (opcao != 0);
    }

    private static void simularContaEspecifica() {
        System.out.println();
        System.out.println("--- SIMULAR CONTA ESPECIFICA ---");

        Conta conta = selecionarConta();
        if (conta == null) return;

        int dias = selecionarPeriodo();
        if (dias == -1) return;

        System.out.println();
        System.out.println("  Simulacao para " + dias + " dias:");
        System.out.println("  Conta: " + conta);
        System.out.println();

        SimulacaoResultado resultado = conta.simularRendimento(dias);
        System.out.println(resultado);
    }

    private static void simularTodasAsContas() {
        System.out.println();
        System.out.println("--- SIMULAR TODAS AS CONTAS ---");

        if (clienteLogado.getContas().isEmpty()) {
            System.out.println("  Nenhuma conta cadastrada.");
            return;
        }

        int dias = selecionarPeriodo();
        if (dias == -1) return;

        System.out.println();
        System.out.println("  Simulacao de " + dias + " dias para "
            + clienteLogado.getNome() + ":");
        System.out.println("  ------------------------------------------------");

        List<ResultadoPorConta> resultados =
            contaController.simularTodasAsContas(clienteLogado, dias);

        for (ResultadoPorConta rpc : resultados) {
            System.out.println();
            System.out.println("  " + rpc.getConta());
            System.out.println(rpc.getResultado());
        }

        System.out.println("  ------------------------------------------------");
    }

    // ================================================================
    //  MENU DE GERENCIAMENTO DE PRODUTOS (CRUD)
    // ================================================================

    private static void menuProdutos() {
        int opcao;
        do {
            System.out.println();
            System.out.println("======= PRODUTOS DE INVESTIMENTO =======");
            System.out.println("  1. Listar Produtos");
            System.out.println("  2. Cadastrar Produto de Renda Fixa");
            System.out.println("  3. Cadastrar Produto de Renda Variavel");
            System.out.println("  4. Atualizar Produto");
            System.out.println("  5. Excluir Produto");
            System.out.println("  0. Voltar");
            System.out.println("=========================================");

            opcao = lerOpcao();

            switch (opcao) {
                case 1: listarProdutos(); break;
                case 2: cadastrarProdutoRendaFixa(); break;
                case 3: cadastrarProdutoRendaVariavel(); break;
                case 4: atualizarProduto(); break;
                case 5: excluirProduto(); break;
                case 0: break;
                default: System.out.println("  Opcao invalida!");
            }
        } while (opcao != 0);
    }

    // ---------- Listar Produtos (READ ALL) ----------

    private static void listarProdutos() {
        System.out.println();
        System.out.println("--- PRODUTOS CADASTRADOS ---");
        List<ProdutoInvestimento> produtos = bd.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("  Nenhum produto cadastrado.");
        } else {
            for (int i = 0; i < produtos.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + produtos.get(i));
            }
            System.out.println("  Total: " + produtos.size() + " produto(s)");
        }
    }

    // ---------- Cadastrar Renda Fixa (CREATE) ----------

    private static void cadastrarProdutoRendaFixa() {
        System.out.println();
        System.out.println("--- CADASTRAR PRODUTO RENDA FIXA ---");

        String nome = lerTexto("Nome do produto");
        String descricao = lerTexto("Descricao");
        double rendMensal = lerDouble("Rendimento mensal fixo (ex: 1.2 para 1.2%)");
        int carencia = lerInt("Periodo de carencia (dias)");

        ProdutoRendaFixa produto = new ProdutoRendaFixa(
            nome, descricao, rendMensal / 100.0, carencia
        );
        bd.salvarProduto(produto);

        System.out.println();
        System.out.println("  Produto '" + nome + "' cadastrado com sucesso!");
        System.out.println("  " + produto);
    }

    // ---------- Cadastrar Renda Variável (CREATE) ----------

    private static void cadastrarProdutoRendaVariavel() {
        System.out.println();
        System.out.println("--- CADASTRAR PRODUTO RENDA VARIAVEL ---");

        String nome = lerTexto("Nome do produto");
        String descricao = lerTexto("Descricao");
        double rendEsperado = lerDouble("Rendimento mensal esperado (ex: 2.0 para 2%)");

        ProdutoRendaVariavel produto = new ProdutoRendaVariavel(
            nome, descricao, rendEsperado / 100.0
        );
        bd.salvarProduto(produto);

        System.out.println();
        System.out.println("  Produto '" + nome + "' cadastrado com sucesso!");
        System.out.println("  " + produto);
    }

    // ---------- Atualizar Produto (UPDATE) ----------

    private static void atualizarProduto() {
        List<ProdutoInvestimento> produtos = bd.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("  Nenhum produto cadastrado.");
            return;
        }

        System.out.println();
        System.out.println("--- ATUALIZAR PRODUTO ---");
        listarProdutos();
        System.out.println();
        System.out.print("  Selecione o produto para atualizar: ");

        int indice = lerOpcaoSemPrompt() - 1;
        if (indice < 0 || indice >= produtos.size()) {
            System.out.println("  Opcao invalida!");
            return;
        }

        ProdutoInvestimento produto = produtos.get(indice);
        System.out.println("  Produto selecionado: " + produto);
        System.out.println("  (Pressione Enter para manter o valor atual)");
        System.out.println();

        System.out.print("  Novo nome [" + produto.getNome() + "]: ");
        String nome = scanner.nextLine().trim();
        if (!nome.isEmpty()) produto.setNome(nome);

        System.out.print("  Nova descricao [" + produto.getDescricao() + "]: ");
        String descricao = scanner.nextLine().trim();
        if (!descricao.isEmpty()) produto.setDescricao(descricao);

        if (produto instanceof ProdutoRendaFixa) {
            ProdutoRendaFixa rf = (ProdutoRendaFixa) produto;

            System.out.print("  Novo rendimento mensal % ["
                + String.format("%.2f", rf.getRendimentoMensalFixo() * 100) + "]: ");
            String rend = scanner.nextLine().trim();
            if (!rend.isEmpty()) {
                rf.setRendimentoMensalFixo(Double.parseDouble(rend) / 100.0);
            }

            System.out.print("  Nova carencia em dias ["
                + rf.getPeriodoCarenciaDias() + "]: ");
            String carencia = scanner.nextLine().trim();
            if (!carencia.isEmpty()) {
                rf.setPeriodoCarenciaDias(Integer.parseInt(carencia));
            }
        } else if (produto instanceof ProdutoRendaVariavel) {
            ProdutoRendaVariavel rv = (ProdutoRendaVariavel) produto;

            System.out.print("  Novo rendimento esperado % ["
                + String.format("%.2f", rv.getRendimentoMensalEsperado() * 100) + "]: ");
            String rend = scanner.nextLine().trim();
            if (!rend.isEmpty()) {
                rv.setRendimentoMensalEsperado(Double.parseDouble(rend) / 100.0);
            }
        }

        System.out.println();
        System.out.println("  Produto atualizado com sucesso!");
        System.out.println("  " + produto);
    }

    // ---------- Excluir Produto (DELETE) ----------

    private static void excluirProduto() {
        List<ProdutoInvestimento> produtos = bd.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("  Nenhum produto cadastrado.");
            return;
        }

        System.out.println();
        System.out.println("--- EXCLUIR PRODUTO ---");
        listarProdutos();
        System.out.println();
        System.out.print("  Selecione o produto para excluir: ");

        int indice = lerOpcaoSemPrompt() - 1;
        if (indice < 0 || indice >= produtos.size()) {
            System.out.println("  Opcao invalida!");
            return;
        }

        ProdutoInvestimento produto = produtos.get(indice);
        System.out.print("  Confirma exclusao de '" + produto.getNome() + "'? (S/N): ");
        String confirmacao = scanner.nextLine().trim().toUpperCase();

        if (confirmacao.equals("S")) {
            bd.removerProduto(produto);
            System.out.println("  Produto removido com sucesso!");
        } else {
            System.out.println("  Exclusao cancelada.");
        }
    }

    // ================================================================
    //  MÉTODOS AUXILIARES DE SELEÇÃO
    // ================================================================

    /**
     * Exibe opções de tipo de conta e retorna a string selecionada (ou null).
     */
    private static String selecionarTipoConta() {
        System.out.println();
        System.out.println("  Tipo de conta:");
        System.out.println("    1. Conta Corrente");
        System.out.println("    2. Conta CDI");
        System.out.println("    3. Conta de Investimento Automatico");
        System.out.println("    0. Cancelar");

        int opcao = lerOpcao();
        switch (opcao) {
            case 1: return "CORRENTE";
            case 2: return "CDI";
            case 3: return "INVESTIMENTO";
            default: return null;
        }
    }

    /**
     * Seleciona conta do cliente logado (ou null se cancelar/vazio).
     */
    private static Conta selecionarConta() {
        List<Conta> contas = clienteLogado.getContas();
        if (contas.isEmpty()) {
            System.out.println("  Nenhuma conta cadastrada.");
            return null;
        }

        System.out.println("  Selecione a conta:");
        for (int i = 0; i < contas.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + contas.get(i));
        }
        System.out.println("    0. Cancelar");

        int indice = lerOpcao();
        if (indice == 0) return null;
        if (indice < 1 || indice > contas.size()) {
            System.out.println("  Opcao invalida!");
            return null;
        }

        return contas.get(indice - 1);
    }

    /**
     * Seleciona período de simulação (30,60,90,180) ou -1.
     */
    private static int selecionarPeriodo() {
        System.out.println();
        System.out.println("  Selecione o periodo da simulacao:");
        System.out.println("    1.  30 dias");
        System.out.println("    2.  60 dias");
        System.out.println("    3.  90 dias");
        System.out.println("    4. 180 dias");
        System.out.println("    0. Cancelar");

        int opcao = lerOpcao();
        switch (opcao) {
            case 1: return 30;
            case 2: return 60;
            case 3: return 90;
            case 4: return 180;
            case 0: return -1;
            default:
                System.out.println("  Opcao invalida!");
                return -1;
        }
    }

    // ================================================================
    //  MÉTODOS UTILITÁRIOS DE LEITURA
    // ================================================================

    /**
     * Lê opção numérica com prompt.
     */
    private static int lerOpcao() {
        System.out.print("  Opcao: ");
        return lerOpcaoSemPrompt();
    }

    /**
     * Lê opção numérica sem prompt.
     */
    private static int lerOpcaoSemPrompt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Lê texto com rótulo.
     */
    private static String lerTexto(String campo) {
        System.out.print("  " + campo + ": ");
        return scanner.nextLine().trim();
    }

    /**
     * Lê valor double validado.
     */
    private static double lerDouble(String campo) {
        while (true) {
            System.out.print("  " + campo + ": ");
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("    Valor invalido. Digite um numero.");
            }
        }
    }

    /**
     * Lê valor inteiro validado.
     */
    private static int lerInt(String campo) {
        while (true) {
            System.out.print("  " + campo + ": ");
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("    Valor invalido. Digite um numero inteiro.");
            }
        }
    }
}
