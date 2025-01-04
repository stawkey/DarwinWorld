package agh.fcs.oop;

import agh.fcs.oop.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    World world = new World(20, 20, 10);
    AnimalConfig animalConfig = new AnimalConfig(10, 4, 1, 5, 8);

    @Test
    public void testAnimalInitialization() {
        Vector2d startPosition = new Vector2d(2, 2);

        Animal animal = new Animal(startPosition, animalConfig);

        assertEquals(startPosition, animal.getPosition());
        assertEquals(10, animal.getEnergy());
        assertEquals(0, animal.getAge());
        assertEquals(0, animal.getChildrenNumber());
    }

    @Test
    public void testAnimalMove() {
        Animal animal = new Animal(new Vector2d(9, 9), animalConfig);

        for (int i = animal.getCurrGene(); i < animal.getCurrGene() + animal.getGene().size(); i++) {
            Vector2d oldPosition = animal.getPosition(); // 4,4
            MapDirection oldDirection = animal.getFacingDirection(); // ne
            int oldGene = animal.getGene().get(i); // 0
//            System.out.println(oldPosition + "   " + oldDirection + "   " + oldGene);

            animal.move(world);
            assertEquals(oldPosition.add(oldDirection.rotateBy(oldGene).toUnitVector()), animal.getPosition());
        }
    }

    @Test
    public void testAnimalMoveOutOfMap()
    {
        World world2 = new World(1, 1, 10);
        Animal animal = new Animal(new Vector2d(0,0), animalConfig);
        for (int i = 0; i < animal.getGene().size(); i++) {
            animal.move(world2);
            assertEquals(new Vector2d(0,0), animal.getPosition());
        }
    }

    @Test
    public void testAnimalReproduce() {
        Vector2d position = new Vector2d(0, 0);
        Animal parent1 = new Animal(position, animalConfig);
        Animal parent2 = new Animal(position, animalConfig);

        parent1.reproduce(parent2);

        assertEquals(6, parent1.getEnergy());
        assertEquals(6, parent2.getEnergy());
        assertEquals(1, parent1.getChildrenNumber());
        assertEquals(1, parent2.getChildrenNumber());
    }

    @Test
    public void testAnimalDiesWhenEnergyDepleted() {
        Vector2d position = new Vector2d(2, 2);

        Animal animal = new Animal(position, animalConfig);
        animal.setEnergy(0);

        assertFalse(animal.isAlive());
    }

    @Test
    public void testAnimalMutation() {
        Vector2d position = new Vector2d(2, 2);

        Animal parent1 = new Animal(position, animalConfig);
        Animal parent2 = new Animal(position, animalConfig);

        parent1.reproduce(parent2);
    }
}

