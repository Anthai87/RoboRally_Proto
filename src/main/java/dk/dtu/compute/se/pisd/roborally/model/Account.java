package dk.dtu.compute.se.pisd.roborally.model;

/**
 * @author s163053, s195388,
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
    //Returnere hhv. 1,2 og 3 som følge af at man er landet på de 3 checkpoint i den rigtige rækkefølge.
    public int getCheckpointBalance() {
        if (firstCheckPoint && !secondCheckPoint) {
            return 1;
        } else if (secondCheckPoint && !thirdCheckPoint) {
            return 2;
        } else if (thirdCheckPoint) {
            return 3;
        }

        return 0;
    }

    public boolean isFirstCheckPoint() {
        return firstCheckPoint;
    }

    public void setFirstCheckPoint(int firstCheckPoint) {
        if (firstCheckPoint == 1) {
            this.firstCheckPoint = true;
        }
    }

    public boolean isSecondCheckPoint() {
        return secondCheckPoint;
    }

    public void setSecondCheckPoint(int secondCheckPoint) {
        if (firstCheckPoint && secondCheckPoint == 2) {
            this.secondCheckPoint = true;
        }
    }

    public boolean isThirdCheckPoint() {
        return thirdCheckPoint;
    }

    public void setThirdCheckPoint(int thirdCheckPoint) {
        if (firstCheckPoint && secondCheckPoint && thirdCheckPoint == 3) {
            this.thirdCheckPoint = true;
        }
    }

}
