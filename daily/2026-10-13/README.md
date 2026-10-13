# 今日语法记录清单（2026-10-13 · Day 7）

**训练时间：50–60 分钟。** 进入接口 / 多态 / 泛型集合综合题；数组题由“递增”升级为“相邻差值不变”，`countVisits` 以命令处理器的形式再次复习。

| # | 规则 | 正确写法 | 常见错误 | 为什么 |
|---|---|---|---|---|
| 1 | 重载看**编译时类型**，重写看**运行时类型** | `Base b=new Child(); b.f("x")` 先选 Base 的 `f(String)` | 误以为 Child 新增的同名重载必被调用 | 重载解析发生在编译期。 |
| 2 | 接口变量只能调用接口可见成员 | `Command c=new MoveCommand("R",2); c.steps()` | `c.someSubclassOnlyMethod()` | 实际对象是子类，不代表静态类型有子类方法。 |
| 3 | 泛型集合不协变 | `List<? extends Command> x=moves` | `List<Command> x = new ArrayList<MoveCommand>()` | 否则可向只允许 MoveCommand 的列表加入 WaitCommand。 |
| 4 | 泛型方法需要保留类型参数 | `static <T extends Command> List<T> sortedCopy(List<T> input)` | 直接返回 `List<Command>` 导致调用方丢失 T | 上界限制可用的方法，T 保留元素类型。 |
| 5 | Comparator 避免整型溢出 | `Comparator.comparingLong(c -> Math.abs((long)c.steps()))` | `Math.abs(c.steps())` 对 `Integer.MIN_VALUE` 为负 | `int` 的最小值无对应的正 `int`。 |
| 6 | 列表复制和不可变视图不同 | `new ArrayList<>(input)`；`List.copyOf(labels)` | `this.labels=input` | 共享可变列表会让快照随外部修改。 |
| 7 | 数组复制仍须双向保护 | 构造时 `counts.clone()`；getter 也 `clone()` | 只复制构造输入，getter 返回内部数组 | `final int[]` 只禁止重新赋引用，不能阻止修改元素。 |
| 8 | 算术连续段：重置长度为 **2** | 差值改变时 `current=2; start=i-1` | 沿用递增段的 `current=1` | 新段已包含当前与前一个元素两个数。 |
| 9 | 相邻差值要用 long | `long d=(long)a[i]-a[i-1]` | `long d=a[i]-a[i-1]` | 右边先以 `int` 运算，溢出后再转换已太迟。 |
| 10 | `countVisits` 模拟先验界、后执行 | `long target=(long)pos+steps;` 合法后逐格 `counts[pos]++` | 每步都从 start 算；只判上界；`counts[j]=1` | 必须维护动态位置、双向边界与重复访问次数。 |

## 题 1 · 单选（overload + override + interface）

```java
interface Named { default String label() { return "N"; } }
class Base {
    String show(Object x) { return "B:Object"; }
    String show(String x) { return "B:String"; }
}
class Child extends Base implements Named {
    @Override String show(Object x) { return "C:Object"; }
    @Override public String label() { return "Child"; }
}
Base b = new Child();
System.out.println(b.show("hi") + "/" + b.show((Object) "hi") + "/" + ((Named)b).label());
```

A. `C:Object/C:Object/Child`　B. `B:String/C:Object/Child`　C. `B:String/B:Object/N`　D. 编译失败

## 题 2 · 单选（对象引用 / clone / final）

```java
class Box<T> {
    private final T[] items;
    Box(T[] input) { items = input.clone(); }
    T[] copy() { return items.clone(); }
}
String[] src = {"A"};
Box<String> box = new Box<>(src);
src[0] = "B";
String[] out = box.copy();
out[0] = "C";
System.out.println(box.copy()[0] + ":" + (src == out));
```

A. `C:true`　B. `A:false`　C. `B:false`　D. 编译失败

## 题 3 · 多选（逐项独立判断）

以下选项假定已导入 `java.util.*`，且题 5 的 `Command`、`MoveCommand`、`WaitCommand` 均已定义。

A. `Command[] a = new Command[2];` 合法。  
B. 泛型方法里 `T[] a = new T[2];` 合法。  
C. 对 `T[] input`，`T[] copy = input.clone();` 合法。  
D. `Command[] a = new Command[]{new WaitCommand()};` 合法。  
E. `Command c = new MoveCommand("X",1); c.steps();` 合法。  
F. `Command c = new MoveCommand("X",1); c.getSteps();` 合法。  
G. `Math.abs(Integer.MIN_VALUE) < 0` 为 true。  
H. `List<Command> x = new ArrayList<MoveCommand>();` 合法。  
I. `List<? extends Command> x = new ArrayList<MoveCommand>();` 合法。  
J. `final List<Command> x = new ArrayList<>(); x.add(new WaitCommand());` 合法。  
K. 静态方法可以不经对象直接读取同类实例字段。

## 题 4 · 编程（12–15 分钟）：ArithmeticRuns

完成 `ArithmeticRuns.longestArithmeticRun(int[] values)`，返回 `{start,length}`，代表**相邻差值始终相同**的最长连续子数组；并列选最早起点。

- 前置条件：`values != null`；不能修改输入。
- 空数组返回 `{-1,0}`，单元素返回 `{0,1}`；两个元素必然构成长度为 2 的算术段。
- `{1,3,5,7,8,9}` → `{0,4}`。
- `{1,2,3,9,11,13,15}` → `{3,4}`。
- `{1,2,3,10,20,30}` → `{0,3}`（并列取最早）。
- `{5,5,5,5}` → `{0,4}`。
- `Integer.MIN_VALUE` 与 `Integer.MAX_VALUE` 的差必须按 `long` 计算。
- 时间复杂度 O(n)，额外空间 O(1)（不计返回数组）。特别关注差值改变时的重置起点与长度。

## 题 5 · 编程（25–30 分钟）：CommandRunner（接口 + 泛型 List + 状态模拟）

给定 `Command` 接口和 `WaitCommand`，补全 `MoveCommand`、`CommandRunner`、`RunReport`。

**Command**：`int steps()`、`String label()`。`WaitCommand` 是合法的 0 步命令，标签固定 `"WAIT"`。

**MoveCommand**：`new MoveCommand(String label,int steps)`；标签不能为 null 或 blank，否则抛 `IllegalArgumentException`；steps 可以是任意 int。两个 getter 返回构造时数据。对象应保持不可变。

**CommandRunner** 是无状态工具类：

```java
public static <T extends Command> RunReport execute(int size, int start, List<T> commands)
public static <T extends Command> List<T> sortedCopyByMagnitude(List<T> commands)
```

`execute`：
- `size>0`，`0<=start<size`，否则 `IllegalArgumentException`；`commands` 不能为 null，否则 `NullPointerException`。
- 初始 `counts[start]=1`，逐个执行命令；每次都从**当前**位置开始。
- 0 步是有效命令：记录标签，不新增访问次数；正负步逐格计数（不计原位置，计终点）。
- 若整步目标越界，则这步**完全不执行**，不记录标签，立刻停止并忽略后续命令；`stopped=true`。避免 `int` 加法溢出。
- 有效命令的 `label()` 依次进入报告。遇到 null 命令且该命令实际被执行到时抛 `NullPointerException`；无效命令之后的剩余项不得访问。
- 不修改传入的 List 或 Command。

`sortedCopyByMagnitude`：返回新的 `List<T>`，先按 `abs(steps)` **升序**，再按 `label()` 字典序**升序**；注意 `Integer.MIN_VALUE`；输入不变；拒绝 null 列表或元素。

**RunReport**：构造签名为 `RunReport(int position,boolean stopped,int[] counts,List<String> executedLabels)`。保存不可变快照：构造时复制数组和列表，`visitCounts()` 返回新的数组，`executedLabels()` 返回不可修改的列表。另有 `position()`、`stopped()`。数组 / 列表为 null 时抛 `NullPointerException`。

这是 `countVisits` 的**第 3 种形态**：不再直接读 `int[] moves`，而是通过接口动态分派读取命令，并返回防御性快照。

## 运行与评分

从仓库根目录：

```bash
mvn test-compile
mvn "-Dtest=Practice20261013Test" test
```

测试文件为 `2026-10-13Test.java`，测试类为非 public 的 `Practice20261013Test`（Java 类名不能以数字开头）。Starter 故意保持可编译但测试失败；不要先阅读测试实现。

<details><summary>核对提示（做完再展开）</summary>

- 单选：1.B；2.B。多选：A、C、D、E、G、I、J。
- 题 4：差值改变后，新段至少有两个元素；只在 `current > best` 时替换最早起点。
- 题 5：目标先用 long 判界；位置随每格变化；重复访问 `++`；无效命令的标签不能记录；输入 List 不修改；数组 / List 在报告中不共享可变状态。

</details>
