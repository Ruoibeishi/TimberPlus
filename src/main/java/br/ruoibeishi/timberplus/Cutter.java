/*
⠄⠄⠄⢰⣧⣼⣯⠄⣸⣠⣶⣶⣦⣾⠄⠄⠄⠄⡀⠄⢀⣿⣿⠄⠄⠄⢸⡇⠄⠄
⠄⠄⠄⣾⣿⠿⠿⠶⠿⢿⣿⣿⣿⣿⣦⣤⣄⢀⡅⢠⣾⣛⡉⠄⠄⠄⠸⢀⣿⠄
⠄⠄⢀⡋⣡⣴⣶⣶⡀⠄⠄⠙⢿⣿⣿⣿⣿⣿⣴⣿⣿⣿⢃⣤⣄⣀⣥⣿⣿⠄
⠄⠄⢸⣇⠻⣿⣿⣿⣧⣀⢀⣠⡌⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠿⠿⣿⣿⣿⠄
⠄⢀⢸⣿⣷⣤⣤⣤⣬⣙⣛⢿⣿⣿⣿⣿⣿⣿⡿⣿⣿⡍⠄⠄⢀⣤⣄⠉⠋⣰
⠄⣼⣖⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⣿⣿⣿⣿⣿⢇⣿⣿⡷⠶⠶⢿⣿⣿⠇⢀⣤
⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣽⣿⣿⣿⡇⣿⣿⣿⣿⣿⣿⣷⣶⣥⣴⣿⡗
⢀⠈⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠄
⢸⣿⣦⣌⣛⣻⣿⣿⣧⠙⠛⠛⡭⠅⠒⠦⠭⣭⡻⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⠄
⠘⣿⣿⣿⣿⣿⣿⣿⣿⡆⠄⠄⠄⠄⠄⠄⠄⠄⠹⠈⢋⣽⣿⣿⣿⣿⣵⣾⠃⠄
⠄⠘⣿⣿⣿⣿⣿⣿⣿⣿⠄⣴⣿⣶⣄⠄⣴⣶⠄⢀⣾⣿⣿⣿⣿⣿⣿⠃⠄⠄
⠄⠄⠈⠻⣿⣿⣿⣿⣿⣿⡄⢻⣿⣿⣿⠄⣿⣿⡀⣾⣿⣿⣿⣿⣛⠛⠁⠄⠄⠄
⠄⠄⠄⠄⠈⠛⢿⣿⣿⣿⠁⠞⢿⣿⣿⡄⢿⣿⡇⣸⣿⣿⠿⠛⠁⠄⠄⠄⠄⠄
⠄⠄⠄⠄⠄⠄⠄⠉⠻⣿⣿⣾⣦⡙⠻⣷⣾⣿⠃⠿⠋⠁⠄⠄⠄⠄⠄⢀⣠⣴
⣿⣿⣿⣶⣶⣮⣥⣒⠲⢮⣝⡿⣿⣿⡆⣿⡿⠃⠄⠄⠄⠄⠄⠄⠄⣠⣴⣿⣿⣿
 */
package br.ruoibeishi.timberplus;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class Cutter {
    private static final List<Material> LOGS = Arrays.asList(
            Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG,
            Material.JUNGLE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG
    );

    static boolean isLogBlock(Block block) {
        return LOGS.contains(block.getType());
    }

    static int chopTree(final Location location) {
        List<Block> blocksToChop = new LinkedList<>();
        int durabilityCost = 1; // We initialize it to 1 to count the log chopped by the player

        // Break all blocks in the Y axis
        for (int i = location.getBlockY(); i < getHighest(location); i++) {
            /*
            // Makes a list of relatives blocks (North, South, East and West)
            // Index 0 -> North
            // Index 1 -> South
            // Index 2 -> East
            // Index 3 -> West
            List<Block> brokenBlockRelatives = getRelativesLogs(location.getBlock());

            // For each relative block
            for (Block block : brokenBlockRelatives) {
                // If it's a log, add it to the blocks list
                if (block != null) blocksToChop.add(block);
            }
            */

            Location upperBlock = location.add(0, 1, 0);
            Material blockType = upperBlock.getBlock().getType();

            if (LOGS.contains(blockType)) {
                blocksToChop.add(upperBlock.getBlock());
            } else break;

        }

        for (Block block : blocksToChop) {
            block.breakNaturally(new ItemStack(Material.DIAMOND_AXE));
            durabilityCost++;
        }

        return durabilityCost;
    }

    // TODO: Improve the logic so that every log connected will be chopped
    /*
    // Check whether there's a log N/S/E/W of the target block
    private static List<Block> getRelativesLogs(Block block) {
        // List to store blocks at N/S/E/W position
        List<Block> blocks = Arrays.asList(null, null, null, null);

        Block nBlock = block.getRelative(0, 0, -1); // Get block to north
        Block sBlock = block.getRelative(0, 0, 1); // Get block to south
        Block eBlock = block.getRelative(1, 0, 0); // Get block to east
        Block wBlock = block.getRelative(-1, 0, 0); // Get block to west

        // If those blocks are valid logs, add them to the list
        if (LOGS.contains(nBlock.getType())) blocks.set(0, nBlock);
        if (LOGS.contains(sBlock.getType())) blocks.set(1, sBlock);
        if (LOGS.contains(eBlock.getType())) blocks.set(2, eBlock);
        if (LOGS.contains(wBlock.getType())) blocks.set(3, wBlock);

        return blocks;
    }
    */

    // Get highest block at chop X/Z location
    private static int getHighest(final Location location) {
        World world = location.getWorld();

        if (world == null) return -1;

        return location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    static boolean isAxe(Material material) {
        boolean isAxe;

        switch (material) {
            case WOODEN_AXE:
            case STONE_AXE:
            case IRON_AXE:
            case GOLDEN_AXE:
            case DIAMOND_AXE:
                isAxe = true;
                break;
            default:
                isAxe = false;
                break;
        }

        return isAxe;
    }
}
