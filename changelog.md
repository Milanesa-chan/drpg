**v0.2**
--- version variable in DRPGBot
- >joinguild command
--- added getGuildMembers to DatabaseConnector
--- renamed CommandHandler to CommandManager because "Handler" is already an interface and has nothing to do with this shit
--- added "PagedEmbeds" to make embeds that have multiple pages that can be scrolled through with reactions as buttons.
- Improved >help to have multiple pages (still needs polishing)
--- added test item category with 3 test items for... testing...
--- added a default item image (with "no image" text) for showing if no imageurl was set.
--- improved item registration
--- added 'onMasterCommand' and 'onCharacterRequiredCommand' to filter out command conditions
- added inventory and commands:
	>inv: to see your inventory
	>inv <@User>: to see another user's inventory
	>createitem (mastercommand): to give an item to an user. For testing.
---- fixed an issue with PagedEmbedBuilder when total lines was less than lines per page.

**v0.3-PRE**
---- fixed characters keeping their 'GuildID' after deleting their guild
--- set '>deletechar' to also delete the character's inventory
