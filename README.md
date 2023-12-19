# MicroBattles

MicroBattles is a mini-game inspired by MinePlex's old MicroBattles mini-game. The idea is simple:
there are a fixed number of teams on a floating island which will start breaking faster as time
goes by.

## Components

**MicroBattles** is based on several components that makes everything work as intended:

- **Engine**: This is the plugin's "engine", used to configure new game instances. It contains
  information such as database credentials, match settings and so on. You can create an engine
  on your own, but there's a built-in tool that makes it easy to create and automatically save
  engine templates, [EngineFactory](https://github.com/Nasgar-Network/MicroBattles-2/engine-factory).
  **Note that EngineFactory requires NodeJS**.
- [Plugin](https://github.com/Nasgar-Network/MicroBattles-2/plugin): This is the mini-game itself. When
  a player requests a **match** in a **lobby**, a new server is created. That server is the one which will
  host the match, so the plugin is installed and **automatically** configured. When everything is set up,
  those players who requested the match are sent to the server. If nobody joins the server, it's deleted
  after a fixed-time.
- [Lobby](https://github.com/Nasgar-Network/MicroBattles-2/lobby): This is the mini-game's "gateway";
  it allows players to handle their profiles, join new or existing matches, and so on. Your environment
  should have at least at one **lobby** in order to enhance user experience. **It requires
  [Plugin](https://github.com/Nasgar-Network/MicroBattles-2/plugin) to work, since it's an API consumer**.
- [Setup](https://github.com/Nasgar-Network/MicroBattles-2/setup): This is the mini-game's setup entrypoint.
  it allows servers administrators to easily create arenas, kits, and so on. Server administrators may use
  the **assisted setup** feature to ease their work. **It requires
  [Plugin](https://github.com/Nasgar-Network/MicroBattles-2/plugin) to work, since it's an API consumer**.

## Setup

In order tu setup MicroBattles, your host environment must fit the following requirements: <br>

- Have access to a **Redis** server <br>
- Have access to a **MongoDB** server <br>
- Have access to a **CloudNet3** instance <br>

Once your environment fits these, you can continue.

- **Creating arenas, kits and so on**: Server administrators can use
  the [Setup](https://github.com/Nasgar-Network/MicroBattles-2/setup)
  plugin in order to easily create and configure an arena. The plugin provides an **entire command tree** (including an
  assisted setup mode). Click the hyperlink for more information.
- Once these are set up, you need to create an **engine**. To do so, we highly recommend using the
  [EngineFactory](https://github.com/Nasgar-Network/MicroBattles-2/engine-factory) tool as following: <br>
  ```shell
  node engine-factory/engine-factory.js
  ```
  This will start an interactive command-line interface which will help you to generate and save an **engine**. <br>
  **You can create as many engines as you need**.
- The next step is to create a **lobby** server. To do so, you must export the **lobby template** to **CloudNet3**. You
  can make use of the setup plugin to automatize this. Once the template is exported, **CloudNet** will create a default
  server you can join.
- Once you have the **lobby** servers, everything is done. To check if there was any error during the setup, join a
  **lobby** server and request a new match in any arena. After a few seconds, you should be sent to another server with
  a waiting match.

## How does this work

As you can see, the plugins follows a **modular architecture**. The game logic is separated from the lobby logic.
To achieve harmony, these components use the **Pub/Sub** pattern using **Redis**.

#### Caching strategies:

- Matches: Matches are stored in Redis. It allows players to re-connect to their previous **match** if they suddenly
  disconnect during the game.
- Users: Users follow a local-caching strategy. This means that a user playing a match cannot be directly accessed from,
  for instance, a **lobby**.
- Arenas: Arenas are stored in Redis. They have a fixed TTL to guarantee fast access without unnecessary resource
  consumption.
- Kits: Kits are stored in Redis. They have a fixed TTL to guarantee fast access without unnecessary resource
  consumption.

#### Communication across components:

- From **lobby** to **game servers**: **Lobby** servers are in charge of requesting new **game servers**. When a player
  requests a **match** with a given **arena**, the **lobby** servers tries to find a free, running **game** server with
  the provided **arena**. If none was found, a new one is created. Once everything is ready on the **game server**, the
  player is sent using BungeeCord's plugin messaging system.
- From **game servers** to **lobby**: When a **match** ends, players can choose two different options: play another
  match
  or return to the lobby. When the first option is chosen, the player is directly sent to a **lobby server**. On the
  other hand, when the second option is chosen, the **game server** will request a **waiting match** to any **lobby**.
  so the player can be sent.

# Bug reporting & suggestions

If you want to suggest something or report a bug, go to the [Issues](https://github.com/Nasgar-Network/MicroBattles-2)
section and provide a detailed description of the bug/suggestion. If you're reporting
a bug, **make sure you provide the steps to reproduce the bug**. Otherwise, your bug report will be
most likely ignored.