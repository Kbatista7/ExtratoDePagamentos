
// ==================== PADRÃO SINGLETON ====================
// Garante que existe apenas UMA instância da classe no sistema
// Exemplo: Configuração da loja que deve ser única

class ConfiguracaoLoja {
    // Guarda a única instância que vai existir
    private static ConfiguracaoLoja instanciaUnica = null;

    // Informações da loja
    private String nomeLoja;
    private double taxaEntrega;

    // Construtor privado - ninguém pode fazer "new ConfiguracaoLoja()"
    private ConfiguracaoLoja() {
        this.nomeLoja = "Loja do João";
        this.taxaEntrega = 10.0;
        System.out.println("[SINGLETON] Configuração da loja criada pela primeira vez!");
    }

    // Único jeito de pegar a instância
    public static ConfiguracaoLoja getInstancia() {
        // Se ainda não existe, cria
        if (instanciaUnica == null) {
            instanciaUnica = new ConfiguracaoLoja();
        }
        // Sempre retorna a mesma
        return instanciaUnica;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public double getTaxaEntrega() {
        return taxaEntrega;
    }
}

// ==================== PADRÃO STRATEGY ====================
// Permite trocar o comportamento (algoritmo) em tempo de execução
// Exemplo: Diferentes formas de pagamento

// Interface - o "contrato" que todos os pagamentos devem seguir
interface FormaPagamento {
    void processar(double valor);
}

// Forma 1: Cartão de Crédito
class PagamentoCartao implements FormaPagamento {
    private String numeroCartao;
    private String titular;

    public PagamentoCartao(String numeroCartao, String titular) {
        this.numeroCartao = numeroCartao;
        this.titular = titular;
    }

    @Override
    public void processar(double valor) {
        System.out.println("\n--- PAGAMENTO COM CARTÃO ---");
        System.out.println("Valor: R$ " + valor);
        System.out.println("Titular: " + titular);
        System.out.println("Cartão: **** **** **** " + numeroCartao.substring(12));
        System.out.println("✓ Pagamento aprovado!");
    }
}

// Forma 2: PIX
class PagamentoPix implements FormaPagamento {
    private String chave;

    public PagamentoPix(String chave) {
        this.chave = chave;
    }

    @Override
    public void processar(double valor) {
        System.out.println("\n--- PAGAMENTO COM PIX ---");
        System.out.println("Valor: R$ " + valor);
        System.out.println("Chave: " + chave);
        System.out.println("QR Code gerado!");
        System.out.println("✓ Pagamento instantâneo!");
    }
}

// Forma 3: Boleto
class PagamentoBoleto implements FormaPagamento {
    private String cpf;

    public PagamentoBoleto(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public void processar(double valor) {
        System.out.println("\n--- PAGAMENTO COM BOLETO ---");
        System.out.println("Valor: R$ " + valor);
        System.out.println("CPF: " + cpf);
        System.out.println("Código: 34191.79001 01043.510047 91020.150008");
        System.out.println("✓ Boleto gerado! Vence em 3 dias.");
    }
}

// Classe que USA a estratégia (forma de pagamento)
class Pedido {
    private double valorProdutos;
    private FormaPagamento formaPagamento;

    public Pedido() {
        this.valorProdutos = 0.0;
    }

    public void adicionarProduto(String nome, double preco) {
        System.out.println("+ Adicionado: " + nome + " - R$ " + preco);
        this.valorProdutos = this.valorProdutos + preco;
    }

    public void escolherFormaPagamento(FormaPagamento forma) {
        this.formaPagamento = forma;
    }

    public void finalizar() {
        if (formaPagamento == null) {
            System.out.println("ERRO: Escolha uma forma de pagamento!");
            return;
        }

        // Pega a configuração (Singleton)
        ConfiguracaoLoja config = ConfiguracaoLoja.getInstancia();
        double valorTotal = valorProdutos + config.getTaxaEntrega();

        System.out.println("\n========================================");
        System.out.println("  " + config.getNomeLoja());
        System.out.println("========================================");
        System.out.println("Produtos: R$ " + valorProdutos);
        System.out.println("Entrega: R$ " + config.getTaxaEntrega());
        System.out.println("TOTAL: R$ " + valorTotal);
        System.out.println("========================================");

        // Processa o pagamento usando a estratégia escolhida
        formaPagamento.processar(valorTotal);
    }
}

// ==================== PADRÃO FACTORY ====================
// Cria objetos sem expor a lógica de criação
// Exemplo: Fábrica que cria diferentes formas de pagamento

class FabricaFormaPagamento {

    // Método que cria a forma de pagamento baseado no tipo
    public static FormaPagamento criar(String tipo) {

        // Converte para minúsculo pra facilitar
        tipo = tipo.toLowerCase();

        if (tipo.equals("cartao")) {
            // Cria um pagamento com cartão com dados de exemplo
            return new PagamentoCartao("1234567890123456", "Maria Silva");

        } else if (tipo.equals("pix")) {
            // Cria um pagamento PIX com dados de exemplo
            return new PagamentoPix("maria@email.com");

        } else if (tipo.equals("boleto")) {
            // Cria um pagamento com boleto com dados de exemplo
            return new PagamentoBoleto("123.456.789-00");

        } else {
            System.out.println("ERRO: Tipo '" + tipo + "' não existe!");
            return null;
        }
    }
}

// ==================== PROGRAMA PRINCIPAL ====================

public class Main {

    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  SISTEMA DE VENDAS - PADRÕES DE DESIGN ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // ===== TESTE 1: SINGLETON =====
        System.out.println("═══ TESTE DO SINGLETON ═══");
        ConfiguracaoLoja config1 = ConfiguracaoLoja.getInstancia();
        ConfiguracaoLoja config2 = ConfiguracaoLoja.getInstancia();

        // Verifica se são a mesma instância
        if (config1 == config2) {
            System.out.println("✓ É a mesma instância! Singleton funcionando!");
        }
        System.out.println();

        // ===== COMPRA 1: Pagamento com CARTÃO =====
        System.out.println("\n═══ COMPRA 1 ═══");
        Pedido pedido1 = new Pedido();
        pedido1.adicionarProduto("Mouse Gamer", 89.90);
        pedido1.adicionarProduto("Teclado Mecânico", 299.90);

        // Usando o FACTORY para criar a forma de pagamento
        FormaPagamento pagamento1 = FabricaFormaPagamento.criar("cartao");
        pedido1.escolherFormaPagamento(pagamento1);
        pedido1.finalizar();

        // ===== COMPRA 2: Pagamento com PIX =====
        System.out.println("\n\n═══ COMPRA 2 ═══");
        Pedido pedido2 = new Pedido();
        pedido2.adicionarProduto("Headset", 159.90);

        // Usando o FACTORY novamente
        FormaPagamento pagamento2 = FabricaFormaPagamento.criar("pix");
        pedido2.escolherFormaPagamento(pagamento2);
        pedido2.finalizar();

        // ===== COMPRA 3: Pagamento com BOLETO =====
        System.out.println("\n\n═══ COMPRA 3 ═══");
        Pedido pedido3 = new Pedido();
        pedido3.adicionarProduto("Webcam HD", 249.90);
        pedido3.adicionarProduto("Microfone USB", 179.90);

        // Usando o FACTORY mais uma vez
        FormaPagamento pagamento3 = FabricaFormaPagamento.criar("boleto");
        pedido3.escolherFormaPagamento(pagamento3);
        pedido3.finalizar();

        System.out.println("\n\n╔════════════════════════════════════════╗");
        System.out.println("║       FIM DAS DEMONSTRAÇÕES!          ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
}

