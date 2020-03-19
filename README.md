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
- Add following Permissions and invite it to your Server, you can use [this](https://discordapi.com/permissions.html) tool:
(Permissions Integer = 268561616)
    ### Permissions
    #### General Permissions
    - View Audit Log
    - Manage Roles
    - Manage Chanels
    - View Channels
    #### Text Permissions
    - Send Messages
    - Menage Messages
    - Embed Links
    - Attach Files
    - Read Message History
    - Add Reactions
    <!-- here for later add
    #### Voice Permissions
    - 
    --->
- start the latest release (if available) or build your own jar-file
- start the jar with `java -jar <jar-file-name>.jar`
- follow the instructions at your discord server
    ## What can I edit
    - You can deactivate the following features
    ```
    >maincount_on=true
    >gamecategory_on=true 
    >streamcategory_on=true
    >twitchname_on=true
    >twitchcount_on=true
    >membercount_on=true
    >rolecount_on=true
    >gamerolecount_on=true
    >categorycount_on=true
    >textchannelcount_on=true
    >voicechannelcount_on=true
    >gamecount_on=true
    >playingcount_on=true
    >games_on=true
    >logs_on=true
    >bot-channel_on=true
    >bot-zustimmung_on=true
    ```
    - to edit the configuration you can use the command `>settings set <feature> <true/false>`
    
