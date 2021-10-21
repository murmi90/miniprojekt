package ch.ost.rj.mge.testat2;

public class LocationDataStruckture {
    String name;
    String ort;
    int levelMin;
    int levelMax;
    boolean isFavorite;
    LocationDataStruckture(String name, String ort, int levelMin, int levelMax){
        this.name = name;
        this.ort = ort;
        this.levelMin = levelMin;
        this.levelMax = levelMax;
        isFavorite = false;
    }

    public String getName(){
        return name;
    }

    public String getOrt(){
        return  ort;
    }

    public void  setFavorite(boolean isFavorite){
        this.isFavorite = isFavorite;
    }

}
