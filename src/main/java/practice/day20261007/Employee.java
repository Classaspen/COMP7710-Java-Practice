package practice.day20261007;

import java.util.Objects;

/** Mutable element: cloning Employee[] alone does not copy these objects. */
public class Employee {
    private final String name;
    private int salary;

    public Employee(String name, int salary) {
        this.name = Objects.requireNonNull(name, "name");
        setSalary(salary);
    }

    public String getName() { return name; }
    public int getSalary() { return salary; }

    public void setSalary(int salary) {
        if (salary < 0) throw new IllegalArgumentException("salary");
        this.salary = salary;
    }
}
