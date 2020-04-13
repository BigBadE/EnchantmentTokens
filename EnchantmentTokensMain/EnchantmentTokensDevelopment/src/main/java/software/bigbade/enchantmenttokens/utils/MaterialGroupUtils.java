/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils;

import org.bukkit.Material;

public enum MaterialGroupUtils {
    PICKAXES(new Material[]{Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE}),
    SWORDS(new Material[]{Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD}),
    AXES(new Material[]{Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE}),
    HOES(new Material[]{Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE}),
    SHOVELS(new Material[] { Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL }),
    TOOLS(joinAll(PICKAXES.getMaterials(), AXES.getMaterials(), HOES.getMaterials(), SHOVELS.getMaterials())),
    LEATHER_ARMOR(new Material[] { Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS }),
    CHAINMAIL_ARMOR(new Material[] { Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS }),
    IRON_ARMOR(new Material[] { Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS }),
    GOLD_ARMOR(new Material[] { Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS }),
    DIAMOND_ARMOR(new Material[] { Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS }),
    HELMETS(new Material[] { Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET }),
    CHESTPLATE(new Material[] { Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE }),
    LEGGINGS(new Material[] { Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS }),
    BOOTS(new Material[] { Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS }),
    ARMOR(joinAll(LEATHER_ARMOR.getMaterials(), CHAINMAIL_ARMOR.getMaterials(), IRON_ARMOR.getMaterials(), GOLD_ARMOR.getMaterials(), DIAMOND_ARMOR.getMaterials()));
    private Material[] materials;

    MaterialGroupUtils(Material[] materials) {
        this.materials = materials;
    }

    public Material[] getMaterials() {
        return materials;
    }

    private static Material[] joinAll(Material[]... materials) {
        int adding = 0;
        for(Material[] found : materials)
            adding += found.length;
        Material[] merged = new Material[adding];
        int added = 0;
        for(Material[] materialArray : materials) {
            System.arraycopy(materialArray, 0, merged, added, materialArray.length);
            added += materialArray.length;
        }
        return merged;
    }
}
