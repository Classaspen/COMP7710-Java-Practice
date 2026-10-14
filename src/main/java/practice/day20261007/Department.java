package practice.day20261007;

import java.util.Objects;

public class Department {
    private final Employee[] employees;

    public Department(Employee[] employees) {
        Objects.requireNonNull(employees, "employees");
        for (Employee employee : employees) Objects.requireNonNull(employee, "employee");
        // TODO Q4: isolate both the array and its mutable elements.
        this.employees = deepCopy(employees);
    }

    public Employee[] getEmployees() {
        // TODO Q4: callers must not be able to mutate our state.
        Objects.requireNonNull(employees, "employees can not be null");
        return deepCopy(employees);
    }

    private static Employee[] deepCopy(Employee[] source) {
        Employee[] copy = new Employee[source.length];
        for (int i = 0; i < source.length; i++) {
            Employee employee = source[i];
            copy[i] = new Employee(employee.getName(), employee.getSalary());
        }
        return copy;
    }

    public long totalSalary() {
        long total = 0;
        for (Employee employee : employees) total += employee.getSalary();
        return total;
    }
}
