# 👨‍🍳 Special Cook – Kitchen Management System

![Build & Test](https://github.com/YamamaSawalmeh/SoftwareProject/actions/workflows/main.yml/badge.svg)

A Java system for running a custom-meal kitchen, built with **Behavior-Driven Development (BDD)** for the Software Engineering course at An-Najah National University.

Every feature started as a user story in Gherkin (`.feature` files) and is covered by automated **Cucumber** acceptance tests: **19 feature files, 113 passing tests**.

## Features

| Area | What it does |
|------|--------------|
| 🔐 **Login & users** | Role-based login for admins, chefs, kitchen managers and customers |
| 🥗 **Preferences & allergies** | Store each customer's dietary preferences and allergies, and warn about conflicting ingredients |
| 🍽️ **Custom meals** | Customers build their own meals, validated against available ingredients |
| 🤖 **Recipe recommendations** | Suggests recipes that match the customer's preferences and available time |
| 📋 **Orders & history** | Order tracking, past orders and personalized meal plans |
| 👩‍🍳 **Chef scheduling** | Assigns cooking tasks to chefs according to their workload and expertise |
| 📦 **Inventory & suppliers** | Stock tracking, low-stock alerts and automatic restocking from suppliers |
| 🧾 **Billing & invoicing** | Generates invoices and financial reports |
| 🔔 **Notifications** | Reminders for customers and chefs |

## Tech stack

Java 17 · Maven · Cucumber 7 (BDD / Gherkin) · JUnit 5 · JaCoCo · GitHub Actions

## Project structure

```
cook/
├── src/main/java/app/                    # domain classes and services
├── src/test/resources/user_stories/      # 19 Gherkin feature files
└── src/test/java/testing_cases/
    ├── AcceptanceTest.java               # Cucumber runner
    └── step_definition/                  # step definitions
```

## Run

```bash
cd cook
mvn verify                               # run all tests (+ JaCoCo coverage report in target/site/jacoco)
mvn compile exec:java -Dexec.mainClass=app.Main   # interactive console demo
```

## Team

Yamama Sawalma · Lama Damiri
