# Day 3 · 2026-10-09：状态维护、HashSet 契约与类间协作（50–60 分钟）

## 今日语法记录清单

1. **连续区间 / run**：空数组返回 `0`；非空数组 `current = best = 1`。从 `i=1` 比较 `a[i]` 与 `a[i-1]`；不满足时 `current=1`，不是 `0`。
2. **最大值更新**：每轮执行 `best = Math.max(best,current)`，否则可能漏掉最后一段。
3. **模拟移动 / state**：`currentPosition` 随有效 move 更新；`start` 只用于初始位置，不能每次都用 `start + moves[i]`。
4. **正负步数**：先判断目标是否在 `[0,size)`，再以 `+1/-1` 逐格统计。用 `(long) currentPosition + move` 防止 int 溢出。
5. **访问次数**：`counts[position]++`，不是 `=1`；move 为 `0` 不新增访问；无效 move 不执行任何部分并立即终止。
6. **数组引用**：`moves.clone()` 复制数组容器，`copy=moves` 只是共享引用；`new int[n]` 合法，`new int[]` 不合法，泛型 `new T[n]` 也不合法。
7. **equals/hashCode 契约**：`a.equals(b)` 为 true 则两者 `hashCode()` 必须相同；`HashSet` 不会自动按字段去重。
8. **可变哈希键**：入 Set 后修改参与 hashCode 的字段可能使 contains/remove 找不到；优先不可变 final 字段。
9. **LinkedHashSet 与 static**：按首次插入顺序迭代；`Roster.from(...)` 是静态工厂，`roster.register(...)` 是实例方法；final 引用不冻结对象。
10. **编译优先**：`@Override` 可捕捉签名错误；compile error 与测试断言失败不是一回事。

## 题 1 · 单选：连续区间与空数组

```java
static int run(int[] a) {
    int current = 1, best = 1;
    for (int i = 1; i < a.length; i++) {
        if (a[i] >= a[i - 1]) current++;
        else current = 1;
        best = Math.max(best, current);
    }
    return best;
}
```
分别调用 `run(new int[]{4,4,2,3,3})` 和 `run(new int[0])`，结果是什么？
A. `3` 和 `1`；B. `2` 和 `1`；C. `3` 和 `0`；D. 编译错误。

## 题 2 · 单选：可变 HashSet 键

```java
class Key {
    String id;
    Key(String id) { this.id = id; }
    @Override public boolean equals(Object o) {
        return o instanceof Key k && id.equals(k.id);
    }
    @Override public int hashCode() { return id.hashCode(); }
}
Key k = new Key("A");
java.util.Set<Key> set = new java.util.HashSet<>();
set.add(k);
k.id = "B";
System.out.println(set.contains(k));
```
A. `false`；B. `true`；C. 编译错误；D. 抛出 ConcurrentModificationException。

## 题 3 · 多选：独立判断每项

A. `Member[] a = new Member[3];` 合法（Member 是具体类）。
B. 在 `class Box<T>` 中 `T[] a = new T[3];` 合法。
C. 签名不匹配的方法加 `@Override` 会触发编译错误。
D. `final java.util.List<String> xs = new java.util.ArrayList<>(); xs.add("x");` 合法。
E. Member 未重写 equals/hashCode 时，HashSet 也会自动按 id 去重。
F. static 方法可不通过对象直接访问同类实例字段。
G. 两个不同但内容相同的 StringBuilder 默认 equals 为 true。
H. `Member[] a = new Member[]{new Member("u1","Ada")};` 合法。

## 题 4 · 编程：ArrayRoutes（20–25 分钟）

修改 [`ArrayRoutes.java`](../../src/main/java/practice/day20261009/ArrayRoutes.java)，保留 public static 方法签名。

**A. `longestNonDecreasingRun(int[] values)`**：返回最长连续非递减段（相邻 `<=`）的长度；空数组 0，单元素 1。要求 O(n) 时间、O(1) 额外空间、不修改输入。示例 `{5,5,1,2,2,0}` → 3；`{9,8,7}` → 1。

**B. `countVisits(int[] moves,int start,int size)`**：位置 0..size-1；初始 start 先计一次。按顺序执行有符号 move，每经过一格，刚到达的位置 +1（出发点不额外计）。0 move 不新增计数。若**整个 move 的终点**越界，必须在移动前停止，不计该 move 的任何位置，也忽略后续 moves。不得修改 moves。

示例：`{2,-1,1,-1}, start=0,size=5` → `{1,3,2,0,0}`；`{1,5,-1},0,4` → `{1,1,0,0}`；`{-2,1,0,2},3,6` → `{0,1,2,2,1,0}`。
前置条件：size>0，0<=start<size，数组非 null；大幅移动时也必须正确判界。

## 题 5 · 编程：Member / Roster（20–25 分钟）

修改 [`Member.java`](../../src/main/java/practice/day20261009/Member.java) 和 [`Roster.java`](../../src/main/java/practice/day20261009/Roster.java)；[`Identified.java`](../../src/main/java/practice/day20261009/Identified.java) 是给定接口。

- Member 不可变；id/displayName 非 null，id 不得为空白；实现 Identified.id()。
- **只按 id（区分大小写）判逻辑相等**，忽略 displayName；正确重写 equals(Object) 与 hashCode()。
- register(Member) 首次 id 返回 true，重复返回 false；保留首次对象及顺序；null 抛 NPE。
- hasId(String) 按 id 查询；null 抛 NPE，空白 id 抛 IAE。
- snapshot() 返回按首次注册顺序排列的**新数组**；修改数组元素引用不影响 Roster。Member 不可变，无需复制元素。
- size() 为唯一 id 数；static from(Member[] input) 按顺序注册、去重、不修改输入；null 数组或元素抛 NPE。

## 编译与测试

```bash
mvn test-compile
mvn "-Dtest=Practice20261009Test" test
```

测试文件：[`Practice20261009Test.java`](../../src/test/java/practice/day20261009/Practice20261009Test.java)。Java 类名不能以数字开头或带连字符，所以沿用 PracticeYYYYMMDDTest。Starter 故意有逻辑错误但应可编译。

<details>
<summary>核对提示（完成后展开）</summary>

1. A；2. A；3. A、C、D、H。
题 4：空数组先处理；更新最大值；先算目标后移动；当前位置变化、正负方向、重复计数、无效移动全停。
题 5：equals/hashCode 只依赖稳定 id；LinkedHashSet 保持插入顺序；snapshot 返回新数组；from 是 static factory。

</details>
