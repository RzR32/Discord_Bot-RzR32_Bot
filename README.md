# RzR32 Discord-Bot (based on [JDA](https://github.com/DV8FromTheWorld/JDA))

```
The main function of the bot is it, to capture and save the activities of the members,
if desired a role is added to each game and can then be mentioned.
```

## Overview
<a href="#Features">Features</a><br>
<a href="#how-to-start">How to Start</a><br>
<a href="#what-can-i-edit">What can I edit</a><br>
<a href="#permissions">Permissions</a>

## Features
In addition to the main function, the bot has a few **counter**, like ...

- `GUILD`
    - Member
    - Role
    - Category
    - TextChannel
    - VoiceChannel
    - Emote
- `GAME`
    - Game (games in the file)
    - GamePlaying
- `OTHER`
    - TwitchFollower [The [API](https://api.crunchprank.net/twitch/followcount/$user) for this counter is not mine, so if its not work, I cant help for the moment]
 
indicates when a member...
+ joins / leaves
+ change his User- & Nickname

and you can delete messages (with filter like: categoryID, texthchannelID, roleID, UserID, ...)

## How to start
- Create [Bot Account](https://discordapp.com/developers/applications/me)
- Add following Permissions and invite it to your Server, you can use [this](https://discordapi.com/permissions.html#268561616) tool:
(Permissions Integer = 268561616)

    ### Permissions
    #### General Permissions
    - View Audit Log
    - Manage Roles (bot need 2FA, if Server also has 2FA on)
    - Manage Channels (bot need 2FA, if Server also has 2FA on)
    #### Text Permissions
    - Read Messages
    - Send Messages
    - Manage Messages (bot need 2FA, if Server also has 2FA on)
    - Embed Links
    - Attach Files
    - Read Message History
    - Add Reactions
    #### Voice Permissions
    - View Channel
- start the latest release (if available) or build your own jar-file
- start the jar with `java -jar <jar-file-name>.jar`
- follow the instructions at your discord server
    ## What can I edit
    - At the begin, the Bot will create one textchannel with your ID (only for u and the Bot)
    - Then the Bot will send all possible Settings as Message, you can React to them to de-/activate them (a overview is also displayed in the channel).
    - After that you write ">start" - the Bot will finally start