package mmd.konnect;

public class Utils {

    public static int[] materialColors;

    public static int[] getMaterialColors() {
        if (materialColors == null) {
            materialColors = new int[6];
            materialColors[0] = R.color.material_blue;
            materialColors[1] = R.color.material_green;
            materialColors[2] = R.color.material_pink;
            materialColors[3] = R.color.material_orange;
            materialColors[4] = R.color.material_red;
            materialColors[5] = R.color.material_cyan;
        }

        return materialColors;
    }

    public static int getHashedMaterialColor(String s) {
        int i = (Math.abs(s.hashCode() % getMaterialColors().length));
        return getMaterialColors()[i];
    }

}
