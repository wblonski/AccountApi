Zwięzły opis realizaji zadania: Konta walutowe

Zrealizowano z użyciem: Maven, Java 8, REST, Spring-Boot, h2.

Realizacja wymagań funkcjonalnych:
p.1-4 : Account createAccount(@RequestBody Account newAccount, @PathVariable String pesel)
	parametr newAccount - każde pole jest walidowane wewnątrz metody,
	Metoda zwraca utworzone konto.
p. 5 :  Account getAccount(@PathVariable String pesel)
	można też uzyskać za pomocą metody patch, podając value = 0, zob. p. 6.
	Zwraca konto dla danego pesela.
p. 6-7 : 	Optional<Account> exchangeInAccount(@RequestBody Map<String, String> update, @PathVariable String pesel)
	parametr update zawiera klucze: currencyFrom, currencyTo, value
	Metoda zwraca zmodyfikowane konto, o ile takie istnieje.
	Rzuca InvalidParmeterException gdy: niepoprawny pesel, nie istnieje account, value < 0.


	Aby poprawnie uruchomić:
	1) mvn clean install
	2) mvn spring-boot:run

	Propozycja testowania:
	1) utwórz konto - screen_create.jpg
