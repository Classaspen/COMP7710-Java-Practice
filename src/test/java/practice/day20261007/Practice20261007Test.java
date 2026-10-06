package practice.day20261007;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class Practice20261007Test {
    @Test void departmentCopiesInputArray() {
        Employee[] input = {new Employee("Ada", 100)};
        Department d = new Department(input);
        input[0] = new Employee("Bob", 900);
        assertEquals(100L, d.totalSalary());
    }
    @Test void departmentCopiesInputElements() {
        Employee e = new Employee("Ada", 100);
        Department d = new Department(new Employee[]{e});
        e.setSalary(900);
        assertEquals(100L, d.totalSalary());
    }
    @Test void departmentCopiesOutputArray() {
        Department d = new Department(new Employee[]{new Employee("Ada", 100)});
        Employee[] out = d.getEmployees();
        out[0] = new Employee("Bob", 900);
        assertEquals("Ada", d.getEmployees()[0].getName());
        assertEquals(100L, d.totalSalary());
    }
    @Test void departmentCopiesOutputElements() {
        Department d = new Department(new Employee[]{new Employee("Ada", 100)});
        d.getEmployees()[0].setSalary(900);
        assertEquals(100L, d.totalSalary());
    }
    @Test void departmentHandlesEmptyAndLargeTotal() {
        assertEquals(0L, new Department(new Employee[0]).totalSalary());
        Department d = new Department(new Employee[]{
            new Employee("A", Integer.MAX_VALUE), new Employee("B", Integer.MAX_VALUE)});
        assertEquals(4294967294L, d.totalSalary());
        assertEquals(2, d.getEmployees().length);
        assertEquals("B", d.getEmployees()[1].getName());
    }
    @Test void departmentRejectsNull() {
        assertThrows(NullPointerException.class, () -> new Department(null));
        assertThrows(NullPointerException.class, () -> new Department(new Employee[]{null}));
    }
    @Test void employeeValidation() {
        assertThrows(NullPointerException.class, () -> new Employee(null, 0));
        assertThrows(IllegalArgumentException.class, () -> new Employee("A", -1));
        assertThrows(IllegalArgumentException.class, () -> new Employee("A", 0).setSalary(-1));
    }
    @Test void payrollUsesPolymorphism() {
        Payable[] items = {new SalariedWorker("A", 100), new HourlyWorker("B", 3, 20), () -> 7L};
        assertEquals(167L, Payroll.total(items));
        assertEquals(0L, Payroll.total(new Payable[0]));
    }
    @Test void payrollDoesNotModifyInput() {
        Payable a = new SalariedWorker("A", 10);
        Payable b = new SalariedWorker("B", 20);
        Payable[] items = {a, b};
        assertEquals(30L, Payroll.total(items));
        assertSame(a, items[0]);
        assertSame(b, items[1]);
    }
    @Test void hourlyMultiplicationUsesLong() {
        assertEquals(2500000000L, new HourlyWorker("A", 50000, 50000).pay());
        assertEquals(0L, new HourlyWorker("B", 0, 100).pay());
    }
    @Test void comparisonBreaksTiesByName() {
        Worker a = new SalariedWorker("Ada", 100);
        Worker b = new HourlyWorker("Bob", 2, 50);
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(new SalariedWorker("Ada", 100)));
    }
    @Test void comparisonAvoidsNarrowingAndSubtractionOverflow() {
        Worker low = new SalariedWorker("Z", 0);
        Worker high = new SalariedWorker("A", Long.MAX_VALUE);
        assertTrue(low.compareTo(high) < 0);
        assertTrue(high.compareTo(low) > 0);
        assertEquals(0, high.compareTo(high));
    }
    @Test void sortingUsesPayThenNameAcrossSubclasses() {
        Worker[] workers = {new SalariedWorker("Z", 50), new HourlyWorker("B", 1, 10),
            new SalariedWorker("A", 10), new SalariedWorker("N", 0)};
        Arrays.sort(workers);
        assertArrayEquals(new String[]{"N", "A", "B", "Z"},
            Arrays.stream(workers).map(Worker::getName).toArray(String[]::new));
    }
    @Test void workerAndPayrollValidation() {
        assertThrows(NullPointerException.class, () -> new SalariedWorker(null, 0));
        assertThrows(IllegalArgumentException.class, () -> new SalariedWorker("A", -1));
        assertThrows(IllegalArgumentException.class, () -> new HourlyWorker("A", -1, 1));
        assertThrows(IllegalArgumentException.class, () -> new HourlyWorker("A", 1, -1));
        assertThrows(NullPointerException.class, () -> Payroll.total(null));
        assertThrows(NullPointerException.class, () -> Payroll.total(new Payable[]{null}));
    }
}
