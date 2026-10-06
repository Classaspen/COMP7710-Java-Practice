package practice.day20261007;

public class SalariedWorker extends Worker {
    private final long monthlyPay;

    public SalariedWorker(String name, long monthlyPay) {
        super(name);
        if (monthlyPay < 0) throw new IllegalArgumentException("monthlyPay");
        this.monthlyPay = monthlyPay;
    }

    @Override
    public long pay() { return monthlyPay; }
}
