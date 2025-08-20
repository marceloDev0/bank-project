package my.project.dio.repository;

import my.project.dio.expcetion.AccountNotFoundException;
import my.project.dio.expcetion.PixInUseException;
import my.project.dio.model.AccountWallet;

import java.util.ArrayList;
import java.util.List;

import static my.project.dio.repository.CommonsRepository.checkFundsForTransaction;

public class AccountRepository {

    private final List<AccountWallet> accounts = new ArrayList<>();

    public AccountWallet create(final List<String> pix, final long initialFunds) {
        if (!accounts.isEmpty()) {
            var pixInUse = accounts.stream().flatMap(a -> a.getPix().stream()).toList();
            for (var p : pix) {
                if (pixInUse.contains(p)) {
                    throw new PixInUseException("O pix " + p + " já está em uso");
                }
            }
        }
        var newAccount = new AccountWallet(initialFunds, pix);
        accounts.add(newAccount);
        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount) {
        var target = findByPix(pix);
        target.addMoney(fundsAmount, "Depósito recebido");
    }

    public long withdraw(final String pix, final long amount) {
        var source = findByPix(pix);
        checkFundsForTransaction(source, amount);
        source.reduceMoney(amount, "Saque");
        return amount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount) {
        var source = findByPix(sourcePix);
        checkFundsForTransaction(source, amount);
        var target = findByPix(targetPix);
        
        var messageToSource = "PIX enviado para " + targetPix;
        source.reduceMoney(amount, messageToSource);

        var messageToTarget = "PIX recebido de " + sourcePix;
        target.addMoney(amount, messageToTarget);
    }

    public AccountWallet findByPix(final String pix) {
        return accounts.stream().filter(a -> a.getPix().contains(pix)).findFirst().orElseThrow(() -> new AccountNotFoundException("A conta com a chave pix " + pix + " não existe"));
    }

    public List<AccountWallet> list() {
        return this.accounts;
    }
}