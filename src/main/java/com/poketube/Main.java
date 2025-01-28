package com.poketube;

import com.poketube.Game.Poketube;
import com.poketube.Utils.Logger;
import com.poketube.Utils.Random;
import com.poketube.View.App;

class Main {
    public static void main(String[] args) {
        Poketube poketube = Poketube.getInstance();
        poketube.start();

        double tmp = Random.generateValue(1, 2);
        Logger.log(Double.toString(tmp));

        App.startWindow();
    }
}
