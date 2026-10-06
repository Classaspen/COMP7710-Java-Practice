package practice.day20261007;

import java.util.Objects;

public class Department {
    private final Employee[] employees;

    public Department(Employee[] employees) {
        Objects.requireNonNull(employees, "employees");
        for (Employee employee : employees) Objects.requireNonNull(employee, "employee");
        // TODO Q4: isolate both the array and its mutable elements.
        this.employees = employees.clone();
    }

    public Employee[] getEmployees() {
        // TODO Q4: callers must not be able to mutate our state.
        return employees.clone();
    }

    public long totalSalary() {
        long total = 0;
        for (Employee employee : employees) total += employee.getSalary();
        return total;
    }
}
