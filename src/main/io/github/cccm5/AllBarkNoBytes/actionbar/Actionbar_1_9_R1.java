package io.github.cccm5.AllBarkNoBytes.actionbar;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;

import org.bukkit.entity.Player;

public class Actionbar_1_9_R1 implements Actionbar {

    @Override
    public void sendActionbar(Player p, String message) {

        IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");

        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
    }
}
