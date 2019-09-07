package cashmachine;

import cashmachine.exception.NotEnoughMoneyException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CurrencyManipulator {
    private String currencyCode;
    private Map<Integer, Integer> denominations = new TreeMap<>(Collections.reverseOrder());

    public CurrencyManipulator(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public int getTotalAmount() {
        int totalAmount = 0;
        for (Map.Entry<Integer, Integer> pair : denominations.entrySet())
            totalAmount += pair.getKey() * pair.getValue();

        return totalAmount;
    }

    public boolean hasMoney() {
        return !denominations.isEmpty();
    }

    public boolean isAmountAvailable(int expectedAmount) {
        return getTotalAmount() >= expectedAmount;
    }

    public Map<Integer, Integer> withdrawAmount(int expectedAmount) throws NotEnoughMoneyException {
        int currentAmount = 0;
        HashMap<Integer, Integer> backup = new HashMap<>(denominations);
        HashMap<Integer, Integer> withdrawBanknotes = new HashMap<>();
        for (Map.Entry<Integer, Integer> pair : denominations.entrySet()) {
            while (pair.getValue() != 0 && currentAmount < expectedAmount) {
                if (pair.getKey() <= expectedAmount - currentAmount) {
                    currentAmount += pair.getKey();
                    denominations.put(pair.getKey(), pair.getValue() - 1);
                    if (withdrawBanknotes.containsKey(pair.getKey())) {
                        withdrawBanknotes.put(pair.getKey(), withdrawBanknotes.get(pair.getKey()) + 1);
                    } else
                        withdrawBanknotes.put(pair.getKey(), 1);
                } else {
                    break;
                }
            }
            if (currentAmount == expectedAmount) {
                return withdrawBanknotes;
            }
        }
        denominations.clear();
        denominations.putAll(backup);
        throw new NotEnoughMoneyException();
    }

    public void addAmount(int denomination, int count) {
        if (this.denominations.containsKey(denomination))
            this.denominations.put(denomination, this.denominations.get(denomination) + count);
        else
            this.denominations.put(denomination, count);

        ConsoleHelper.writeMessage("Successfully added.");
    }
}
