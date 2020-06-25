package pl.wbsoft.enums;

public enum AllowedCurrencies {
    PLN("PLN"), USD("USD");
    private String name;

    AllowedCurrencies(String curencyName) {
        name = curencyName;
    }

    public static boolean areAllowed(String... curencyNames) {
        try {
            for (String name : curencyNames) {
                AllowedCurrencies.valueOf(name);
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

}
