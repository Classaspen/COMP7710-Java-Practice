package practice.day20261007;

public class HourlyWorker extends Worker {
    private final int hours;
    private final int rate;

    public HourlyWorker(String name, int hours, int rate) {
        super(name);
        if (hours < 0 || rate < 0) throw new IllegalArgumentException("hours/rate");
        this.hours = hours;
        this.rate = rate;
    }

    @Override
    public long pay() {
        // TODO Q5: check the type used during multiplication.
        return (long)hours * rate;
    }
}
