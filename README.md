# SGCCKiller
基于Excel的做题器，追求速度，代码质量不高，后期改为brower-server。
> 一小时码出来的，为了尽快应用秉承能润就行的心态，后续会推出基于SpringBoot+Vue前后端分离架构的答题系统，敬请期待！
- 轻量级不需要数据库
- 能用就行目前，代码有点高耦合面向过程了。
## Version 1.0
- 基于CML的操作界面
- 可以选择做题范围（单数字代表某题，回车默认全部）
- 支持错题功能和错题最大数统计
- 支持背题模式（方便导出word）
## Version 1.1
- 支持选择不同的题库
- 基于CML的操作界面
- 支持不同的配置文件
- 支持1/a/A来代表第一个选项
- 支持自定义T/F标识
- 仅支持04后的Excel格式（poi接口发生变化），自用工具，暂且未支持全格式。 
## Ver1.2
- 优化代码
## Ver1.3
- 可以通过输入s/S来skip改题，将错误次数重置
- 添加了计时功能
- 添加了排序功能，将题库按照名称排序
## Ver1.4
- 添加了乱序做题功能
- 添加了重置题库功能
- 记录历史数据
- 默认情况，直接跳过题库和配置文件的选择
- 修复小bugs
- 修复了错题不会增加的bug
## Ver1.5
- 兼容了xls格式
- 修复了获取最后一行的bug
- 优化了错题模式算法，如果答对n次错题，可以减少错题次数。(ratio配置项)
- 可以选择题型
## TodoList
- 兼容xls格式 √
- 打乱选项顺序 √
- 记录历史数据 √
- 随机抽样 √
- 猜你喜欢：类似内存淘汰算法，LRU等等来猜不会的题，采用机器学习？
- 可以记录用户做题的各个数据，然后给题打标签，从而更加精确的判断哪些题可能不熟悉。如果做对了就打上熟悉的标签，做错就打上不熟悉的标签。
记录用户答题时长，平均答题时长，做错次数，等等。
- 包年、广告、私有云等


