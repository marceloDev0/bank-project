package my.project.dio.repository;

import my.project.dio.expcetion.AccountWithInvestmentException;
import my.project.dio.expcetion.InvestmentNotFoundException;
import my.project.dio.expcetion.WalletNotFundsException;
import my.project.dio.model.AccountWallet;
import my.project.dio.model.Investment;
import my.project.dio.model.InvestmentWallet;

import java.util.ArrayList;
import java.util.List;

import static my.project.dio.repository.CommonsRepository.checkFundsForTransaction;

public class InvestmentRepository {

    private long nextId = 0;
    private final List<Investment> investments = new ArrayList<>();
    private final List<InvestmentWallet> wallets = new ArrayList<>();

    public Investment create(final long tax, final long initialFunds) {
        this.nextId++;
        var investment = new Investment(this.nextId, tax, initialFunds);
        investments.add(investment);
        return investment;
    }

    public InvestmentWallet initInvestment(final AccountWallet account, final long id) {
        if (!wallets.isEmpty()) {
            var accountsInUse = wallets.stream().map(InvestmentWallet::getAccount).toList();
            if (accountsInUse.contains(account)) {
                throw new AccountWithInvestmentException("A conta " + account.getPix().getFirst() + " já possui um investimento");
            }
        }
        var investment = findById(id);
        checkFundsForTransaction(account, investment.initialFunds());
        var wallet = new InvestmentWallet(investment, account, investment.initialFunds());
        wallets.add(wallet);
        return wallet;
    }

    public InvestmentWallet deposit(final String pix, final long funds) {
        var wallet = findWalletByAccount(pix);
        wallet.getAccount().reduceMoney(funds, "Aplicação em investimento");
        wallet.addMoney(funds, "Depósito no investimento");
        return wallet;
    }

    public InvestmentWallet withdraw(final String pix, final long funds) {
        var wallet = findWalletByAccount(pix);
        checkFundsForTransaction(wallet, funds);
        wallet.reduceMoney(funds, "Resgate de investimento");
        wallet.getAccount().addMoney(funds, "Valor resgatado do investimento");

        if (wallet.getFunds() == 0) {
            wallets.remove(wallet);
        }
        return wallet;
    }

    public void updateAccount() {
        wallets.forEach(w -> w.updateAmount(w.getInvestment().tax()));
    }

    public Investment findById(final long id) {
        return investments.stream().filter(a -> a.id() == id).findFirst().orElseThrow(() -> new InvestmentNotFoundException("O investimento " + id + " não foi encontrado"));
    }

    public InvestmentWallet findWalletByAccount(final String pix) {
        return wallets.stream().filter(w -> w.getAccount().getPix().contains(pix)).findFirst().orElseThrow(() -> new WalletNotFundsException("A carteira de investimentos não foi encontrada para o pix " + pix));
    }

    public List<InvestmentWallet> listWallets() {
        return this.wallets;
    }

    public List<Investment> list() {
        return this.investments;
    }
}