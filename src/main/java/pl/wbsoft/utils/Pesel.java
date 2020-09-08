package pl.wbsoft.utils;

import pl.wbsoft.exceptions.InvalidPeselException;

import java.time.LocalDate;
import java.util.Objects;

public class Pesel {
    
    private final String peselStr;
    
    public Pesel(String peselStr) {
        this.peselStr = peselStr;
    }
    
    public static void validatePeselStr(String peselStr) throws InvalidPeselException {
        PeselValidator validator = new PeselValidator(peselStr);
        validator.validate();
    }
    
    public static boolean isAdultPeselOwner(String pesel, int adultAge) {
        LocalDate birthDate;
        try {
            birthDate = getBirthDate(pesel);
        } catch (InvalidPeselException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return !birthDate.plusYears(adultAge).isAfter(LocalDate.now());
    }
    
    public static LocalDate getBirthDate(String pesel) throws InvalidPeselException {
        PeselValidator validator = new PeselValidator(pesel);
        return validator.getBirthDate();
    }
    
    @Override
    public String toString() {
        return peselStr;
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
    
    private static class PeselValidator {
        
        private static final String INVALID_PESEL_LENGTH = "Invalid pesel string length. ";
        private static final String INVALID_PESEL_CONTROL_SUM = "Invalid pesel control sum. ";
        private static final String INVALID_PESEL_YEAR = "Invalid pesel year of birth. ";
        private static final String INVALID_PESEL_MONTH = "Invalid pesel month of birth. ";
        private static final String INVALID_PESEL_DAY = "Invalid pesel day of birth. ";
        private static final String INVALID_PESEL_CONTENT = "Pesel string contains not only digits. ";
        
        private static final int PESEL_LEN = 11;
        
        private final String peselStr;
        
        public PeselValidator(String pesel) {
            peselStr = pesel;
        }
        
        private int peselValue(int idx) {
            return peselStr.charAt(idx) - 48;
        }
        
        private void validate() throws InvalidPeselException {
            validatePeselChars();
            validatePeselLength();
            validateControlSum();
            validateBirthMonth();
            validateBirthDay();
        }
    
        private void validatePeselChars() throws InvalidPeselException {
            if (!peselStr.matches("\\d*"))
                throw new InvalidPeselException(INVALID_PESEL_CONTENT, peselStr);
        }
    
        private void validatePeselLength() throws InvalidPeselException {
            if (peselStr.length() != PESEL_LEN)
                throw new InvalidPeselException(INVALID_PESEL_LENGTH, peselStr);
        }
    
        public LocalDate getBirthDate() throws InvalidPeselException {
            int birthYear = getBirthYear();
            int birthMonth = getBirthMonth();
            int birthDay = getBirthDay();
            return LocalDate.of(birthYear, birthMonth, birthDay);
        }
        
        private int getBirthYear() throws InvalidPeselException {
            int year = (10 * peselValue(0)) + peselValue(1);
            int month = 10 * peselValue(2) + peselValue(3);
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
                throw new InvalidPeselException(INVALID_PESEL_YEAR, peselStr);
            return year;
        }
        
        private int getBirthMonth() throws InvalidPeselException {
            int month = 10 * peselValue(2) + peselValue(3);
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
                throw new InvalidPeselException(INVALID_PESEL_MONTH, peselStr);
            return month;
        }
        
        private int getBirthDay() {
            return 10 * peselValue(4) + peselValue(5);
        }
        
        private void validateControlSum() throws InvalidPeselException {
            int sum = 1 * peselValue(0) +
                    3 * peselValue(1) +
                    7 * peselValue(2) +
                    9 * peselValue(3) +
                    1 * peselValue(4) +
                    3 * peselValue(5) +
                    7 * peselValue(6) +
                    9 * peselValue(7) +
                    1 * peselValue(8) +
                    3 * peselValue(9);
            sum %= 10;
            sum = 10 - sum;
            sum %= 10;
            if (sum != peselValue(10))
                throw new InvalidPeselException(INVALID_PESEL_CONTROL_SUM, peselStr);
        }
        
        private void validateBirthMonth() throws InvalidPeselException {
            int month = getBirthMonth();
            if (month < 1 || month > 12)
                throw new InvalidPeselException(INVALID_PESEL_MONTH, peselStr);
        }
        
        private void validateBirthDay() throws InvalidPeselException {
            int year = getBirthYear();
            int month = getBirthMonth();
            int day = getBirthDay();
            if (day < 1)
                throw new InvalidPeselException(INVALID_PESEL_DAY, peselStr);
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    if (day > 31)
                        throw new InvalidPeselException(INVALID_PESEL_DAY, peselStr);
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    if (day > 30)
                        throw new InvalidPeselException(INVALID_PESEL_DAY, peselStr);
                    break;
                case 2:
                    if ((day > 29 && leapYear(year)) || (day > 28 && !leapYear(year)))
                        throw new InvalidPeselException(INVALID_PESEL_DAY, peselStr);
                    break;
                default:
                    throw new InvalidPeselException(INVALID_PESEL_MONTH, peselStr);
            }
        }
        
        private boolean leapYear(int year) {
            return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0);
        }
        
    }
    
    
}

