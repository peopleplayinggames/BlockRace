package yt.ppg.blockrace;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import yt.ppg.blockrace.util.CC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class PlayerSettings {

    private final Player player;
    private final BossBar bossBar = Bukkit.createBossBar(CC.translate("&e&lWaiting for game to start"), BarColor.YELLOW, BarStyle.SOLID);
    private Material currentBlock = null;
    private List<Material> completedBlocks = new ArrayList<>();
    private int score = 0;

    public void init() {
        bossBar.addPlayer(player);
    }

    public void change(boolean score) {
        if (score) this.score++;

        List<Material> blocks = new ArrayList<>(Core.getInstance().getPossibleBlocks());

        completedBlocks.add(currentBlock);

        for (Material completed : completedBlocks) {
            blocks.remove(completed);
        }

        if (blocks.size() == 0) {
            Bukkit.broadcastMessage(CC.translate("&e&l" + player.getName() + " has completed the race"));
            return;
        }

        int index = new Random().nextInt(blocks.size() - 1);
        Material newBlock = (Material) blocks.toArray()[index];
        currentBlock = newBlock;
        player.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "NEW BLOCK: " + newBlock.name().replace("_", " "));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);

    }

    public void bossBarLoop() {
        bossBar.setColor(BarColor.GREEN);
        bossBar.setProgress(Core.getInstance().getTimeLeft() / 1200f);

        int mins = Core.getInstance().getTimeLeft() / 60;
        int seconds = Core.getInstance().getTimeLeft() % 60;

        bossBar.setTitle(CC.translate("&a&l" + mins + ":" + (seconds < 10 ? "0" : "") + seconds));
    }

    public void endBossBar(PlayerSettings playerSettings) {
        bossBar.setProgress(1);
        bossBar.setColor(BarColor.YELLOW);
        bossBar.setTitle(CC.translate("&6&l" + playerSettings.getPlayer().getName() + " has won the game!"));
    }

}
