package com.mitsugaru.karmiclives.services;

import org.bukkit.command.CommandSender;

import com.mitsugaru.karmiclives.permissions.PermissionNode;

public interface IPermissionHandler {

   boolean has(CommandSender sender, PermissionNode node);
   
}
