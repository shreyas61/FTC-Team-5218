package opmodes.Houston;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

import team25core.DeadReckonPath;

import static opmodes.Houston.HoustonBachDropoffUtil.MineralPosition.DEFAULT;
import static team25core.MineralDetectionTask.LABEL_GOLD_MINERAL;

/**
 * Created by Lizzie on 2/6/2019.
 */
public class HoustonBachDropoffUtil {

    // groundwork for autonomous variables
    public enum EndingPosition {
        PARKING,
        NOT_PARKING,
        DEFAULT,
    }

    public enum MineralPosition {
        LEFT,
        RIGHT,
        CENTER,
        DEFAULT,
    }

    public enum HangingPosition {
        HANGING,
        NOT_HANGING,
        DEFAULT,
    }

    public static void sendPositionTelemetry(MineralPosition pos, Telemetry.Item item) {
        switch (pos) {
            case LEFT:
                item.setValue("LEFT");
                break;
            case RIGHT:
                item.setValue("RIGHT");
                break;
            case CENTER:
                item.setValue("CENTER");
                break;
        }
    }

    public static MineralPosition determineGoldPosition(List<Recognition> minerals) {
        if (minerals != null) {
            int goldMineralX = -1;
            int silverMineral1X = -1;
            int silverMineral2X = -1;
            for (Recognition recognition : minerals) {
                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                    goldMineralX = (int) recognition.getLeft();
                } else if (silverMineral1X == -1) {
                    silverMineral1X = (int) recognition.getLeft();
                } else {
                    silverMineral2X = (int) recognition.getLeft();
                }
            }

            int size = minerals.size();
            if (size == 2) {
                // two mineral detection code
                if (goldMineralX != -1 && silverMineral1X != -1) {
                    if (goldMineralX < silverMineral1X) {
                        return MineralPosition.LEFT;
                    } else if (goldMineralX > silverMineral1X) {
                        return MineralPosition.CENTER;
                    }
                } else if (silverMineral1X != -1 && silverMineral2X != -1) {
                    return MineralPosition.RIGHT;
                }
            } else if (size == 3) {
                if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                    if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                        return MineralPosition.LEFT;
                    } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                        return MineralPosition.RIGHT;
                    } else {
                        return MineralPosition.CENTER;
                    }
                }
            }

            /*
            int size = minerals.size();
            if (size == 2) {
                // two mineral detection code
                if (goldMineralX != -1 && silverMineral1X != -1) {
                    if (goldMineralX < silverMineral1X) {
                        return MineralPosition.CENTER;
                    } else if (goldMineralX > silverMineral1X) {
                        return MineralPosition.RIGHT;
                    }
                } else if (silverMineral1X != -1 && silverMineral2X != -1) {
                    return MineralPosition.LEFT;
                }
            } else if (size == 3) {
                if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                    if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                        return MineralPosition.LEFT;
                    } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                        return MineralPosition.RIGHT;
                    } else {
                        return MineralPosition.CENTER;
                    }
                }
            }
            */
        }
        return MineralPosition.DEFAULT;
    }

    DeadReckonPath[][] paths = new DeadReckonPath[2][3];

    public HoustonBachDropoffUtil() {
        /*
        DEPOT CODE: marker dropoff with parking
        */

        // depot, parking, center mineral
        paths[EndingPosition.PARKING.ordinal()][MineralPosition.CENTER.ordinal()] = new DeadReckonPath();
        paths[EndingPosition.PARKING.ordinal()][MineralPosition.CENTER.ordinal()].addSegment(DeadReckonPath.SegmentType.STRAIGHT, 15.0, 0.5);

        // depot, no parking, center mineral
        paths[EndingPosition.NOT_PARKING.ordinal()][MineralPosition.CENTER.ordinal()] = new DeadReckonPath();

        // depot, parking, left mineral
        paths[EndingPosition.PARKING.ordinal()][MineralPosition.RIGHT.ordinal()] = new DeadReckonPath();
        paths[EndingPosition.PARKING.ordinal()][MineralPosition.RIGHT.ordinal()].addSegment(DeadReckonPath.SegmentType.STRAIGHT, 2.0, 0.5);
        paths[EndingPosition.PARKING.ordinal()][MineralPosition.RIGHT.ordinal()].addSegment(DeadReckonPath.SegmentType.TURN, 23.0, -0.5);
        paths[EndingPosition.PARKING.ordinal()][MineralPosition.RIGHT.ordinal()].addSegment(DeadReckonPath.SegmentType.STRAIGHT, 10.0, 0.5);

        // depot, no parking, left mineral
        paths[EndingPosition.NOT_PARKING.ordinal()][MineralPosition.LEFT.ordinal()] = new DeadReckonPath();

        // depot, parking, right mineral
        paths[EndingPosition.PARKING.ordinal()][MineralPosition.LEFT.ordinal()] = new DeadReckonPath();
        paths[EndingPosition.PARKING.ordinal()][MineralPosition.LEFT.ordinal()].addSegment(DeadReckonPath.SegmentType.TURN, 12.0, -0.3);
        paths[EndingPosition.PARKING.ordinal()][MineralPosition.LEFT.ordinal()].addSegment(DeadReckonPath.SegmentType.STRAIGHT, 10.0, 0.5);

        // depot, no parking, right mineral
        paths[EndingPosition.NOT_PARKING.ordinal()][MineralPosition.RIGHT.ordinal()] = new DeadReckonPath();
    }

    public DeadReckonPath getPath (HoustonBachDropoffUtil.EndingPosition end, HoustonBachDropoffUtil.MineralPosition position) { return paths[end.ordinal()][position.ordinal()];
    }
}
