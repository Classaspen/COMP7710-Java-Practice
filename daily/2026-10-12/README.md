# 今日语法记录清单（2026-10-12 · Day 6）

**目标：50–60 分钟。** 今日进入接口、Comparable、泛型和多态的组合训练，同时复习数组扫描与 countVisits 错题。

1. **泛型数组**：正确 `T[] copy = input.clone()`；错误 `new T[n]`（compile error）。因为 Java 泛型类型擦除，不允许直接创建类型变量数组。
2. **数组创建**：`new Player[n]`、`new Player[]{p1,p2}` 正确；`new Player[]` 错误（缺长度或初始化器）。
3. **Comparable**：`a.compareTo(b)` 正确；`Player.compareTo(b)` 错误（实例方法）。`Comparable<Player>` 要实现 `public int compareTo(Player other)`。
4. **避免比较溢出**：`Integer.compare(b.score(), a.score())` 正确；`b.score()-a.score()` 可能溢出。
5. **重载与重写**：重载由变量的静态类型确定；重写由实际对象类型决定。先选签名，再做动态分派。
6. **接口引用**：`Rated r = new Player("a",3); r.score()` 正确；`r.compareTo(...)` 不可编译，因为 Rated 没有声明它。
7. **defensive copy**：构造器保存 `input.clone()`，getter 返回 `members.clone()`；只复制一端仍可能泄露内部数组。
8. **最长连续段**：相邻条件成立时 `current++`，不成立时 `current=1; start=i`；只在 `current>best` 更新，保留最早并列起点。
9. **整数溢出**：`Math.abs((long)a[i]-a[i-1])` 正确；`Math.abs(a[i]-a[i-1])` 对极端整数可能溢出。
10. **countVisits 复盘**：每一步要更新 position，逐格 `counts[position]++`；0 步不计数；无效移动必须整体拒绝并停止；不能 `counts[j]=1`。

## 题 1 · 单选（重载 + 重写 + 接口）

以下类均位于同一 package：

```java
interface Named { default String label() { return "N"; } }
class Parent { String f(Object x) { return "P"; } }
class Child extends Parent implements Named {
    @Override String f(Object x) { return "C"; }
    String f(String x) { return "S"; }
}
Parent p = new Child();
System.out.println(p.f("x") + ":" + ((Named)p).label());
```

A. `S:N`　B. `C:N`　C. `P:N`　D. compile error

## 题 2 · 单选（clone + final + 引用）

```java
int[] a = {1, 2};
int[] b = a.clone();
final int[] c = a;
c[0] = 9;
System.out.println(a[0] + ":" + b[0] + ":" + (a == b));
```

A. `1:1:true`　B. `9:9:true`　C. `9:1:false`　D. compile error

## 题 3 · 多选（逐项独立判断）

A. `Player[] a = new Player[3];` 合法。  
B. 在泛型方法中 `T[] a = new T[3];` 合法。  
C. 对于 `T[] input`，`T[] copy = input.clone();` 合法。  
D. `Player p = (Player) new Object();` 可以编译，但运行时可能抛 `ClassCastException`。  
E. `Player.compareTo(other)` 可以直接调用实现的实例比较方法。  
F. `Comparator.comparingInt(Player::score).reversed()` 合法。  
G. `b.score() - a.score()` 永远不会发生整数溢出。  
H. `List<Player> list = new ArrayList<>(); list.add(new Player("p",2));` 合法（假定已 import）。  
I. `Rated r = new Player("a",1); r.compareTo(new Player("b",2));` 合法。  
J. `new int[]{1,2}` 合法。

## 题 4 · 编程（15 分钟）：RunScanner

实现 `RunScanner.longestBoundedGapRun(int[] values, int maxGap)`，返回 `int[]{start,length}`，代表**相邻元素差的绝对值都不超过 maxGap** 的最长连续段；并列取**最早起点**。

- `values` 不为 null；`maxGap<0` 抛 `IllegalArgumentException`。
- 空数组 → `{-1,0}`；单元素 → `{0,1}`。
- `{1,3,5,20,21,22}` 且 maxGap=2 → `{0,3}`。
- `{1,2,100,101,102}` 且 maxGap=1 → `{2,3}`。
- `{Integer.MIN_VALUE,Integer.MAX_VALUE}` 且 maxGap=1 → `{0,1}`。
- 时间 O(n)，额外空间 O(1)（不计返回数组），不得修改输入。特别注意 long 溢出保护、首尾和并列情况。

## 题 5 · 编程（25–30 分钟）：泛型排行榜与团队快照

提供 `Rated.java`（给定接口）、`Player.java`、`Rankings.java`、`ScoreTeam.java` 四个文件。

**Player（immutable）**：`Player(String id,int score)`；id 不能为 null/空白，score >=0，否则抛 `IllegalArgumentException`；实现 `Rated`、`Comparable<Player>`；`id()` 和 `score()`。自然排序：分数**降序**，同分按 id **字典序升序**。避免用减法比较。

**Rankings（static 工具类）**：精确实现签名：

```java
public static <T extends Rated & Comparable<? super T>> T[] sortedCopy(T[] input)
public static <T extends Rated & Comparable<? super T>> T best(T[] input)
```

`sortedCopy` 返回排序后的**新数组**，不修改原数组；`best` 返回自然排序第一项，空数组返回 null。两者都拒绝 null 数组及 null 元素（抛 `NullPointerException`）。不能写 `new T[n]`。

**ScoreTeam**：`ScoreTeam(Player[] members)` 对数组 defensive copy，拒绝 null 数组/元素；`members()` 返回新数组；`best()` 委托 `Rankings.best`；`totalScore()` 返回 long，避免 int 累加溢出。空团队 best 为 null，总分为 0。Player 本身不可变，因此只需复制数组。

## countVisits 错题回访（1 分钟口算）

`start=3,size=5,moves={-2,1,0,2,-1}`。从 start 先计一次，依次执行移动，统计经过的每个位置。问：最后位置和访问次数数组分别是什么？提醒：0 步不计数；无效移动先判断，不能执行一部分。

## 运行方式

从仓库根目录执行：

```bash
mvn test-compile
mvn "-Dtest=Practice20261012Test" test
```

测试源文件名为 `2026-10-12Test.java`，内部类 `Practice20261012Test` 不加 public（因为 Java public 类名不能以数字开头）。Starter 可编译但不会通过所有测试；不要修改测试。

<details><summary>核对提示（做完再看）</summary>

选择题：1.B；2.C；3.A、C、D、F、H、J。  
题 4：先把 int 转成 long 再减；并列只在严格更长时更新最优起点。  
题 5：`Comparable<Player>`；泛型多重上界；`clone()` 两端；`long` 累加。  
countVisits：最后位置为 **3**，访问次数为 `{0,1,2,3,1}`。

</details>
