# Day 1 · 2026-10-07：数组、对象隔离与多态（约 60–90 分钟）

## 今日语法记录清单
1. 具体类型数组：`new Employee[n]` 或 `new Employee[]{...}`；`new Employee[]` 缺少长度/初始化器，无法编译。
2. 泛型类型参数 `T` 不能直接用于 `new T[n]`；不要把具体类名占位符和泛型类型变量混淆。
3. `array.clone()` 复制数组容器；元素为可变对象时仍共享对象引用。
4. Defensive copy 要检查输入和输出两条路径；`final Employee[]` 只限制字段重新赋值。
5. 静态方法没有隐式 `this`；实例方法通过对象动态分派。
6. `Comparable<Worker>` 对应 `public int compareTo(Worker other)`；使用 `@Override` 捕获签名错误。
7. 向下转型要看真实对象；接口数组可以容纳不同实现，不保证全部是某一子类。
8. `int * int` 先用 int 运算，再赋给 long 也救不了溢出；比较 long 不应强转相减结果为 int。
9. 区分编译失败、运行异常、断言失败；本日 starter 可编译，但有逻辑缺陷。

## 1. 单选：浅复制
```java
Employee[] a = {new Employee("Ada", 100)};
Employee[] b = a.clone();
b[0].setSalary(200);
b = new Employee[]{new Employee("Bob", 300)};
System.out.println(a[0].getSalary());
```
A. 100　B. 200　C. 300　D. 编译失败

## 2. 单选：编译与运行
```java
Payable p = new HourlyWorker("Bo", 2, 30);
SalariedWorker s = (SalariedWorker) p;
System.out.println(s.pay());
```
A. 输出 60　B. 编译失败　C. 转型时抛出 ClassCastException　D. 输出 0

## 3. 多选：每项独立判断，选出所有正确陈述
A. `Employee[] x = new Employee[0];` 可以编译。
B. 在泛型类 `Box<T>` 中，`T[] x = new T[3];` 可以编译。
C. `final Employee[] x = new Employee[1];` 之后可以给 `x[0]` 赋值。
D. 数组 clone 后，修改其中 Employee 的工资绝不会影响原数组的元素。
E. `Worker` 实现 `Comparable<Worker>` 时，`compareTo(Object)` 可以直接替代 `compareTo(Worker)`。
F. 实现接口实例方法 `pay()` 时，不能改成 static 方法。

## 4. 编程改错：Department 的防御性复制
编辑 [Department.java](../../src/main/java/practice/day20261007/Department.java)，可添加 private helper，保留公开 API。
- 构造函数保存输入在调用时的快照：数组和每个可变 Employee 都必须隔离。
- `getEmployees()` 每次返回独立快照；保持姓名、工资、顺序和元素数量。
- 输入数组、元素为 null 时抛 NullPointerException；空数组合法。
- `totalSalary()` 返回 long，总额允许超过 int 上限。
- Employee 姓名不可为 null，工资非负；保留现有校验。
先画出 clone 前后的引用图，再修复。禁止通过把 Employee 改成不可变类绕过本题。

## 5. 编程改错：工资系统的类间关系
编辑 [Worker.java](../../src/main/java/practice/day20261007/Worker.java)、
[HourlyWorker.java](../../src/main/java/practice/day20261007/HourlyWorker.java)、
[Payroll.java](../../src/main/java/practice/day20261007/Payroll.java)。
- SalariedWorker 的 pay 为 monthlyPay；HourlyWorker 的 pay 为 hours × rate，使用 long 运算。
- Payroll.total 支持任意 Payable，包括非 Worker 实现；不修改输入，空数组返回 0。
- Payroll.total 的数组或元素为 null 时抛 NullPointerException。
- Worker 按 pay 升序，再按姓名 String.compareTo 升序；同工资同姓名返回 0。
- 不能通过不安全向下转型、字符串化金额、相减后强转 int 实现比较。
- 保留参数校验；本题约定总工资不超过 Long.MAX_VALUE。

## 运行与自查
从仓库根目录运行：
```bash
mvn "-Dtest=Practice20261007Test" test
```
测试位于 [Practice20261007Test.java](../../src/test/java/practice/day20261007/Practice20261007Test.java)。
命名采用日期 + Test，并加 Practice 前缀，因为 Java 类名不能以数字开头或包含连字符。

初始版本故意有测试失败；完成标准是全部 14 个测试通过，且能解释每个修复。
不要删除测试、添加 Disabled、忽略失败或直接硬编码测试答案。

<details>
<summary>完成后展开：选择题核对与提示</summary>

1. B：clone 共享元素；重新赋值 b 不会恢复对象状态。
2. C：接口引用允许该强转通过编译，但真实对象不是 SalariedWorker。
3. A、C、F。泛型数组创建受限；final 不冻结数组；泛型 Comparable 的方法签名必须匹配。
编程提示：分别跟踪容器和元素的所有权；检查运算发生时的类型；让接口方法完成动态分派。

</details>
