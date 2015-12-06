package com.daexsys.automata;

import com.daexsys.automata.event.tile.TilePlacementReason;
import com.daexsys.automata.gui.chat.ChatMessage;
import com.daexsys.automata.world.Chunk;
import com.daexsys.automata.world.tiletypes.TileType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatManager {

    private Game game;

    public ChatManager(Game game) {
        this.game = game;
    }

    private List<ChatMessage> chatMessages = new ArrayList<>();

    public void addChatMessage(ChatMessage chatMessage) {

        if(chatMessage.getMessage().trim().equalsIgnoreCase("Player: /megaboom")) {
            chatMessages.add(new ChatMessage("BOOM", Color.RED));

            try {
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        Chunk chunk = game.getWorld().getChunkManager().getChunk(i, j);

                        Random random = game.getWorld().getRandom();
                        int rx = random.nextInt(16);
                        int ry = random.nextInt(16);

                        chunk.flashWithNewType(0, rx, ry, TileType.BOMB, TilePlacementReason.PLAYER_EDIT);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            chatMessages.add(chatMessage);
        }
    }

    public List<ChatMessage> getChatMessages() {
        return new ArrayList<>(chatMessages);
    }
}
