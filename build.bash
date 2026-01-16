#!/bin/bash
set -u -e
javac Game.java View.java Controller.java Model.java Tree.java Link.java Json.java Sprite.java Boomerang.java TreasureChest.java
java Game
