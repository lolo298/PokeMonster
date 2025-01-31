# PokemonSimulator

## Description

## Installation

1. get the prebuilt installers from the [releases](https://github.com/lolo298/PokeMonster/releases) page.
2. run the installer
3. run the game

## Compile

### Requirements

you need to have the following installed:

- Java 17+
- On Windows: [WiX Toolset](https://wixtoolset.org/releases/)
- On Linux: `sudo apt install fakeroot`

### Run from source

1. clone the repository
2. run `sudo chmod +x ./gradle`
3. run `./gradlew run`
4. the game will start

### Compile the installer from source

1. clone the repository
2. run `sudo chmod +x ./gradlew`
3. run `./gradlew jpackage`
4. the installer will be in `build/jpackage`
