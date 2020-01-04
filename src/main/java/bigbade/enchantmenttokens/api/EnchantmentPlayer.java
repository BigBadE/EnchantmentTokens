package bigbade.enchantmenttokens.api;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.utils.ByteUtils;
import org.bukkit.entity.Player;

import java.io.*;

public class EnchantmentPlayer implements Serializable {
    private long gems;
    private Player player;
    private boolean newPlayer;

    private EnchantmentPlayer(Player player, long gems, boolean newPlayer) {
        this.gems = gems;
        this.player = player;
        this.newPlayer = newPlayer;
    }

    public static EnchantmentPlayer loadPlayer(Player player, EnchantmentTokens main, ByteUtils utils) {
        File playerFile = new File(main.getDataFolder().getAbsolutePath() + "\\data\\" + player.getUniqueId().toString().substring(0, 2) + "\\data.dat");
        if(!playerFile.exists()) {
            try {
                playerFile.getParentFile().mkdir();
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileInputStream stream = new FileInputStream(playerFile);
                for (int i = 0; i < stream.available() / 24; i += 1) {
                    byte[] temp = new byte[8];
                    stream.read(temp);
                    long mostSigBits = utils.bytesToLong(temp);
                    if (mostSigBits == player.getUniqueId().getMostSignificantBits()) {
                        stream.read(temp);
                        long leastSigBits = utils.bytesToLong(temp);
                        if (leastSigBits == player.getUniqueId().getLeastSignificantBits()) {
                            stream.read(temp);
                            return new EnchantmentPlayer(player, utils.bytesToLong(temp), false);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new EnchantmentPlayer(player, 0, true);
    }

    public void save(ByteUtils utils, EnchantmentTokens main) {
        try {
            String name = main.getDataFolder().getAbsolutePath() + "\\data\\" + player.getUniqueId().toString().substring(0, 2) + "\\data.dat";
            if(newPlayer) {
                FileOutputStream writer = new FileOutputStream(name, true);
                byte[] temp = utils.longToBytes(player.getUniqueId().getMostSignificantBits());
                writer.write(temp);
                temp = utils.longToBytes(player.getUniqueId().getLeastSignificantBits());
                writer.write(temp);
                temp = utils.longToBytes(getGems());
                writer.write(temp);
            } else {
                FileOutputStream writer = new FileOutputStream(name, false);
                byte[] data = new byte[24];
                System.arraycopy(utils.longToBytes(player.getUniqueId().getMostSignificantBits()), 0, data, 0, 8);
                System.arraycopy(utils.longToBytes(player.getUniqueId().getLeastSignificantBits()), 0, data, 8, 8);
                System.arraycopy(utils.longToBytes(gems), 0, data, 16, 8);
                FileInputStream stream = new FileInputStream(name);
                for (int i = 0; i < stream.available(); i += 24) {
                    byte[] temp = new byte[8];
                    stream.read(temp);
                    long mostSigBits = utils.bytesToLong(temp);
                    if(mostSigBits == player.getUniqueId().getMostSignificantBits()) {
                        stream.read(temp);
                        long leastSigBits = utils.bytesToLong(temp);
                        if(leastSigBits == player.getUniqueId().getLeastSignificantBits()) {
                            writer.write(data, i, 24);
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public long getGems() {
        return gems;
    }

    public void addGems(long amount) {
        this.gems += amount;
    }
}
