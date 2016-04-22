package com.example.CuccoRun;

/**
 * Created by darwinmendyke on 7/9/14.
 */
public class CollisionDetector {

    public CollisionDetector() {

    }

    // returns a HashSet of strings that list all the pixels in an image that aren't transparent
    // the pixels contained in the HashSet follow the guideline of:
    // x,y where x is the absolute x position of the pixel and y is the absolute y position of the pixel
    /**public HashSet<String> getMask(AnimatedObject object){

        HashSet<String> mask = new HashSet<String>();
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File (object.getDefaultImageLocation()));
        } catch (IOException e) {
            System.out.println("error");
        }
        int pixel, a;
        for(int i = 0; i < image.getWidth(); i++){ // for every (x,y) component in the given box,
            for( int j = 0; j < image.getHeight(); j++){

                pixel = image.getRGB(i, j); // get the RGB value of the pixel
                a= (pixel >> 24) & 0xff;

                if(a != 0){  // if the alpha is not 0, it must be something other than transparent
                    mask.add((go.xPos+i)+","+(go.yPos- j)); // add the absolute x and absolute y coordinates to our set
                }
            }
        }

        return mask;  //return our set

    }

    // Returns true if there is a collision between object a and object b
    public boolean checkCollision(AnimatedObject a, AnimatedObject b){

        // This method detects to see if the images overlap at all. If they do, collision is possible
        int ax1 = a.getxPos();
        int ay1 = a.getyPos();
        int ax2 = ax1 + a.getWidth();
        int ay2 = ay1 + a.getHeight();
        int bx1 = b.getxPos();
        int by1 = b.getyPos();
        int bx2 = bx1 + b.getWidth();
        int by2 = by1 + b.getHeight();

        if(by2 < ay1 || ay2 < by1 || bx2 < ax1 || ax2 < bx1)
        {
            return false; // Collision is impossible.
        }
        else // Collision is possible.
        {
            // get the masks for both images
            HashSet<String> maskPlayer1 = getMask(player1);
            HashSet<String> maskPlayer2 = getMask(player2);

            maskPlayer1.retainAll(maskPlayer2);  // Check to see if any pixels in maskPlayer2 are the same as those in maskPlayer1

            if(maskPlayer1.size() > 0){  // if so, than there exists at least one pixel that is the same in both images, thus
                System.out.println("Collision" + count);//  collision has occurred.
                count++;
                return true;

            }
        }
        return false;
    }**/
}
