import processing.core.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
   extends PApplet
{
   private static final String WYVERN_KEY = "Wyvern";
   private static final String WYVERN_ID = "Wyvern";
   private static final int WYVERN_LIMIT = 100;
   private static final int WYVERN_ACTION_PERIOD = 5;
   private static final int WYVERN_ANIMATION_PERIOD = 6;

   private static final String VEGAN_VEGAN_OCTO_KEY = "v";
   private static final String VEGAN_OCTO_ID = "v";
   private static final int VEGAN_OCTO_LIMIT = 4;
   private static final int VEGAN_OCTO_ACTION_PERIOD = 3;
   private static final int VEGAN_OCTO_ANIMATION_PERIOD = 4;


   private static final int TILE_SIZE = 32;
   public static final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 1280;
   private static final int VIEW_HEIGHT = 960;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "world.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   public ImageStore imageStore;
   public WorldModel world;
   public WorldView view;
   public EventScheduler scheduler;

   public long next_time;
   private Object Point;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }
   public void mousePressed()
   {
      Point pressed = mouseToPoint(mouseX, mouseY);
      Background Lava = new Background("Lava", imageStore.getImageList("Lava"));
      Point top_Right = new Point(pressed.x+1, pressed.y+1);
      Point top_Middle = new Point(pressed.x, pressed.y+1);
      Point top_Left = new Point(pressed.x-1, pressed.y+1);
      Point middle = new Point(pressed.x, pressed.y);
      Point middle_Right = new Point(pressed.x+1, pressed.y);
      Point middle_Left = new Point(pressed.x-1, pressed.y);
      Point bottom_Right = new Point(pressed.x+1, pressed.y-1);
      Point bottom_Left = new Point(pressed.x-1, pressed.y-1);
      Point bottom_Middle = new Point(pressed.x, pressed.y-1);





      if(this.world.getOccupancyCell(middle) == null){
         Wyvern wyvern = new Wyvern(WYVERN_ID, middle, imageStore.getImageList(WYVERN_KEY),
                 WYVERN_ACTION_PERIOD, WYVERN_ANIMATION_PERIOD, WYVERN_LIMIT, 0);
         this.world.addEntity(wyvern);
         wyvern.scheduleActions(scheduler, world, imageStore);
      }

      if(this.world.getOccupancyCell(top_Right) == null){
         this.world.setBackgroundCell(top_Right, Lava);
      }
      else if (this.world.getOccupant(top_Right).get().getClass() == Octo.class){
         scheduler.unscheduleAllEvents(this.world.getOccupant(top_Right).get());
         world.removeEntity(this.world.getOccupant(top_Right).get());

         VeganOcto veganOcto = new VeganOcto(VEGAN_OCTO_ID, top_Right, imageStore.getImageList(VEGAN_VEGAN_OCTO_KEY),
              VEGAN_OCTO_ACTION_PERIOD, VEGAN_OCTO_ANIMATION_PERIOD, VEGAN_OCTO_LIMIT, 0);
         this.world.addEntity(veganOcto);
         veganOcto.scheduleActions(scheduler, world, imageStore);
      }

      if(this.world.getOccupancyCell(top_Middle) == null){
         this.world.setBackgroundCell(top_Middle, Lava);
      }
      if(this.world.getOccupancyCell(top_Left) == null){
         this.world.setBackgroundCell(top_Left, Lava);
      }
      if(this.world.getOccupancyCell(middle_Right) == null){
         this.world.setBackgroundCell(middle_Right, Lava);
      }
      if(this.world.getOccupancyCell(middle_Left) == null){
         this.world.setBackgroundCell(middle_Left, Lava);
      }
      if(this.world.getOccupancyCell(bottom_Right) == null){
         this.world.setBackgroundCell(bottom_Right, Lava);
      }
      if(this.world.getOccupancyCell(bottom_Left) == null){
         this.world.setBackgroundCell(bottom_Left, Lava);
      }
      if(this.world.getOccupancyCell(bottom_Middle) == null){
         this.world.setBackgroundCell(bottom_Middle, Lava);
      }

      redraw();

   }

   private Point mouseToPoint(int x, int y)
   {
      return new Point(mouseX/TILE_SIZE, mouseY/TILE_SIZE);
   }



   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         view.shiftView(dx, dy);
      }
   }

   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   public static void loadWorld(WorldModel world, String filename,
                                ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         world.load(in, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }




   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getentities())
      {
         //Only start actions for entities that include action (not those with just animations)

         if (entity.getClass().getSuperclass() == Actionable.class || entity.getClass().getSuperclass() == Moveable.class){
            entity.scheduleActions(scheduler, world, imageStore);}
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
