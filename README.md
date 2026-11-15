# Brighter Bright

A small client-side NeoForge mod that raises the maximum value of Sodium's existing brightness slider from **100% to 200%**.

## Supported versions

- Minecraft 1.21.1
- NeoForge 21.1.x
- Sodium 0.8.13 for NeoForge 1.21.1

## What it changes

Brighter Bright uses Sodium's Config API to overlay the range of the existing `sodium:general.gamma` option. It keeps Sodium's original slider, percentage display, value binding, and save behavior; only the maximum is changed.

The slider continues to work normally from 0% through 100%, and can now be raised to 200%.

## Installation

1. Install NeoForge for Minecraft 1.21.1.
2. Install Sodium 0.8.13 for NeoForge 1.21.1.
3. Put the Brighter Bright JAR in your `mods` folder.
4. Launch the game and open Video Settings.

This is client-side only. It does not need to be installed on a server.

## Building

Requires Java 21 and Gradle 8.10.2 or newer.

```sh
gradle build
```

The built JAR will be in `build/libs/`.
