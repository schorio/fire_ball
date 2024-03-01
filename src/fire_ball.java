import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.util.*;

public class fire_ball extends MIDlet {
    private Display display;
    private GameCanvas gameCanvas;

    public fire_ball() {
        display = Display.getDisplay(this);
        gameCanvas = new GameCanvas();
    }

    public void startApp() {
        display.setCurrent(gameCanvas);
    }

    public void pauseApp() {}

    public void destroyApp(boolean unconditional) {}

    class GameCanvas extends Canvas {
        int playerX = 0;
        int playerY = getHeight() / 2;
        int dy = 0;

        public void paint(Graphics g) {
            g.setColor(255, 255, 255);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(0, 0, 0);
            g.fillRect(playerX, playerY, 7, 5);
        }

    }
}