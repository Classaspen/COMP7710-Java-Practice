# COMP7710 Java Practice

这是一个持续更新的 Java 上机练习仓库。

- 每日题目：`daily/YYYY-MM-DD/README.md`
- Starter code：`src/main/java/practice/dayYYYYMMDD/`
- JUnit 测试：`src/test/java/practice/dayYYYYMMDD/`

首次使用：

```bash
git clone https://github.com/Classaspen/COMP7710-Java-Practice.git
cd COMP7710-Java-Practice
mvn test
```

之后每天：

```bash
git pull
mvn test
```

> 建议先做题，再跑测试。不要先读 test 文件找答案。

---

# 高频语法 / 错误清单

这部分会持续累积，只保留高价值、容易在上机中造成 compile error、runtime error 或隐蔽逻辑错误的内容。

## 1. 数组创建：具体类型必须指定长度或初始化器

```java
A[] x = new A[5];          // ✅ 指定长度
A[] y = new A[]{a1, a2};   // ✅ 指定元素
A[] z = new A[];           // ❌ compile error
```

注意：如果 `T` 是泛型类型参数，`new T[length]` 也不能编译；上面的 `A` 指具体类名。

复制数组：

```java
A[] copy = input.clone();
// 或
A[] copy = new A[input.length];
System.arraycopy(input, 0, copy, 0, input.length);
```

`A[] copy = input;` 能编译，但两个变量指向同一个数组对象，不是 defensive copy。

## 2. static 和 instance 不要混

```java
class A {
    int x;
    static int y;

    void f() { x++; y++; }       // ✅
    static void g() { y++; }     // ✅
    // static void h() { x++; }  // ❌
}
```

instance method 通常通过对象调用：

```java
obj.compareTo(other);  // ✅
A.compareTo(other);    // ❌ 如果 compareTo 不是 static
```

## 3. override 的方法签名必须匹配

```java
@Override
public int compareTo(A other) { ... }
```

参数类型、方法名必须匹配接口/父类声明；返回类型必须兼容；不能降低访问级别。

## 4. upcasting 安全，downcasting 要看真实对象

```java
A a = new B();  // ✅
B b = (B) a;    // ✅ 真实对象是 B

A a2 = new A();
B b2 = (B) a2;  // 编译可能通过，但运行时 ClassCastException
```

必要时先判断：

```java
if (a instanceof B) {
    B b = (B) a;
}
```

## 5. `==` 和 `equals()` 不同

```java
x == y       // 是否同一个引用
x.equals(y)  // 是否逻辑相等（取决于 equals 实现）
```

## 6. `final` 引用不能改指向，但对象本身可能还能改

```java
final StringBuilder sb = new StringBuilder();
sb.append("abc");              // ✅
// sb = new StringBuilder();    // ❌
```

## 7. Java 自动测试通常是 compile first, then test

```text
compile production code
→ compile test code
→ run tests
```

如果 compile 阶段失败，对应测试通常不会开始运行。因此上机最后几分钟优先保证项目可以编译。

---

## 训练节奏

前 14 天高密度循环复习核心语法与 OO 关系；之后继续每天更新，把新的 COMP7710 课程内容、近期错题和高风险点加入训练。

## 环境与每日使用

需要 JDK 17 或更高版本，以及 Maven 3.9+；IDE 中把根目录 pom.xml 作为 Maven 项目打开。
首次下载依赖需要联网。项目使用 Java 17 语言级别和 JUnit 5。

- [首日练习：2026-10-07](daily/2026-10-07/README.md)
- [两周计划与反馈入口](CURRICULUM.md)

每天先保存本地作答，再拉取新增练习：
```bash
git status
# 有作答改动时先在本地 commit，或自行妥善保存
git pull --ff-only
mvn "-Dtest=Practice20261007Test" test
```

未完成的 starter 会真实报错，这是练习的初始状态；不是依赖安装失败。
`mvn test` 运行全部日期，适合全量复习；平时使用日期测试名，避免旧题干扰。
只检查项目能否编译（包括测试）可运行 `mvn test-compile`。
若 pull 提示分支分叉，先保留本地作答再合并，不要使用 reset --hard。

测试文件使用 `PracticeYYYYMMDDTest.java`：Java 类名不能以日期数字开头，也不能带连字符。
每日生成遵循 CURRICULUM.md；定时任务的启用情况以应用里的任务设置为准。
