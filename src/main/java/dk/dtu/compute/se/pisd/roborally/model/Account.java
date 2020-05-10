package dk.dtu.compute.se.pisd.roborally.model;

/**
 *
 * @authors s163053, s195388
 *
 */

public class Account {
    private boolean firstCheckPoint;
    private boolean secondCheckPoint;
    private boolean thirdCheckPoint;

    public Account() {
        this.firstCheckPoint = false;
        this.secondCheckPoint = false;
        this.thirdCheckPoint = false;
    }

    public int getCheckpointBalance() {
        if (firstCheckPoint && !secondCheckPoint) {
            return 1;
        } else if (secondCheckPoint && !thirdCheckPoint) {
            return 2;
        } else if (thirdCheckPoint){
            return 3;
        }

        return 0;
    }

    public boolean isFirstCheckPoint() {
        return firstCheckPoint;
    }

    public void setFirstCheckPoint(int firstCheckPoint) {
        System.out.println(firstCheckPoint);
        if (firstCheckPoint == 1) {
            this.firstCheckPoint = true;
        }
        System.out.println(this.firstCheckPoint);
    }

    public boolean isSecondCheckPoint() {
        return secondCheckPoint;
    }

    public void setSecondCheckPoint(int secondCheckPoint) {
        System.out.println(secondCheckPoint);
        if (firstCheckPoint && secondCheckPoint == 2) {
            this.secondCheckPoint = true;
        }
        System.out.println(this.secondCheckPoint);
    }

    public boolean isThirdCheckPoint(){
        return thirdCheckPoint;
    }

    public void setThirdCheckPoint (int thirdCheckPoint) {
        if (firstCheckPoint && secondCheckPoint && thirdCheckPoint ==3){
            this.thirdCheckPoint = true;
        }


        System.out.println(thirdCheckPoint);
        System.out.println(this.thirdCheckPoint);
    }

    }
