# 今日语法记录清单｜2026-10-14（Day 8）

预计 50–60 分钟。今天是继承 / 抽象类 / 接口 / Comparable / Comparator / 泛型集合 / downcasting 的综合训练。先独立答题，后运行 JUnit 测试。**Starter 可编译，但 TODO 尚未实现。**

## 10 个高风险语法点（规则 + 正确写法 + 错误 + 原因）

1. 抽象类数组：`SensorEvent[] a=new SensorEvent[3];` 正确；`new SensorEvent(...)` 错误。抽象类不能实例化，但可以存储其引用。
2. Overload/override：`Base b=new Child(); b.show("x")` 先按 Base 选重载，再按 Child 选重写；误以为子类新增的同名重载一定被调用。
3. Downcast：`if(e instanceof MotionEvent m) m.movement();`；盲目 `(MotionEvent)e` 可能编译通过却运行时抛 `ClassCastException`。
4. 接口方法：`@Override public int severity()`；不能写成 protected，因为实现接口时不能缩小 public 可见性。
5. Comparable：`public int compareTo(SensorEvent other)`；不能写 static 或 `compareTo(Object)` 冒充实现；用 `Integer.compare` 避免减法溢出。
6. Comparator：`Comparator.comparingInt(SensorEvent::severity)`；独立于自然顺序，`list.sort(c)` 会修改被排序的列表。
7. 泛型不协变：`List<? extends SensorEvent> x=new ArrayList<MotionEvent>();` 正确；`List<SensorEvent> x=new ArrayList<MotionEvent>()` 错误，防止不安全插入。
8. 泛型方法：`static <T extends SensorEvent> List<T> sortedCopy(List<T> x)`；不能随意丢失 T 返回 `List<SensorEvent>`。
9. 快照：`new ArrayList<>(input)`、`List.copyOf(input)` 复制列表结构；直接保存 input 会共享可变列表；复制列表仍是浅拷贝。
10. 单次扫描：`(a[i]&1)!=(a[i-1]&1)` 处理负奇数；不满足时 `current=1`；别用负数 `%2` 结果直接判断奇偶，别漏掉尾段。

## 题 1｜单选（overload + override）

```java
class Base { String show(Object x){return "Base:Object";} }
class Child extends Base {
    @Override String show(Object x){return "Child:Object";}
    String show(String x){return "Child:String";}
}
Base b=new Child();
System.out.println(b.show("abc"));
```

A. Base:Object　B. Child:Object　C. Child:String　D. compile error

## 题 2｜单选（downcasting）

```java
SensorEvent e=new BatteryEvent("pack",3,50);
System.out.println(e instanceof MotionEvent);
MotionEvent m=(MotionEvent)e;
System.out.println(m.movement());
```

A. 输出 false，然后抛 ClassCastException　B. false、0　C. compile error　D. true、50

## 题 3｜多选（独立判断；已 import java.util.*）

A. `SensorEvent[] a=new SensorEvent[2];` 合法（即使抽象类）。  
B. `new SensorEvent("x",1)` 合法。  
C. `List<? extends SensorEvent> a=new ArrayList<MotionEvent>();` 合法。  
D. 对 C 中的 a，`a.add(new MotionEvent("m",1,2));` 合法。  
E. 泛型方法中 `T[] a=new T[3];` 合法。  
F. `final List<SensorEvent> a=new ArrayList<>(); a.add(new BatteryEvent("b",1,30));` 合法。  
G. `List<SensorEvent> a=new ArrayList<MotionEvent>();` 合法。  
H. `new SensorEvent[]{new MotionEvent("m",1,2)}` 合法。  
I. `List.copyOf(source)` 不可修改，且 source 含 null 元素时抛 NPE。  
J. `new ArrayList<>(source)` 会深拷贝列表内所有对象。

## 题 4｜编程（12–15 分钟）：ParityRuns

实现 `public static int[] longestAlternatingParityRun(int[] values)`：返回最长连续**奇偶交替段**的 `{start,length}`，并列取最早起点。非 null 输入；空数组 `{-1,0}`，单元素 `{0,1}`。只能一次 for 循环，O(n) 时间 / O(1) 额外空间，不得修改输入。负数、Integer.MIN_VALUE 也要正确。

示例：`{2,5,8,11,14}->{0,5}`；`{2,4,7,10,13}->{1,4}`；`{3,3,4,5,6}->{1,4}`；`{0,-1,-2,-3}->{0,4}`；`{2,3,8,8,9,4}->{0,3}`。注意 current 初始 1，失败重置为 1，严格更长时才更新最早起点。

## 题 5｜编程（25–30 分钟）：Sensor Event Board

只补 `SensorEvent.java`、`MotionEvent.java`、`BatteryEvent.java`、`EventBoard.java` 的 TODO。给定 `Alertable` 接口（`int severity()`）。所有文件在 `practice.day20261014` 包。

- `SensorEvent` 抽象类：构造器校验 source 非 null/blank、priority 为 0–10 已给；`summary()` 输出 `KIND:source:severity`；自然顺序 `compareTo`：priority **降序**，再 source **升序**，再 kind **升序**；null 参数抛 NPE。
- `MotionEvent(source,priority,movement)`：movement 0–100；`kind()` 为 MOTION，`severity()` = priority + movement。
- `BatteryEvent(source,priority,percentage)`：percentage 0–100；`kind()` 为 BATTERY，`severity()` = priority + (100-percentage)。
- `EventBoard(List<? extends SensorEvent> initial)`：拒绝 null 列表/元素，防御性复制。
- `add(SensorEvent)` 拒绝 null；`snapshot()` 返回不可修改的**独立快照**，后续 add 不影响旧快照。
- `highestSeverity()` 用多态 severity 取最大值，并列取**最早加入**的对象；空返回 null。
- `<T extends SensorEvent> T firstOfType(Class<T> type)`：第一个运行时类型匹配的对象，未找到返回 null；type 为 null 抛 NPE；用 `type.isInstance` 和 `type.cast`。
- `static <T extends SensorEvent> List<T> sortedCopy(List<T> input)`：返回新的**可修改**列表，按自然顺序排序；不修改输入，拒绝 null 列表/元素。
- `static Comparator<SensorEvent> bySeverityThenSource()`：severity **升序**，source **升序**，kind **升序**。与自然顺序相互独立。

## countVisits 错题回访（2 分钟，额外）

手算 `size=6,start=3,moves={-2,2,0,1,4,-1}` 的最终位置、访问次数、停止步骤。起点先记一次；每步从 current 开始；正负逐格 `counts[pos]++`；0 步不计数；整步越界则不执行该步并忽略余下命令。

## 运行

```bash
mvn test-compile
mvn "-Dtest=Practice20261014Test" test
```

测试文件为 `2026-10-14Test.java`，测试类名 `Practice20261014Test`（非 public，因为 Java 标识符不能以数字开头）。

<details><summary>核对提示（先做题再看）</summary>

单选 1.B、2.A；多选 A、C、F、H、I。题 4 用 `&1` 判断奇偶、并列不更新；题 5 注意 public override、泛型类型保留、浅拷贝与快照、动态分派、safe cast。countVisits：第 5 个 move（+4）无效，最终位置 4，次数 `{0,1,2,2,1,0}`。

</details>
