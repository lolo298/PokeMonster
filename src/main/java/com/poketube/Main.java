package com.poketube;

import com.poketube.Game.Poketube;
import com.poketube.View.App;

class Main {
    public static void main(String[] args) {
        Poketube poketube = Poketube.getInstance();
        poketube.start();

        App.startWindow();
    }
}
