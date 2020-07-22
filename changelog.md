### v0.2
- **Development**
	- version variable in DRPGBot
	- added getGuildMembers to DatabaseConnector
	- renamed CommandHandler to CommandManager because "Handler" is already an interface and has nothing to do with this shit
	- added "PagedEmbeds" to make embeds that have multiple pages that can be scrolled through with reactions as buttons.
	- added test item category with 3 test items for... testing...
	- added a default item image (with "no image" text) for showing if no imageurl was set.
	- improved item registration
	- added 'onMasterCommand' and 'onCharacterRequiredCommand' to filter out command conditions

- **Major**
	- \>joinguild command
	- Improved >help to have multiple pages (still needs polishing)
	- added inventory and commands:
		- \>inv: to see your inventory
		- \>inv <@User>: to see another user's inventory
		- \>createitem (mastercommand): to give an item to an user. For testing.

- **Fixed**
	- fixed an issue with PagedEmbedBuilder when total lines was less than lines per page.

### v0.3-PRE

- **Development**
	- set '>deletechar' to also delete the character's inventory
	- added class 'BattleCharacter' to keep the transient information needed for the battle
	- added 'Equipment' class to keep a character's equipment. Behaves like Inventory.
	- removed 'CharacterFactory' to move to new system. Now 'Character' can be initialized by it's 'userID', then you call 'loadFromDatabase()' so it loads itself. This behaviour should be the standard for this app.
	- added 'AlreadyInPartyException' to throw when you try to add an user to a party that it's already in.

- **Major**
	- added a "Battle channel" for a guild. This channel will be used to spam everything related to guild battles, as well as receiving battle commands from users.
	- added '>setbattlechannel' command, to set the current channel as the battle channel.
	- added equipment for each characters that contains the weapon to be used in battle
		- added '>equip' command to show your current equipment
		- added '>equip \<itemID>' command to equip an item from your inventory
		- added '>unequip' command to unequip your current weapon

- **Fixed**
	- fixed characters keeping their 'GuildID' after deleting their guild
	- fixed characters/guild error when saving to database


