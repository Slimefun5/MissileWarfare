# MissileWarfare

[![Build Status](https://Slimefun5.github.io/builds/Slimefun5/MissileWarfare/stable/badge.svg)](https://Slimefun5.github.io/builds/Slimefun5/MissileWarfare/stable)
![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/Slimefun5/MissileWarfare/total)
[![GitHub Followers](https://img.shields.io/github/followers/Slimefun5?style=social)](https://github.com/Slimefun5)
[![GitHub Stars](https://img.shields.io/github/stars/Slimefun5/MissileWarfare?style=social)](https://github.com/Slimefun5/MissileWarfare)

A Slimefun addon that adds missiles, launchers, and warfare equipment.

## Requirements
- Java 25
- Paper 1.16.* - 26.1.*
- Slimefun 5

## Integrations (Soft Depends)
- **Towny:** Checks if the person nearest to the missile at the time of its launch was an enemy of the town where the explosion has happened. If they aren't, the explosion is cancelled.
- **Worldguard:** Adds flag `ALLOW_MISSILE_EXPLODE`. 

## Contributors
|Position|Discord|Github|
|Original Author| pain.#2883 | [koiboi-dev](https://github.com/koiboi-dev) |
| Current Maintainer and Developer | Colonel Kai#0001 | [ColonelKai](https://github.com/ColonelKai) |

## Developer API

You can easily depend on this project using [github-gradle](https://github.com/intisy/github-gradle).

In your `build.gradle.kts`:

```kotlin
plugins {
    id("io.github.intisy.github-gradle") version "1.8.2.1"
}

dependencies {
    "githubCompileOnly"("Slimefun5:MissileWarfare:v2.0.1")
}
```
