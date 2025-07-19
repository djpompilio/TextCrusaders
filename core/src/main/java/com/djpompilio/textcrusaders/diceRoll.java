package com.djpompilio.textcrusaders;

public class diceRoll {
    int d100() {

        return (int)(Math.random()*100+1);
    }
    int d20() {

        return (int)(Math.random()*20+1);
    }
    int d6() {

        return (int)(Math.random()*6+1);
    }
    int d4() {

        return (int)(Math.random()*4+1);
    }
    int coin() {

        return (int)(Math.random()*2+1);
    }
}
