package systems.kinau.fishingbot.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import systems.kinau.fishingbot.auth.AuthData;
import systems.kinau.fishingbot.network.protocol.NetworkHandler;
import systems.kinau.fishingbot.network.protocol.Packet;
import systems.kinau.fishingbot.network.utils.ByteArrayDataInputWrapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketOutChatSessionUpdate extends Packet {

    private AuthData.ProfileKeys keys;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        if (keys == null) return;
        out.writeLong(keys.getChatSessionId().getMostSignificantBits());
        out.writeLong(keys.getChatSessionId().getLeastSignificantBits());
        out.writeLong(keys.getExpiresAt());
        byte[] pubKey = keys.getPublicKey().getEncoded();
        writeVarInt(pubKey.length, out);
        out.write(pubKey);
        byte[] signature = ByteBuffer.wrap(Base64.getDecoder().decode(keys.getPublicKeySignature())).array();
        writeVarInt(signature.length, out);
        out.write(signature);
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {
        // Only outgoing packet
    }
}
