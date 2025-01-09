package agh.fcs.oop.model;

public class WorldGlobe extends World {

    public WorldGlobe(int width, int height, int startingGrassAmount) {
        super(width, height, startingGrassAmount);
    }

    @Override
    public void move(Animal animal) {
        Vector2d prevPosition = animal.getPosition();
        MapDirection prevDirection = animal.getFacingDirection();
        animal.move(this);
        Vector2d actualPosition = animal.getPosition();
        MapDirection actualDirection = animal.getFacingDirection();
        boolean boundary = isBoundary(prevPosition, actualDirection);

        if (prevPosition.x() == 0 && actualPosition.x() == 0) {
            if (actualDirection == MapDirection.WEST || actualDirection == MapDirection.NORTHWEST || actualDirection == MapDirection.SOUTHWEST) {
//                animal.move(this);
                Vector2d newPosition = animal.getPosition().add(animal.getFacingDirection().toUnitVector());
                animal.setPosition(new Vector2d(width - 1, newPosition.y()));
                animal.setEnergy(animal.getEnergy() - 1);
                animal.setAge(animal.getAge() + 1);
            }

        }
        else if (prevPosition.x() == width - 1 && actualPosition.x() == width - 1) {
            if (actualDirection == MapDirection.EAST || actualDirection == MapDirection.NORTHEAST || actualDirection == MapDirection.SOUTHEAST) {
//                animal.move(this);
                Vector2d newPosition = animal.getPosition().add(animal.getFacingDirection().toUnitVector());
                animal.setPosition(new Vector2d(0, newPosition.y()));
                animal.setEnergy(animal.getEnergy() - 1);
                animal.setAge(animal.getAge() + 1);
            }
        }

        if (boundary) {
            animal.setFacingDirection(animal.getFacingDirection().oppositeDirection());
        }

        animalMap.remove(prevPosition);
        animalMap.put(animal.getPosition(), animal);
        observersNotification("An animal moved from " + prevPosition + " facing " + prevDirection + " to " + animal.getPosition() + " facing " + animal.getFacingDirection()); // + " (turned to " + actualDirection + " before moving)");
    }

    public boolean isBoundary(Vector2d prevPosition, MapDirection prevDirection) {
        boolean boundary = false;
        if (prevPosition.y() == 0) {
            if (prevDirection == MapDirection.SOUTH || prevDirection == MapDirection.SOUTHWEST || prevDirection == MapDirection.SOUTHEAST) {
                boundary = true;
            }
        }
        if (prevPosition.y() == height - 1) {
            if (prevDirection == MapDirection.NORTH || prevDirection == MapDirection.NORTHWEST || prevDirection == MapDirection.NORTHEAST) {
                boundary = true;
            }
        }
        return boundary;
    }
}
