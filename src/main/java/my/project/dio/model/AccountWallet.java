package my.project.dio.model;

import lombok.Getter;

import java.util.List;

import static my.project.dio.model.BankService.ACCOUNT;

@Getter
public class AccountWallet extends Wallet {

    private final List<String> pix;

    public AccountWallet(final List<String> pix) {
        super(ACCOUNT);
        this.pix = pix;
    } 

    public AccountWallet(final long initialAmount, final List<String> pix) {
        super(ACCOUNT);
        this.pix = pix;
        if (initialAmount > 0) {
            addMoney(initialAmount, "Valor de criação da conta");
        }
    }
}