package cryptix.other;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

public class DelayedPacket {
    public final Packet packet;
    public long ticksLeft;

    public DelayedPacket(Packet packet, int delayTicks) {
        this.packet = packet;
        this.ticksLeft = System.currentTimeMillis();
    }
}