package pl.wbsoft.utils;

import pl.wbsoft.error.InvalidPeselException;

import java.time.LocalDate;
import java.util.Objects;

public class Pesel {

    private final String peselStr;

    public Pesel(String peselStr) throws InvalidPeselException {

        PeselValidator validator = new PeselValidator(peselStr);
        if (validator.isValid()) {
            this.peselStr = peselStr;
        } else {
            throw new InvalidPeselException("");
        }
    }

    public LocalDate getBirthDate() {
        try {
            PeselValidator validator = new PeselValidator(peselStr);
            return validator.getBirthDate();
        } catch (InvalidPeselException e) {
            // ten wyjątek nie ma prawa się pojawić,
            // bo jeśli istnieje obiekt Pesel, to został już zwalidowany w chwili utworzenia
            throw new RuntimeException();
        }
    }

    @Override
    public String toString() {
        return peselStr;
    }

    private class PeselValidator {

        private static final int PESEL_LEN = 11;
        private final byte[] peselBytes = new byte[PESEL_LEN];

        public LocalDate getBirthDate() throws InvalidPeselException {
            int birthYear = getBirthYear();
            int birthMonth = getBirthMonth();
            int birthDay = getBirthDay();
            return LocalDate.of(birthYear, birthMonth, birthDay);
        }

        private int getBirthYear() throws InvalidPeselException {
            int year = (10 * peselBytes[0]) + peselBytes[1];
            int month = 10 * peselBytes[2] + peselBytes[3];
            if (month >= 1 && month <= 12) {
                year += 1900;
            } else if (month >= 21 && month <= 32) {
                year += 2000;
            } else if (month >= 41 && month <= 52) {
                year += 2100;
            } else if (month >= 61 && month <= 72) {
                year += 2200;
            } else if (month >= 81 && month <= 92) {
                year += 1800;
            } else
                throw new InvalidPeselException();
            return year;
        }

        private int getBirthMonth() throws InvalidPeselException {
            int month = 10 * peselBytes[2] + peselBytes[3];
            if (month >= 1 && month <= 12) {
                month -= 0;
            } else if (month >= 21 && month <= 32) {
                month -= 20;
            } else if (month >= 41 && month <= 52) {
                month -= 40;
            } else if (month >= 61 && month <= 72) {
                month -= 60;
            } else if (month >= 81 && month <= 92) {
                month -= 80;
            } else
                throw new InvalidPeselException();
            return month;
        }

        private int getBirthDay() {
            return 10 * peselBytes[4] + peselBytes[5];
        }

        public PeselValidator(String utilPeselStr) throws InvalidPeselException {
            if (utilPeselStr.length() != PESEL_LEN) {
                throw new InvalidPeselException("");
            }
            for (int i = 0; i < PESEL_LEN; i++) {
                peselBytes[i] = Byte.parseByte(utilPeselStr.substring(i, i + 1));
            }
        }

        private boolean isValid() throws InvalidPeselException {
            return isValidControlSum() && isValidBirthMonth() && isValidBirthDay();
        }

        private boolean isValidControlSum() {
            int sum = 1 * peselBytes[0] +
                    3 * peselBytes[1] +
                    7 * peselBytes[2] +
                    9 * peselBytes[3] +
                    1 * peselBytes[4] +
                    3 * peselBytes[5] +
                    7 * peselBytes[6] +
                    9 * peselBytes[7] +
                    1 * peselBytes[8] +
                    3 * peselBytes[9];
            sum %= 10;
            sum = 10 - sum;
            sum %= 10;
            return (sum == peselBytes[10]);
        }

        private boolean isValidBirthMonth() throws InvalidPeselException {
            int month = getBirthMonth();
            return (month >= 1 && month <= 12);
        }

        private boolean isValidBirthDay() throws InvalidPeselException {
            int year = getBirthYear();
            int month = getBirthMonth();
            int day = getBirthDay();
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    return (day >= 1 && day <= 31);
                case 4:
                case 6:
                case 9:
                case 11:
                    return (day >= 1 && day <= 30);
                case 2:
                    return (day >= 1 && day <= 29 && leapYear(year)) ||
                            (day >= 1 && day <= 28 && !leapYear(year));
                default:
                    throw new InvalidPeselException();
            }
        }

        private boolean leapYear(int year) {
            return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0);
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pesel)) return false;
        Pesel pesel = (Pesel) o;
        return Objects.equals(peselStr, pesel.peselStr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(peselStr);
    }


}

