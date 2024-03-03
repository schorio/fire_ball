import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.util.*;

public class fire_ball extends MIDlet {
    private Display display;
    private GameCanvas gameCanvas;
    private Random random = new Random();

    public fire_ball() {
        display = Display.getDisplay(this);
        gameCanvas = new GameCanvas();
    }

    public void startApp() {
        gameCanvas.balloons.addElement(new Balloon(random.nextInt(gameCanvas.getWidth()), 0));
        display.setCurrent(gameCanvas);
        new Thread(gameCanvas).start();
    }

    public void pauseApp() {}

    public void destroyApp(boolean unconditional) {}

    class GameCanvas extends Canvas implements Runnable {
        int playerX = 0;
        int playerY = getHeight() / 2;
        int dy = 0;
        int balloonSpeed = 2;
        Vector balloons = new Vector();
        Vector bullets = new Vector();

        public void paint(Graphics g) {
            g.setColor(255, 255, 255);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(0, 0, 0);
            g.fillRect(playerX, playerY, 7, 5);

            for (Enumeration e = bullets.elements(); e.hasMoreElements();) {
                Bullet bullet = (Bullet) e.nextElement();
                g.fillRect(bullet.x, bullet.y, 3, 3);
            }

            for (Enumeration e = balloons.elements(); e.hasMoreElements();) {
                Balloon balloon = (Balloon) e.nextElement();
                g.fillArc(balloon.x, balloon.y, 18, 18, 0, 360);
            }
        }

        public void keyPressed(int keyCode) {
            int action = getGameAction(keyCode);
            if (action == UP) {
                dy = -10;
            } else if (action == DOWN) {
                dy = 10;
            }
        }

        public void keyReleased(int keyCode) {
            dy = 0;
        }

        public void run() {
            while (true) {

                playerY += dy;

                for (Enumeration e = bullets.elements(); e.hasMoreElements();) {
                    Bullet bullet = (Bullet) e.nextElement();
                    bullet.x += 10;
                }

                if (random.nextDouble() < 0.05) {
                    int balloonX;
                    do {
                        balloonX = random.nextInt(getWidth());
                    } while (balloonX >= playerX - 20 && balloonX <= playerX + 30);
                    Balloon newBalloon = new Balloon(balloonX, 0);
                    boolean tooClose = false;
                    for (Enumeration e = balloons.elements(); e.hasMoreElements();) {
                        Balloon balloon = (Balloon) e.nextElement();
                        if (Math.abs(balloon.x - newBalloon.x) < 18) {
                            tooClose = true;
                            break;
                        }
                    }
                    if (!tooClose) {
                        balloons.addElement(newBalloon);
                    }
                }

                for (Enumeration e = balloons.elements(); e.hasMoreElements();) {
                    Balloon balloon = (Balloon) e.nextElement();
                    balloon.y += balloonSpeed;
                    if (balloon.y > getHeight()) {
                        balloons.removeElement(balloon);
                    }
                }

                repaint();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Bullet {
        int x, y;

        public Bullet(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class Balloon {
        int x, y;

        public Balloon(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}