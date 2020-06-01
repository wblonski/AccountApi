package pl.wbsoft.repository;

import org.springframework.stereotype.Repository;
import pl.wbsoft.entities.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Repository
public class AccountRepositoryInMemory implements AccountReposiotory<Account, String> {

    private final Map<String, Account> data = new HashMap<>();

    @Override
    public <S extends Account> S save(S s) {
        data.put(s.getPesel(), s);
        return s;
    }

    @Override
    public <S extends Account> Iterable<S> saveAll(Iterable<S> iterable) {
        StreamSupport.stream(iterable.spliterator(), false)
                .forEach(item -> data.put(item.getPesel(), item));
        return (Iterable<S>) data;
    }

    @Override
    public Optional<Account> findById(String s) {
        if (data.containsKey(s)) {
            return Optional.of(data.get(s));
        } else
            return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return data.containsKey(s);
    }

    @Override
    public Iterable<Account> findAll() {
        return (Iterable<Account>) this.data;
    }

    @Override
    public Iterable<Account> findAllById(Iterable<String> iterable) {

        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(item -> data.containsKey(item))
                .map(item -> data.get(item))
                .collect(toList());
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public void deleteById(String s) {
        data.remove(s);
    }

    @Override
    public void delete(Account account) {
        data.remove(account.getPesel());
    }

    @Override
    public void deleteAll(Iterable<? extends Account> iterable) {
        StreamSupport.stream(iterable.spliterator(), false)
                .filter(item -> data.containsKey(item.getPesel()))
                .forEach(item -> data.remove(item.getPesel()));
    }

    @Override
    public void deleteAll() {
        data.clear();
    }
}
