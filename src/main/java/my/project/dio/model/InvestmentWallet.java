package my.project.dio.model;

import lombok.Getter;
import lombok.ToString;
import static my.project.dio.model.BankService.INVESTIMENT;

@ToString
@Getter
public class InvestmentWallet extends Wallet {

    private final Investment investment;
    private final AccountWallet account;

    public InvestmentWallet(final Investment investment, final AccountWallet account, final long amount) {
        super(INVESTIMENT);
        this.investment = investment;
        this.account = account;
        account.reduceMoney(amount, "Aplicação em investimento");
        this.addMoney(amount, "Investimento inicial");
    }

    public void updateAmount(final long percent) {
        var amount = getFunds() * percent / 100;
        if (amount > 0) {
            this.addMoney(amount, "Rendimentos do investimento");
        }
    }
}