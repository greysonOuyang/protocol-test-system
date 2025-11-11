# Metro ATS Protocol Test System

[🇨🇳 中文说明](README_CN.md)

---

## Overview
This project is designed for **subway ATS (Automatic Train Supervision) information testing**,  
supporting both **custom TCP protocol testing** and **ModBus read/write command testing**.

It enables users to define their own message formats, perform automated polling,  
and view **real-time test results via WebSocket (STOMP)** connections.

- **Frontend:** [protocol-test-client](https://github.com/greysonOuyang/protocol-test-client) — built with **Vue + Element UI**  
- **Backend:** [protocol-test-system](https://github.com/greysonOuyang/protocol-test-system) — built with **Spring Boot + Netty**

---

## Features
1. **Custom TCP Protocol Testing**  
   Define message encoding/decoding rules and send binary packets directly to the target server.

2. **ModBus Command Testing**  
   Support ModBus read/write instructions with full customization of registers and function codes.

3. **Polling Test Mode**  
   Automatically send periodic messages to validate connection stability and response timing.

4. **Real-time Feedback (WebSocket + STOMP)**  
   Receive decoded data and test responses instantly through a live WebSocket channel.

5. **Flexible Configuration Interface**  
   Web UI supports protocol definition, ModBus setup, and dynamic test configuration.

6. **Netty-based Backend Core**  
   High-performance non-blocking TCP client for stable and efficient communication.

---

## Architecture
| Layer | Technology | Description |
|-------|-------------|-------------|
| **Frontend** | Vue + Element UI | Provides interactive test configuration and result visualization |
| **Backend** | Spring Boot + Netty | Implements TCP & ModBus client logic, WebSocket (STOMP) messaging |
| **Protocol Support** | Custom / ModBus | Flexible protocol parsing and binary message handling |





---

## License
MIT License

---

