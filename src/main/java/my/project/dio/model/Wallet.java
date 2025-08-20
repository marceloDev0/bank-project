// REFACTOR: A lógica de dinheiro foi completamente reescrita.
package my.project.dio.model;

import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString(exclude = "financialHistory") 
public class Wallet {
    @Getter
    private final BankService service;

    @Getter
    protected long funds;

    protected final List<MoneyAudit> financialHistory;

    public Wallet(final BankService serviceType) {
        this.service = serviceType;
        this.funds = 0L;
        this.financialHistory = new ArrayList<>();
    }

    public void addMoney(final long amount, final String description) {
        this.funds += amount;
        var audit = new MoneyAudit(UUID.randomUUID(), this.service, description, amount, OffsetDateTime.now());
        this.financialHistory.add(audit);
    }

    public void reduceMoney(final long amount, final String description) {
        this.funds -= amount;
        var audit = new MoneyAudit(UUID.randomUUID(), this.service, description, -amount, OffsetDateTime.now());
        this.financialHistory.add(audit);
    }

    public List<MoneyAudit> getFinancialTransaction() {
        return this.financialHistory;
    }
}