# VoxelShapes for Beta 1.7.3
Fully adds the ability to have multiple outline/collision shapes in one block! Completely compatible with server and client, and modifies Vanilla blocks.

# Base Feature
Fixes stairs and fences collision shape to go to individual smaller boxes rather than the whole block taking up the space.
![Demo Image](https://i.imgur.com/yCJNseu.png)

# API

You can utilize this mod in your mod by giving your blocks multiple boxes!  
`HasVoxelShape` will automatically affect collision, if you want a different collision also implement `HasCollisionVoxelShape`.  
You can also implement `HasCollisionVoxelShape` by itself, to only give collision multiple boxes.

```java
class ExampleBlock extends Block implements HasVoxelShape, HasCollisionVoxelShape {
    public ExampleBlock(int id, Material material) {
        super(id, material);
    }

    @Override
    public Box[] getVoxelShape(World world, int x, int y, int z) {
        // Create one orientation of the stair
        // You could check the blockstate/meta here with world to chnage based off the block
        // Demo of a block made of two vertical half slabs
        Box[] boxes = new Box[2];
        boxes[0] = Box.create(x, y, z, x + 0.5F, y + 1.F, z + 1.F);
        boxes[1] = Box.create(x + 0.5F, y, z, x + 1.F, y + 1.F, z + 1.F);
        return boxes;
    }

    public Box[] getCollisionVoxelShape(World world, int x, int y, int z) {
        // Example of giving this block no collision
        return new Box[0];
    }
}
```
