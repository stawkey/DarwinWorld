package agh.fcs.oop;

import agh.fcs.oop.model.Vector2d;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {

    private final Vector2d vec1 = new Vector2d(1, 0);
    private final Vector2d vec2 = new Vector2d(1, 0);
    private final Vector2d vec3 = new Vector2d(0, -1);
    private final Vector2d vec4 = new Vector2d(1, -2);
    private final Vector2d vec5 = new Vector2d(1, -1);
    private final Vector2d vec6 = new Vector2d(0, -2);
    private final Vector2d vec7 = new Vector2d(0, 0);
    private final Vector2d vec8 = new Vector2d(-1, 2);

    @Test
    public void testEquals() {
        assertAll(
                () -> assertEquals(vec1, vec1),
                () -> assertEquals(vec1, vec2),
                () -> assertNotEquals(vec1, vec3),
                () -> assertNotEquals(vec1, vec7)
        );
    }

    @Test
    public void testToString() {
        assertAll(
                () -> assertEquals("(1,0)", vec1.toString()),
                () -> assertEquals("(0,-1)", vec3.toString())
        );
    }

    @Test
    public void testPrecedes() {
        assertAll(
                () -> assertTrue(vec3.precedes(vec1)),
                () -> assertFalse(vec1.precedes(vec3))
        );
    }

    @Test
    public void testFollows() {
        assertAll(
                () -> assertTrue(vec2.follows(vec3)),
                () -> assertFalse(vec3.follows(vec2))
        );
    }

    @Test
    public void testUpperRight() {
        assertEquals(vec5, vec3.upperRight(vec4));
    }

    @Test
    public void testLowerLeft() {
        assertEquals(vec6, vec3.lowerLeft(vec4));
    }

    @Test
    public void testAdd() {
        assertEquals(vec5, vec1.add(vec3));
    }

    @Test
    public void testSubtract() {
        assertEquals(vec7, vec1.subtract(vec2));
    }

    @Test
    public void testOpposite() {
        assertEquals(vec8, vec4.opposite());
    }
}
