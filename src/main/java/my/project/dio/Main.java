package my.project.dio;

import my.project.dio.expcetion.AccountNotFoundException;
import my.project.dio.expcetion.NotFundsEnoughException;
import my.project.dio.model.AccountWallet;
import my.project.dio.repository.AccountRepository;
import my.project.dio.repository.InvestmentRepository;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("||====================================||");
        System.out.println("||       Bem-vindo ao BankDev         ||");
        System.out.println("||====================================||");

        while (true) {
            printMenu();
            try {
                int option = readIntInput();
                switch (option) {
                    case 1 -> createAccount();
                    case 2 -> createInvestment();
                    case 3 -> createWalletInvestment();
                    case 4 -> deposit();
                    case 5 -> withdraw();
                    case 6 -> transferToAccount();
                    case 7 -> incInvestment();
                    case 8 -> rescueInvestment();
                    case 9 -> listAccounts();
                    case 10 -> investmentRepository.list().forEach(System.out::println);
                    case 11 -> investmentRepository.listWallets().forEach(System.out::println);
                    case 12 -> {
                        investmentRepository.updateAccount();
                        System.out.println(">>> Investimentos reajustados com sucesso");
                    }
                    case 13 -> checkHistory();
                    case 14 -> {
                        System.out.println(">>> Obrigado por usar o BankDev! Saindo...");
                        System.exit(0);
                    }
                    default -> System.out.println(">>> Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println(">>> Erro: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println(">>> Ocorreu um erro inesperado: " + e.getMessage());
            }
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine(); 
        }
    }

    private static void printMenu() {
        System.out.println("\n----- MENU DE OPERAÇÕES -----");
        System.out.println("1 - Criar uma conta");
        System.out.println("2 - Listar tipos de investimento");
        System.out.println("3 - Criar carteira de investimento");
        System.out.println("4 - Depositar na conta");
        System.out.println("5 - Sacar da conta");
        System.out.println("6 - Transferência entre contas (PIX)");
        System.out.println("7 - Investir (aplicar)");
        System.out.println("8 - Resgatar investimento");
        System.out.println("9 - Listar contas");
        System.out.println("10 - Listar investimentos disponíveis");
        System.out.println("11 - Listar carteiras de investimento");
        System.out.println("12 - Atualizar rendimentos (simulação)");
        System.out.println("13 - Ver extrato da conta");
        System.out.println("14 - Sair");
        System.out.print("Selecione a operação desejada: ");
    }

    private static int readIntInput() {
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    private static long readLongInput() {
        long input = scanner.nextLong();
        scanner.nextLine(); 
        return input;
    }

    private static String readStringInput() {
        return scanner.nextLine();
    }

    private static void createAccount() {
        System.out.print("Informe as chaves PIX, separadas por ';': ");
        var pix = Arrays.stream(readStringInput().split(";")).toList();
        System.out.print("Informe o valor do depósito inicial (ou 0): ");
        var amount = readLongInput();
        var wallet = accountRepository.create(pix, amount);
        System.out.println("\n>>> Conta criada com sucesso!");
        System.out.println("    Chaves PIX: " + wallet.getPix());
        System.out.println("    Saldo inicial: R$ " + String.format("%.2f", (double) wallet.getFunds()));
    }

    private static void createInvestment() {
        System.out.print("Informe a taxa de rendimento (%): ");
        var tax = readIntInput();
        System.out.print("Informe o valor mínimo para aplicação: ");
        var initialFunds = readLongInput();
        var investment = investmentRepository.create(tax, initialFunds);
        System.out.println(">>> Tipo de investimento criado: " + investment);
    }

    private static void withdraw() {
        System.out.print("Informe a chave PIX da conta para saque: ");
        var pix = readStringInput();
        System.out.print("Informe o valor que será sacado: ");
        var amount = readLongInput();
        try {
            accountRepository.withdraw(pix, amount);
            System.out.println(">>> Saque de R$ " + String.format("%.2f", (double) amount) + " realizado com sucesso!");
        } catch (AccountNotFoundException | NotFundsEnoughException ex) {
            System.out.println(">>> Erro: " + ex.getMessage());
        }
    }

    private static void deposit() {
        System.out.print("Informe a chave PIX da conta para depósito: ");
        var pix = readStringInput();
        System.out.print("Informe o valor que será depositado: ");
        var amount = readLongInput();
        try {
            accountRepository.deposit(pix, amount);
            System.out.println(">>> Depósito de R$ " + String.format("%.2f", (double) amount) + " realizado com sucesso!");
        } catch (AccountNotFoundException ex) {
            System.out.println(">>> Erro: " + ex.getMessage());
        }
    }

    private static void transferToAccount() {
        System.out.print("Informe a chave PIX da conta de origem: ");
        var source = readStringInput();
        System.out.print("Informe a chave PIX da conta de destino: ");
        var target = readStringInput();
        System.out.print("Informe o valor a ser transferido: ");
        var amount = readLongInput();
        try {
            accountRepository.transferMoney(source, target, amount);
            System.out.println(">>> Transferência de R$ " + String.format("%.2f", (double) amount) + " de " + source + " para " + target + " realizada com sucesso!");
        } catch (AccountNotFoundException | NotFundsEnoughException ex) {
            System.out.println(">>> Erro: " + ex.getMessage());
        }
    }

    private static void createWalletInvestment() {
        try {
            System.out.print("Informe a chave PIX da conta: ");
            var pix = readStringInput();
            var account = accountRepository.findByPix(pix);
            System.out.print("Informe o ID do investimento desejado: ");
            var investmentId = readIntInput();
            var investmentWallet = investmentRepository.initInvestment(account, investmentId);
            System.out.println(">>> Carteira de investimento criada com sucesso: " + investmentWallet);
        } catch (Exception ex) {
            System.out.println(">>> Erro ao criar carteira: " + ex.getMessage());
        }
    }

    private static void incInvestment() {
        System.out.print("Informe a chave PIX da conta: ");
        var pix = readStringInput();
        System.out.print("Informe o valor que será investido: ");
        var amount = readLongInput();
        try {
            investmentRepository.deposit(pix, amount);
            System.out.println(">>> Aplicação de R$ " + String.format("%.2f", (double) amount) + " realizada com sucesso!");
        } catch (Exception ex) {
            System.out.println(">>> Erro: " + ex.getMessage());
        }
    }

    private static void rescueInvestment() {
        System.out.print("Informe a chave PIX da conta para resgate: ");
        var pix = readStringInput();
        System.out.print("Informe o valor que será resgatado: ");
        var amount = readLongInput();
        try {
            investmentRepository.withdraw(pix, amount);
            System.out.println(">>> Resgate de R$ " + String.format("%.2f", (double) amount) + " realizado com sucesso!");
        } catch (AccountNotFoundException | NotFundsEnoughException ex) {
            System.out.println(">>> Erro: " + ex.getMessage());
        }
    }

    private static void checkHistory() {
        System.out.print("Informe a chave PIX da conta para ver o extrato: ");
        var pix = readStringInput();
        try {
            AccountWallet wallet = accountRepository.findByPix(pix);
            System.out.println("\n--- EXTRATO DA CONTA: " + pix + " ---");
            System.out.println("SALDO ATUAL: R$ " + String.format("%.2f", (double) wallet.getFunds()));
            System.out.println("----------------------------------------------");

            var transactions = wallet.getFinancialTransaction();
            if (transactions.isEmpty()) {
                System.out.println("Nenhuma transação encontrada.");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
                transactions.forEach(t -> {
                    String formattedDate = t.createAt().format(formatter);
                    String valor = String.format("R$ %.2f", (double) Math.abs(t.amount()));
                    String tipo = t.amount() > 0 ? "[CRÉDITO]" : "[DÉBITO] ";
                    System.out.printf("%s %-10s | %-35s | %s\n",
                            formattedDate,
                            tipo,
                            t.description(),
                            valor);
                });
            }
            System.out.println("----------------------------------------------");
        } catch (AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private static void listAccounts() {
        var accounts = accountRepository.list();
        if (accounts.isEmpty()) {
            System.out.println(">>> Nenhuma conta cadastrada.");
            return;
        }
        System.out.println("--- LISTA DE CONTAS ---");
        accounts.forEach(acc -> {
            System.out.printf("PIX: %-20s | Saldo: R$ %.2f\n", acc.getPix().toString(), (double)acc.getFunds());
        });
        System.out.println("-----------------------");
    }
}