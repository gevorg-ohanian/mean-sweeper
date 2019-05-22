package sweeper;

public class Game {

    private Bomb bomb;
    private Flag flag;
    private GameState state;

    public Game(int cols, int rows, int bombsCount) {
        Ranges.setSize(new Coord(rows, cols));
        bomb = new Bomb(bombsCount);
        flag = new Flag();
    }

    public Box getBox(Coord coord) {
        if (Box.OPENED == flag.get(coord)) {
            return bomb.get(coord);
        } else {
            return flag.get(coord);
        }
    }

    public void start() {
        bomb.start();
        flag.start();
        state = GameState.PLAYED;
    }

    public GameState getState() {
        return state;
    }

    public void pressLeftButton(Coord coord) {
        if (isGameOver()) {
            return;
        }
        openBox(coord);
        checkWinner();
    }

    private void checkWinner() {
        if (GameState.PLAYED == state) {
            if (flag.getTotalClosed() == getTotalBombs()) {
                state = GameState.WINNED;
                flag.setFlagedToLastClosedBoxes();
            }
        }
    }

    private void openBox(Coord coord) {
        switch (flag.get(coord)) {
            case OPENED:
                setOpenedToClosedBoxesAroundNumber(coord);
                break;
            case FLAGED:
                break;
            case CLOSED:
                switch (bomb.get(coord)) {
                    case BOMB:
                        openBombs(coord);
                        break;
                    case ZERO:
                        openBoxesAroundZero(coord);
                        break;
                    default:
                        flag.setOpenedToBox(coord);
                }
        }
    }

    private void setOpenedToClosedBoxesAroundNumber(Coord coord) {
        Box lBox = bomb.get(coord);

        if (Box.BOMB != lBox){
            if (lBox.getNumber() == flag.getCountOfFlagedBoxesAround(coord)){
                for (Coord around: Ranges.getCoordsAround(coord)){
                    if (Box.CLOSED == flag.get(around)){
                        openBox(around);
                    }
                }
            }
        }
    }

    private void openBombs(Coord bombedCoord) {
        flag.setBombedToBox(bombedCoord);
        for (Coord coord: Ranges.getAllCoords()){
            if (Box.BOMB == bomb.get(coord)){
                flag.setOpenedToClosedBox(coord);
            }else {
                flag.setNobombToFlagedBox(coord);
            }
        }
        state = GameState.BOMBED;
    }

    private void openBoxesAroundZero(Coord coord) {
        flag.setOpenedToBox(coord);

        for (Coord around : Ranges.getCoordsAround(coord)) {
            openBox(around);
        }
    }

    public void pressRightButton(Coord coord) {
        if (isGameOver()) {
            return;
        }
        flag.toggleFlagedToBox(coord);
    }

    private boolean isGameOver() {
        if (GameState.PLAYED != state) {
            start();
            return true;
        }

        return false;
    }

    public int getTotalBombs() {
        return bomb.getTotalBombs();
    }

    public int getTotalFlaged() {
        return flag.getTotalFlaged();
    }

}
