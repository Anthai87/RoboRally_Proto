package dk.dtu.compute.se.pisd.roborally.model;

public class Account {
    private boolean firstCheckPoint;
    private boolean secondCheckPoint;

    public Account() {
        this.firstCheckPoint = false;
        this.secondCheckPoint = false;
    }

    public int getCheckpointBalance() {
        if (firstCheckPoint && !secondCheckPoint) {
            return 1;
        } else if (secondCheckPoint) {
            return 2;
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
        if (firstCheckPoint && secondCheckPoint == 2) {
            this.secondCheckPoint = true;
        }
    }
}