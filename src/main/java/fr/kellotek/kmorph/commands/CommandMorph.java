package fr.kellotek.kmorph.commands;

import fr.kellotek.kmorph.KMorph;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandMorph implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        Player player = (Player) sender;

        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Type the command /morph help.");
            return true;
        }

        switch (args[0]) {

            case "help" :
                player.sendMessage("--------------------------------------------------");
                player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "/morph help " + ChatColor.RESET + "" + ChatColor.WHITE + "[Displayed the helps].");
                player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "/morph set " + ChatColor.RESET + "" + ChatColor.WHITE + "[Allows you to transform yourself into an entity].");
                player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "/morph remove " + ChatColor.RESET + "" + ChatColor.WHITE + "[Allows you to detransform].");
                player.sendMessage("--------------------------------------------------");
                break;

            case "set" :
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "You have not indicated an Entity.");
                    break;
                }
                if (KMorph.morphed.containsKey(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You already have a transformation.");
                    break;
                }
                try {
                    EntityType et = EntityType.valueOf(args[1].toUpperCase());
                    if (!et.isAlive()) {
                        player.sendMessage(ChatColor.RED + "Your entity is not a living entity.");
                        break;
                    }
                    player.setInvisible(true);
                    player.setInvulnerable(true);
                    LivingEntity morph = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), et);
                    KMorph.morphed.put(player.getUniqueId(), morph.getUniqueId());
                } catch (IllegalArgumentException e) {
                    player.sendMessage(ChatColor.RED + "The Entite you specified is not valid.");
                }
                break;

            case "remove" :
                if (KMorph.morphed.get(player.getUniqueId()) == null) {
                    player.sendMessage(ChatColor.RED + "You are currently not transformed.");
                    break;
                }
                Objects.requireNonNull(Bukkit.getEntity(KMorph.morphed.get(player.getUniqueId()))).remove();
                KMorph.morphed.remove(player.getUniqueId());
                player.setInvisible(false);
                player.setInvulnerable(false);
                break;
    }

    return true;
}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {

        List<String> tr = new ArrayList<>();

        if(args.length == 1) {
            tr.add("help");
            tr.add("set");
            tr.add("remove");
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("set")) {
            for(EntityType et : EntityType.values()) {
                tr.add(et.name().toLowerCase());
            }
        }
        return tr;
    }
}
