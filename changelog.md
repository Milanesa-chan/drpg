### v0.4-PRE

- **Development**
	- created the StatCard class that creates itself from its ItemID
	- added code to ItemFactory.getItemFromID() in case a statcard ItemID is required
	- added statcards to the equipment
	- unequip now removes your entire equipment
	- created the 'Answerable' interface and the 'AnswerManager' for two-part commands
	- created the FusionHandler for the command '>fuse'
	- added 'getStat(int)' to StatCard to get a stat determined from a number (0 = str, 1 = acc, 2 = vit, 3 = mag)
	- rewrote the '>equip' command's handle method because it was shit
	- created a "draft" of the quests system as a .json in the res directory

- **Major**
	- added 'stat cards' for the character to equip and have its statistics
	- added "two-part commands" that will be used for complex interactions through commands
	- added '>fuse' command to fuse two or more cards and get one, better card

- **Fixed**
	- character no longer keeps its equipment after being deleted
	- fixed duplication on inventories with only 1 item which caused equipment duplication

### v0.3

- **Development**
	- set '>deletechar' to also delete the character's inventory
	- added class 'BattleCharacter' to keep the transient information needed for the battle
	- added 'Equipment' class to keep a character's equipment. Behaves like Inventory.
	- removed 'CharacterFactory' to move to new system. Now 'Character' can be initialized by it's 'userID', then you call 'loadFromDatabase()' so it loads itself. This behaviour should be the standard for this app.
	- added 'AlreadyInPartyException' to throw when you try to add an user to a party that it's already in.
	- added 'HandlerFilter' object as a more complex way to execute Handlers when they need certain conditions. It is used by setting it's conditions to the desired ones and then using 'filterHandler' to execute the command.
	- created 'res' folder for resources. It will include images that will be used for battles and such.
	- created 'Prompt' class to get static and generic Strings for user output
	- started a simple match making system. Every 5 seconds a loop looks if two teams of the same size are queueing and matches them
	- created 'PartyRequired' and 'PartyMemberRequired' handler filters
	- added 'getReady' to GuildParty to check if every user and the party are ready for queueing
	- added 'parameters' packet where classes with static variables will hold information such as 'character initial max health' like an .ini file
	- added relevant stats to the Weapon class
	- started the BattleImageGenerator class
	- made Guild load itself from database. This is the last object that used the old 'Factory' system. Items will keep using the 'ItemFactory' since it makes sense for this specific case
	- added 'lane' as a field in BattleCharacter
	- added exception for unassigned lane when queueing

- **Major**
	- added a "Battle channel" for a guild. This channel will be used to spam everything related to guild battles, as well as receiving battle commands from users.
	- added '>setbattlechannel' command, to set the current channel as the battle channel.
	- added equipment for each characters that contains the weapon to be used in battle
		- added '>equip' command to show your current equipment
		- added '>equip \<itemID>' command to equip an item from your inventory
		- added '>unequip' command to unequip your current weapon
	- added '>battle' command to start a battle party with the command sender as first member.
	- added '>invite \<@User>' to invite users to join the party.
	- added '>party' command to see the current party.
	- added '>queue' command to start/stop matchmaking.
	- changed HP for new characters from 100 to 500.
	- added lanes to characters in parties, you can see the lanes of all characters in party with the '>party' command
	- added '>setlane back/front' command to set your lane

- **Fixed**
	- fixed characters keeping their 'GuildID' after deleting their guild
	- fixed characters/guild error when saving to database
	- solved equipment-related issue in BattleCharacter constructor

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

