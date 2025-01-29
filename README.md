# PokemonSimulator

## Description

## Installation
1. get the prebuilt installers from the [releases](https://github.com/lolo298/PokeMonster/releases) page.
2. run the installer
3. run the game


## Run from source
### Requirements
you need to have the following installed:
- Java 21
- On Windows: [WiX Toolset](https://wixtoolset.org/releases/)
- On Linux: `sudo apt install fakeroot`

### Steps
1. clone the repository
2. run `./gradlew run`
3. the game will start

## Compile the installer from source
1. clone the repository
2. run `./gradlew jpackage`
3. the installer will be in `build/jpackage`

