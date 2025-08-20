package my.project.dio.repository;

import lombok.NoArgsConstructor;
import my.project.dio.expcetion.NotFundsEnoughException;
import my.project.dio.model.*;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class CommonsRepository {

    public static void checkFundsForTransaction(final Wallet source, final long amount) {
        if (source.getFunds() < amount) {
            throw new NotFundsEnoughException("Sua conta não tem dinheiro o suficiente para realizar essa transação.");
        }
    }
}