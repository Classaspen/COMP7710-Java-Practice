package practice.day20261007;

import java.util.Objects;

public abstract class Worker implements Payable, Comparable<Worker> {
    private final String name;

    protected Worker(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    public final String getName() { return name; }

    @Override
    public int compareTo(Worker other) {
        // TODO Q5: pay ascending, then name ascending; avoid overflow.
        int payorder = Long.compare(this.pay(), other.pay());
        if(payorder != 0) {
            return payorder;
        }
        return name.compareTo(other.name); 
    }
}
