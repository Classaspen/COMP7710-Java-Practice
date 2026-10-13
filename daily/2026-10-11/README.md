# 今日语法记录清单 — 2026-10-11 / Day 5

**目标：** 45–60 分钟；继承 / 抽象类 / 接口 / Comparable / Comparator / 泛型 + 数组单次扫描。先独立答题，再运行测试。**今天的 Starter 可以编译，但逻辑尚未完成。**

| 高风险点 | 正确写法 | 常见错误与原因 |
|---|---|---|
| 1. Overload 与 Override | `@Override public String label(Object x)` | overload 在**编译期**按参数静态类型选择；override 在**运行期**按对象真实类型分派。 |
| 2. 抽象类构造器 | 子类构造器 `super(title, severity);` | 抽象类不能直接 `new`，但可以有构造器和字段；子类必须先完成父类初始化。 |
| 3. 接口与多态 | `WorkItem x = new BugItem(...); x.effortPoints();` | 编译器只允许调用静态类型中声明的方法；执行的是子类重写的方法。 |
| 4. Comparable | `class WorkItem implements Comparable<WorkItem>` / `public int compareTo(WorkItem other)` | 不要写成 `compareTo(Object)`；不能缩小 `public` 可见性；用 `Integer.compare(a,b)` 而非 `a-b`。 |
| 5. Comparator | `Comparator.comparingInt(WorkItem::effortPoints)` | Comparator 是外部排序规则，不必修改对象的自然顺序。 |
| 6. 泛型不协变 | `List<BugItem>` **不是** `List<WorkItem>` | `List<? extends WorkItem>` 适合只读；不能向其中添加任意 BugItem。 |
| 7. 泛型方法 | `<T extends WorkItem> T highestEffort(List<T> items)` | `<T ...>` 必须写在返回类型前；调用 `List<BugItem>` 后返回类型仍为 `BugItem`。 |
| 8. 数组浅拷贝 | `WorkItem[] copy = input.clone();` | 数组容器独立，但其中元素对象引用仍共享；`copy=input` 连容器都共享。 |
| 9. 单次扫描状态 | 相邻差先转 `long`，再判断正负；相同元素重置 `current=1` | `values[i]-values[i-1]` 的 `int` 可能溢出；`i < values.length` 防止访问越界。 |
| 10. 错误分类 | 方法签名错误是 compile error；非法 downcast 是 runtime error；排序不符合预期是 test failure | 编译错误会让整个测试类无法开始运行。 |

## 题 1 · 单选（overload + override，约 3 分钟）

```java
class Base {
    String show(Object x) { return "B:Object"; }
    String show(String x) { return "B:String"; }
}
class Child extends Base {
    @Override String show(Object x) { return "C:Object"; }
}
Base x = new Child();
System.out.println(x.show("hi") + "/" + x.show((Object) "hi"));
```

A. `B:String/C:Object`　B. `C:Object/C:Object`　C. `B:String/B:Object`　D. 编译错误。

## 题 2 · 单选（泛型与编译错误，约 3 分钟）

下列哪一行**不能编译**？每行独立判断，假设 `BugItem extends WorkItem`。

A. `WorkItem item = new BugItem("A", 3, 1);`  
B. `List<? extends WorkItem> items = new ArrayList<BugItem>();`  
C. `List<WorkItem> items = new ArrayList<BugItem>();`  
D. `Comparator<WorkItem> c = Comparator.comparingInt(WorkItem::effortPoints);`

## 题 3 · 多选（类型、复制、static，约 5 分钟）

选择所有正确陈述：

A. `WorkItem[] copy = items.clone();` 会产生新数组，但内部对象仍共享。  
B. `WorkItem[] a = new WorkItem[];` 合法。  
C. `List<WorkItem> a = new ArrayList<BugItem>();` 合法。  
D. `Comparator.comparingInt(WorkItem::effortPoints)` 可以用作比较器。  
E. `WorkItem w = new BugItem("B", 3, 2); w.category();` 合法。  
F. `WorkItem.compareTo(other)` 可以直接调用实例方法。  
G. `List<? extends WorkItem> x = new ArrayList<BugItem>(); x.add(new BugItem("X",1,0));` 合法。  
H. `Integer.compare(a,b)` 可避免 `a-b` 计算差值时的溢出问题。  
I. `WorkItem[] a = new WorkItem[2];` 合法。

## 题 4 · 编程（15–20 分钟）— `ArrayRuns.java`

实现两个 `public static` 方法，要求 **O(n) 时间、O(1) 额外空间**，输入 `values` 非 null，且不得修改它。

1. `longestZigzagRun(int[] values)`：最长连续**严格锯齿段**的长度。相邻差值必须非零，且正负方向交替。空数组返回 0；非空数组至少返回 1。示例：`{1,3,2,4,3}` → 5；`{1,1,2}` → 2；`{5,5,5}` → 1；`{Integer.MIN_VALUE,Integer.MAX_VALUE,Integer.MIN_VALUE}` → 3。
2. `earliestLongestZigzagStart(int[] values)`：上述最长段的最早起始下标。空数组返回 -1；并列最长段取最早出现的一个。示例：`{1,3,2,4,3}` → 0；`{1,1,2}` → 1；`{5,5,5}` → 0。

**陷阱：** 方向连续相同（例如 `1,2,3`）时，新段仍可从前一个元素开始，长度为 2；相等时新段只能从当前元素开始，长度为 1；不要用 `int` 相减判断方向。

## 题 5 · 编程（25–30 分钟）— WorkItem 任务管理器

给定 4 个文件 `WorkItem.java`、`BugItem.java`、`FeatureItem.java`、`WorkItemTools.java`，只补 `TODO`，不要改 public API。

- `WorkItem`：抽象父类，实现 `Comparable<WorkItem>`；字段 `title` 和 `severity` 已提供，构造器验证已写好。`category()` 与 `effortPoints()` 是抽象实例方法。`summary()` 已给出，观察动态分派。实现 `compareTo`：**severity 降序**，相同 severity 时 **title 按 String.compareTo 升序**。
- `BugItem`：构造器 `(String title,int severity,int reproductions)`；`reproductions` 在 0–100。`category()` 返回 `"BUG"`；`effortPoints()` = `severity * 2 + reproductions`。
- `FeatureItem`：构造器 `(String title,int severity,int complexity)`；`complexity` 在 1–5。`category()` 返回 `"FEATURE"`；`effortPoints()` = `severity + complexity * 3`。
- `WorkItemTools.highestEffort(List<T>)`：声明 `<T extends WorkItem> T`，返回 effortPoints 最大的**原对象**，并列取最先出现的；空列表返回 null，列表为 null抛 NPE，元素为 null 抛 `IllegalArgumentException`。不能显式 cast。
- `WorkItemTools.sortedCopy(WorkItem[])`：按自然顺序排序，**不能改变输入数组顺序**，返回不同的数组；可共享元素对象引用。null 输入抛 NPE。
- `WorkItemTools.byEffortThenTitle()`：返回 `Comparator<WorkItem>`；effortPoints **升序**，相同时 title **字典序升序**（区分大小写）。不能用减法比较整数。

注意：**不要把 `compareTo` 写成 static；不要用 `new WorkItem[]`（不带长度或元素）；不要把 `List<BugItem>` 强转成 `List<WorkItem>`。**

### 错题回访（不计入 5 题）

不用写代码，心算 `countVisits(new int[]{0,-1,1}, 2, 4)` 的输出。提醒：起点先记一次；`0` 步不增加访问；负向逐格计数；返回时可能同一位置访问多次。

## 运行方法

在仓库根目录执行：

```bash
mvn test-compile
mvn "-Dtest=Practice20261011Test" test
```

今天测试源码文件**按日期命名**为 `2026-10-11Test.java`，其中测试类名 `Practice20261011Test` 不带 `public`（Java 的 public 类名不能以数字开头）。

<details><summary>核对提示（完成后再看）</summary>

- 单选：1.A；2.C。多选：A、D、E、H、I。
- 题 4：维护 `current`、`best`、`start` 和上一次方向；相等元素会重置；并列只在 `current > best` 时更新。
- 题 5：`Integer.compare(other.severity(), this.severity())` 的方向；`clone()`；`<T extends WorkItem>`；多态调用；泛型返回值；`Comparator` 的第二关键字。
- 错题回访：`{0,1,2,0}`。

</details>
