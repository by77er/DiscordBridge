# DiscordBridge
This Spigot / Bukkit plugin + Discord bot combination allows you to bridge chat messages (bi-directionally) between your Minecraft server and a channel on your Discord server.

## To build this plugin, follow these steps:
* Grab the project:
    ```bash
    git clone DiscordBridge
    cd DiscordBridge
    ```
* Download the latest Spigot API `.jar` and place it in `libs/` (you may need to create this directory)
* Modify `build.gradle`'s `compile files('libs/spigot-api-X.jar')` line to reflect the filename of the library you downloaded.
* Build the project:
    ```bash
    ./gradlew build
    ```
* Grab the `.jar` from `builds/libs/DiscordBridge.jar` and put it in your server's `plugins` directory.

### After you've done the steps above, you'll need to do the following to run the bot:
* Run the following commands:
    ```bash
    cd js
    npm install
    ```
* Export the `DSBTOKEN` environment variable with your token.
* Set the name of the channel you'd like to receive message events from in `index.js`.
* Run `node index.js` and follow the setup procedure.
