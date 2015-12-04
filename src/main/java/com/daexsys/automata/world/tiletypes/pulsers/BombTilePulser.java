package com.daexsys.automata.world.tiletypes.pulsers;

import com.daexsys.automata.Tile;
import com.daexsys.automata.gui.chat.ChatMessage;
import com.daexsys.automata.world.World;
import com.daexsys.automata.world.WorldLayer;
import com.daexsys.automata.world.tiletypes.TilePulser;
import com.daexsys.automata.world.tiletypes.TileType;

import java.awt.*;

public class BombTilePulser implements TilePulser {

    @Override
    public void pulse(Tile t) {

    }

    @Override
    public void destruct(Tile tile) {
        World world = tile.getWorld();

        int size = world.getRandom().nextInt(5);

        for (int i = 0; i < size; i++) {
            int lakeX = world.getRandom().nextInt(size) + tile.getCoordinate().x;
            int lakeY = world.getRandom().nextInt(size) + tile.getCoordinate().y;
            int lakeSize = world.getRandom().nextInt(15) + 3;

            for (int fx = lakeX - lakeSize; fx < lakeX + lakeSize; fx++) {
                for (int fy = lakeY - lakeSize; fy < lakeY + lakeSize; fy++) {
                    int x = fx - lakeX;
                    int y = fy - lakeY;

                    if((x * x + y * y) < lakeSize * lakeSize) {
                        world.setTileTypeAt(WorldLayer.GROUND, fx, fy, TileType.DIRT);
                        world.setTileTypeAt(WorldLayer.ABOVE_GROUND, fx, fy, TileType.SMOKE);
                    }
                }
            }
        }

        tile.getWorld().getGame().getChatManager().addChatMessage(new ChatMessage("Pop goes the weasel @ " + tile.getCoordinate(), Color.RED));
    }
}
