package sweeper;

class Flag {

    private Matrix flagMap;

    private int totalFlaged;

    private int totalClosed;

    void start() {
        flagMap = new Matrix(Box.CLOSED);
        totalFlaged = 0;
        totalClosed = Ranges.getSquare();
    }

    Box get(Coord coord) {
        return flagMap.get(coord);
    }

    private void setFlagedToBox(Coord coord) {
        flagMap.set(coord, Box.FLAGED);
        totalFlaged += 1;
    }

    private void setClosedToBox(Coord coord) {
        flagMap.set(coord, Box.CLOSED);
        totalFlaged -= 1;
    }

    int getTotalFlaged() {
        return totalFlaged;
    }

    int getTotalClosed() {
        return totalClosed;
    }

    void setOpenedToBox(Coord coord) {
        flagMap.set(coord, Box.OPENED);
        totalClosed -= 1;
    }

    void toggleFlagedToBox(Coord coord) {
        Box box = flagMap.get(coord);
        switch (box) {
            case CLOSED:
                setFlagedToBox(coord);
                break;
            case FLAGED:
                setClosedToBox(coord);
            default:
                break;
        }
    }

    void setFlagedToLastClosedBoxes() {
        for (Coord coord : Ranges.getAllCoords()) {
            if (Box.CLOSED == flagMap.get(coord)) {
                setFlagedToBox(coord);
            }
        }
    }

    void setBombedToBox(Coord coord) {
        flagMap.set(coord, Box.BOMBED);
    }

    void setOpenedToClosedBox(Coord coord) {
        if (Box.CLOSED == flagMap.get(coord)){
            flagMap.set(coord, Box.OPENED);
        }
    }

    void setNobombToFlagedBox(Coord coord) {
        if (Box.FLAGED == flagMap.get(coord)){
            flagMap.set(coord, Box.NOBOMB);
        }
    }

    int getCountOfFlagedBoxesAround(Coord coord) {
        int count = 0;
        for (Coord around: Ranges.getCoordsAround(coord)){
            if (Box.FLAGED == flagMap.get(around)){
                count += 1;
            }
        }

        return count;
    }
}
