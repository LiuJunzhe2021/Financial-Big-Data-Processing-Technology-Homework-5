# 作业 5：使用 MapReduce 分析股票成分股集合

姓名：________　学号：________

## 任务与环境

输入文件 `A.txt`、`B.txt` 的每行包含“指数编号 股票代码”。本程序先判断每个指数下的股票分别属于 `BOTH`（两文件共有）、`A_ONLY`（仅 A）或 `B_ONLY`（仅 B），再统计各指数的交集、A 独有、B 独有和并集数量。同一文件中重复出现的股票只计算一次。

我在 macOS 的 VS Code 中编写 Java 源文件，通过 `docker cp` 将代码和输入文件复制到已有的 Hadoop 伪分布式 Docker 容器，在容器内使用 Java 17 和 Hadoop 3.5.0 完成编译、打包与运行。输入文件上传至 HDFS 后，由 `Main` 连续提交两个 MapReduce 作业：任务 1 的输出作为任务 2 的输入。本项目使用 `javac` 和 `jar` 打包，未使用 Maven，因此没有 `pom.xml`。

本次作业运行时使用的命令如下（输出目录已存在；如需重新运行，应改用新的输出目录）：

```bash
docker exec hadoop-pseudo-pseudo-1 hadoop jar /tmp/stock-job.jar Main /assignment5/input/A.txt /assignment5/input/B.txt /assignment5/output_task1 /assignment5/output_task2
```

## 程序模块

| 文件 | 作用 |
| --- | --- |
| [`Main.java`](src/Main.java) | 配置并依次运行两个作业；用 `MultipleInputs` 为 A、B 指定不同的 Mapper。两个作业各使用一个 Reducer，使任务 1 按指数编号、股票代码排序，任务 2 按指数编号排序。 |
| [`AMapper.java`](src/AMapper.java) | 逐行读取 A，输出“指数编号＋股票代码”作为键、`A` 作为值。 |
| [`BMapper.java`](src/BMapper.java) | 逐行读取 B，输出相同格式的键、`B` 作为值。 |
| [`RelationReducer.java`](src/RelationReducer.java) | 汇总同一指数、同一股票的 A/B 标记，去除重复记录，输出 `BOTH`、`A_ONLY` 或 `B_ONLY`。 |
| [`CountMapper.java`](src/CountMapper.java) | 读取任务 1 的结果，按指数编号输出对应的关系标记。 |
| [`CountReducer.java`](src/CountReducer.java) | 分别统计三类关系，并计算并集数量。 |

## 运行结果

任务 1 的完整结果见 [`output_task1/part-r-00000`](output_task1/part-r-00000)，共 547 条“指数编号、股票代码、关系标记”记录。任务 2 的完整结果见 [`output_task2/part-r-00000`](output_task2/part-r-00000)：

| 指数编号 | 交集 | A 独有 | B 独有 | 并集 |
| --- | ---: | ---: | ---: | ---: |
| 101 | 3 | 13 | 4 | 20 |
| 102 | 3 | 19 | 3 | 25 |
| 103 | 52 | 195 | 255 | 502 |

两个作业在 YARN 页面均显示 `FINISHED / SUCCEEDED`。下图分别是作业状态和任务 2 的终端输出：

![YARN 页面显示两个作业运行成功](<screenshots/截屏2026-10-11 15.32.31.png>)

![任务 2 统计结果](<screenshots/截屏2026-10-11 15.32.50.png>)

## 文件与改进说明

`src/` 保存源代码，`input/` 保存原始数据，`target/stock-job.jar` 是编译后的 JAR，`output_task1/` 和 `output_task2/` 保存从 HDFS 导出的结果，`screenshots/` 保存运行截图。

目前每个作业只使用一个 Reducer，便于获得整体有序的输出，但大数据量时会限制并行能力。程序会跳过列数不符合要求的输入行；后续可以增加错误记录计数和更完善的输入校验。
