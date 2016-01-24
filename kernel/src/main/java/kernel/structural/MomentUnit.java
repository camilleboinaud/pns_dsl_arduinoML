package kernel.structural;

/**
 * Created by Quentin on 1/24/2016.
 */
public enum MomentUnit {
    second("s", 1),
    minut("m", 60),
    hour("h", 3600);

    String abbreviation;
    int multiplier;

    MomentUnit(String abbreviation, int multiplier) {
        this.abbreviation = abbreviation;
        this.multiplier = multiplier;
    }
}
