# Day 4 · 2026-10-10 — 数组状态维护 + OO 移动模拟（50–60 分钟）

## 今日语法记录清单

1. **连续区间**：空数组先处理；非空区间一般 `current=best=1`。正确：不满足关系时 `current=1`；错误：重置为 0，因为当前元素仍构成长度 1 的新段。
2. **最长段起点 / 并列**：只在 `current > best` 时替换 `bestStart`；错误：用 `>=` 会把最早的同长度区间覆盖。
3. **正负判断**：正确：`x > 0`、`x < 0`；错误：用 `Math.abs(x)` 或 `x*y<0` 判符号，`Integer.MIN_VALUE` 与乘法溢出会出问题。
4. **模拟移动**：`position` 必须随每个有效 move 更新；错误：始终使用 `start + moves[i]`，第二步开始位置就错了。
5. **整步越界检查**：先算 `long target=(long)position+steps`，检查 `<0 || >=size` 后再执行；错误：先部分移动或只检查右边界。
6. **访问次数**：经过的每个新位置 `counts[position]++`；错误：`=1` 会丢失重复访问，`counts[j]++` 会把循环次数当作实际位置。
7. **defensive copy**：`return counts.clone()`，而不是 `return counts`；否则调用者可修改对象内部状态。
8. **constructor / final**：`final int[] counts` 固定数组引用，但元素可变；快照应在构造时复制数组，getter 时再次复制。
9. **interface / override**：`RouteSession s=new RouteTracker(...)` 合法，但变量 `s` 只能调用接口声明的方法。重写方法的签名必须匹配；`@Override` 可在编译期发现错误。
10. **static / instance 与错误类别**：`RouteTracker.fromMoves(...)` 是 static factory，`tracker.move(...)` 是实例方法。签名错误 → compile error；错误 downcast → runtime error；泄露数组引用 → 可能编译成功但测试失败。

## 题 1 · 单选（overload / override）

```java
class Base {
    String show(Object x) { return "B:Object"; }
    String show(String x) { return "B:String"; }
}
class Child extends Base {
    @Override String show(Object x) { return "C:Object"; }
}
Base b = new Child();
System.out.println(b.show("hi") + "/" + b.show((Object) "hi"));
```

A. `B:String/C:Object`　B. `C:Object/C:Object`　C. `B:String/B:Object`　D. 编译错误。

## 题 2 · 单选（static / final / 引用）

```java
class Counter {
    static int total = 0;
    int value = 0;
    Counter() { total++; }
    void add() { value++; }
}
Counter a = new Counter();
Counter b = new Counter();
final Counter c = a;
c.add();
System.out.println(Counter.total + ":" + a.value + ":" + b.value);
```

A. `1:1:0`　B. `2:1:0`　C. `2:0:0`　D. 编译错误。

## 题 3 · 多选（逐项独立判断）

A. `int[] a = new int[3];` 合法。  
B. `int[] b = new int[];` 合法。  
C. `new StringBuilder("x").equals(new StringBuilder("x"))` 为 `true`。  
D. `final int[] a = {1}; a[0] = 2;` 合法。  
E. `RouteSession s = new RouteTracker(4, 0); s.move(1);` 合法。  
F. `RouteTracker r = (RouteTracker) new Object();` 能编译，但运行时抛 `ClassCastException`。  
G. static 方法可不借助对象直接访问同类实例字段 `position`。  
H. `int[] a = new int[]{1,2};` 合法。  
I. 变量类型为 `RouteSession` 时，`s.snapshot()` 必然可编译，即使接口未声明此方法。

## 题 4 · 编程（15–20 分钟）：ArrayPatterns

修改 `src/main/java/practice/day20261010/ArrayPatterns.java`。实现以下两个 `public static` 方法。时间 O(n)、额外空间 O(1)、不得修改输入；前置条件数组非 null。

**A. `longestAlternatingSignRun(int[] values)`**：最长连续非零、正负严格交替子数组的长度。`0` 会中断连续段，且自身不构成有效段。空数组 / 全 0 → 0；`{-1,2,-3,0,4,-5}` → 3；`{2,2,-1}` → 2；`{Integer.MIN_VALUE,1}` → 2。

**B. `firstLongestIncreasingRunStart(int[] values)`**：返回严格递增的最长连续段的**起始下标**；空数组 → -1；并列取最早段。单元素 → 0；`{1,2,0,1,2}` → 2；`{1,2,0,1}` → 0；`{5,5,5}` → 0。注意返回的是下标，不是长度。

## 题 5 · 编程（25–30 分钟）：RouteTracker / RouteSnapshot

修改 `RouteTracker.java`、`RouteSnapshot.java`；`RouteSession.java` 是给定接口，不要改。

- `new RouteTracker(size,start)`：位置为 `0..size-1`；初始 `counts[start]=1`；size <=0 或 start 越界时抛 `IllegalArgumentException`。
- `move(int steps)`：有效 move 返回 true，按正/负方向**逐格**增加访问次数，排除出发点、包含终点，更新当前位置。0 步有效但不新增访问。若整个 move 的目标越界，**整步不执行**、返回 false、永久置 `stopped=true`，之后所有 move 均返回 false。
- `position()` / `stopped()` 返回当前状态。
- `visitCounts()` 每次返回新数组，防止外部修改内部计数。
- `static fromMoves(int size,int start,int[] moves)`：创建新 tracker，按序执行，遇首次无效 move 后立即停止并忽略余下 moves。`moves==null` 抛 `NullPointerException`；不得修改 moves。
- `snapshot()`：返回独立的 `RouteSnapshot`，保存调用瞬间 position、stopped、counts；后续 tracker 的修改不得影响快照。快照 `visitCounts()` 也必须返回新数组。

示例：`fromMoves(5,0,new int[]{2,-1,1,-1})` → `{1,3,2,0,0}`，终点 1；`fromMoves(4,0,new int[]{1,5,-1})` → `{1,1,0,0}`，终点 1，stopped 为 true。

这是 `countVisits` 的 OO 状态机变体，重点考状态持续更新、负向移动、重复计数、越界原子性、接口与 defensive copy。

## 编译与测试

从项目根目录运行：

```bash
mvn test-compile
mvn "-Dtest=Practice20261010Test" test
```

测试类名使用 `Practice20261010Test.java`，因为 Java 类名不能以数字开头。Starter **可以编译但未实现正确逻辑**，测试失败是预期的；不要修改测试。

<details><summary>核对提示（完成后展开）</summary>

单选：1.A，2.B；多选：A、D、E、F、H。  
题 4：`0` 会打断交替段；`Integer.MIN_VALUE`；并列最长段保留最早起点。  
题 5：先用 `long` 判界；逐格累计；无效 move 锁定状态；Tracker 和 Snapshot 均须防御性复制。

</details>
