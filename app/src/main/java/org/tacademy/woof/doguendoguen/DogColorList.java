package org.tacademy.woof.doguendoguen;

import java.util.HashMap;

/**
 * Created by Tak on 2017. 7. 10..
 */

public class DogColorList {
    private static HashMap<String, Integer> colorsMap;
    static {
        colorsMap = new HashMap<>();

        colorsMap.put("흰색", R.drawable.white);
        colorsMap.put("아이보리", R.drawable.ivory);
        colorsMap.put("밝은 갈색", R.drawable.bright_brown);
        colorsMap.put("어두운 갈색", R.drawable.dark_brown);
        colorsMap.put("회색", R.drawable.grey);
        colorsMap.put("검정색", R.drawable.black);
        colorsMap.put("얼룩무늬", R.drawable.spot);
        colorsMap.put("기타", R.drawable.etc);
    }

    public static Integer getColorItem(String key) {
        return colorsMap.get(key);
    }
}
