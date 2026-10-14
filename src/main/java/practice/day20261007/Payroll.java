package practice.day20261007;

import java.util.Objects;

public final class Payroll {
    private Payroll() {}

    public static long total(Payable[] items) {
        if(items == null) {
            throw new NullPointerException("items");
        }
        long total = 0;
        for (Payable item : items) {
            Objects.requireNonNull(item, "item");
            // TODO Q5: support every Payable implementation.
            total += ( item).pay();
            if(item == null){
                throw new NullPointerException("item");
            }
        }

        return total;
    }
}
