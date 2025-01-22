package agh.fcs.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    World world = new World(10, 10, 20);
    World world2 = new World(20, 20, 25);

    @Test
    void testInitialDimensions() {
        assertEquals(10, world.getWidth());
        assertEquals(10, world.getHeight());
        assertNotEquals(25, world2.getHeight());
    }

    @Test
    void testEquatorCalculations() {
        Vector2d equatorLeft = world.getEquatorLeftCorner();
        Vector2d equatorRight = world.getEquatorRightCorner();

        assertNotNull(equatorLeft);
        assertNotNull(equatorRight);
        assertTrue(equatorLeft.precedes(equatorRight));
        assertEquals(world2.getEquatorLeftCorner(), new Vector2d(0, 8));
        assertEquals(world2.getEquatorRightCorner(), new Vector2d(world2.getWidth() - 1, 11));
    }

    @Test
    void testGrassGeneration() {
        assertTrue(world.grassCount() <= 20);
        assertFalse(world2.grassCount() > 50);
    }

    @Test
    void testAnimalPlacement() throws IncorrectPositionException {
        Animal animal = new Animal(new Vector2d(3, 3), new AnimalConfig(1,1,1,1,1), false);
        assertTrue(world.place(animal));
        assertEquals(animal, world.objectAt(new Vector2d(3, 3)));
    }

    @Test
    void testAnimalPlacementOutOfBounds() {
        Animal animal = new Animal(new Vector2d(15, 15),new AnimalConfig(1,1,1,1,1), false);
        assertThrows(IncorrectPositionException.class, () -> world.place(animal));
    }

    @Test
    void testGrassRemoval() {
        Vector2d grassPosition = world.grassMap.keySet().iterator().next();
        world.removeGrass(grassPosition);
        assertNull(world.objectAt(grassPosition));
    }

    @Test
    void testMovement() throws IncorrectPositionException {
        Animal animal = new Animal(new Vector2d(3, 3), new AnimalConfig(1,1,1,1,1), false);
        world.place(animal);
        world.move(animal);

        assertNotEquals(new Vector2d(3, 3), animal.getPosition());
        assertTrue(world.isOccupied(animal.getPosition()));
    }

    @Test
    void testOccupiedField() {
        Vector2d position = new Vector2d(5, 5);
        Animal animal = new Animal(position, new AnimalConfig(1,1,1,1,1), false);

        assertDoesNotThrow(() -> world.place(animal));
        assertTrue(world.isOccupied(position));
    }

    @Test
    void testObserverNotifications() {
        TestMapChangeListener listener = new TestMapChangeListener();
        world.addObserver(listener);

        world.observersNotification("Test message");
        assertEquals("Test message", listener.lastMessage);
    }

    static class TestMapChangeListener implements MapChangeListener {
        String lastMessage;

        @Override
        public void mapChanged(World map, String message) {
            this.lastMessage = message;
        }
    }
}
