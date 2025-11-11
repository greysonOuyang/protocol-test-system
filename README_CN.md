# 地铁ATS协议测试系统

[🇺🇸 English Version](README.md)

---

## 概览
本项目用于 **地铁ATS（自动列车监控系统）信息测试**，  
支持 **自定义TCP协议测试** 与 **ModBus读写指令测试**。  

系统支持轮询发送、WebSocket（STOMP）实时返回测试结果，  
便于验证通信协议、报文解析及服务端响应逻辑。

- **前端仓库：** [protocol-test-client](https://github.com/greysonOuyang/protocol-test-client) — 基于 **Vue + Element UI**  
- **后端仓库：** [protocol-test-system](https://github.com/greysonOuyang/protocol-test-system) — 基于 **Spring Boot + Netty**

---

## 功能特性
1. **自定义TCP协议测试**  
   可配置报文格式与解析规则，直接发送二进制数据至待测服务端。

2. **ModBus读写测试**  
   支持ModBus功能码配置、寄存器读写测试，灵活验证设备通信逻辑。

3. **轮询测试模式**  
   可周期性发送消息，验证连接稳定性与响应性能。

4. **实时数据反馈（WebSocket + STOMP）**  
   测试结果实时返回至前端界面，展示解析后的数据内容。

5. **可视化配置界面**  
   提供协议配置、ModBus参数设置、测试任务管理等功能。

6. **基于Netty的高性能核心**  
   使用异步非阻塞通信模型，确保消息处理的稳定与高效。

---

## 系统架构
| 层级 | 技术栈 | 说明 |
|------|----------|------|
| **前端** | Vue + Element UI | 提供交互式测试配置与结果展示 |
| **后端** | Spring
