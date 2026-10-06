package practice.day20261007;

import java.util.Objects;

public final class Payroll {
    private Payroll() {}

    public static long total(Payable[] items) {
        Objects.requireNonNull(items, "items");
        long total = 0;
        for (Payable item : items) {
            Objects.requireNonNull(item, "item");
            // TODO Q5: support every Payable implementation.
            total += ((SalariedWorker) item).pay();
        }
        return total;
    }
}
