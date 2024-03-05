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
        int score = 0;
        int level = 1;
        int life = 10;
        boolean paused = false;
        Vector balloons = new Vector();
        Vector bullets = new Vector();

        public void paint(Graphics g) {
            g.setColor(255, 255, 255);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(0, 0, 0);
            g.fillRect(playerX, playerY, 7, 5);
            g.drawString("Level: " + level, 10, getHeight() - 20, Graphics.TOP | Graphics.LEFT);
            g.drawString("Score: " + score, getWidth() - 50, getHeight() - 20, Graphics.TOP | Graphics.LEFT);
            g.drawString("Life: " + life, getWidth() - 135, getHeight() - 20, Graphics.TOP | Graphics.LEFT);

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
            } else if (action == FIRE) {
                bullets.addElement(new Bullet(playerX + 10, playerY + 2));
            } else if (keyCode == KEY_NUM0) {
                paused = !paused;
            }
        }

        public void keyReleased(int keyCode) {
            dy = 0;
        }

        public void run() {
            while (true) {
                if (!paused) {

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
                            if (life > 0) {
                                life--;
                            }
                        }
                        for (Enumeration be = bullets.elements(); be.hasMoreElements();) {
                            Bullet bullet = (Bullet) be.nextElement();
                            if (bullet.x >= balloon.x && bullet.x <= balloon.x + 18 && bullet.y >= balloon.y && bullet.y <= balloon.y + 18) {
                                balloons.removeElement(balloon);
                                bullets.removeElement(bullet);
                                score++;
                                if (score % 5 == 0) {
                                    level++;
                                    balloonSpeed++;
                                }
                                break;
                            }
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