package pl.wbsoft.dao;

import pl.wbsoft.entities.Account;
import pl.wbsoft.utils.Pesel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class MemoryAccountDao implements AccountDao {

    List<Account> list = new ArrayList<>();

    @Override
    public Optional<Account> find(Pesel pesel) {

        AtomicReference<Account> foundItem = new AtomicReference<>();
        list.forEach((Account item) -> {
            if (item.getPesel().equals(pesel)) foundItem.set(item);
        });
        return Optional.of(foundItem.get());
    }

    @Override
    public Account create(Account account) {
        list.add(account);
        return account;
    }
}
